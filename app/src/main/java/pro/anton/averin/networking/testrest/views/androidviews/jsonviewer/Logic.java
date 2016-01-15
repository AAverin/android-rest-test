package pro.anton.averin.networking.testrest.views.androidviews.jsonviewer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Iterator;
import java.util.Map;

import pro.anton.averin.networking.testrest.Config;
import rx.Observable;
import rx.Subscriber;

public class Logic {

    private final ViewContract view;

    private int jsonDepthLevel = 0;

    public Logic(ViewContract view) {
        this.view = view;
    }

    public Observable<NodeView> processJson(final JsonElement json) {
        return Observable.create(new Observable.OnSubscribe<NodeView>() {
            @Override
            public void call(Subscriber<? super NodeView> subscriber) {
                NodeView rootNode = view.getRootNodeView();

                processRootElement(json, rootNode);

                subscriber.onNext(rootNode);
                subscriber.onCompleted();
            }
        });
    }

    private void increaseDepth() {
        jsonDepthLevel++;
    }

    private void decreaseDepth() {
        jsonDepthLevel--;
    }

    private boolean isNodeExpandable() {
        return jsonDepthLevel <= Config.MAX_JSON_COLLAPSE_LEVEL;
    }

    private void processRootElement(JsonElement element, NodeView parent) {
        increaseDepth();
        parent.openBracket("{");

        processElement("", element, parent);

        parent.closeBracket("}");
        decreaseDepth();
    }

    private void processElement(String key, JsonElement element, NodeView parent) {
        if (element.isJsonArray()) {
            processJsonArray(key, element.getAsJsonArray(), parent);
        } else if (element.isJsonObject()) {
            processJsonObject(key, element.getAsJsonObject(), parent);
        } else if (element.isJsonPrimitive()) {
            processJsonPrimitive(key, element.getAsJsonPrimitive(), parent);
        } else if (element.isJsonNull()) {
            processJsonNull(key, element.getAsJsonNull(), parent);
        }
    }

    private void processJsonNull(String key, JsonNull jsonNull, NodeView parent) {
        LeafView primitiveLeaf = view.getPrimitiveLeaf(key, "null");
        parent.addView(primitiveLeaf);
    }

    private void processJsonPrimitive(String key, JsonPrimitive jsonPrimitive, NodeView parent) {
        LeafView primitiveLeaf = view.getPrimitiveLeaf(key, jsonPrimitive.getAsString());
        parent.addView(primitiveLeaf);
    }

    private void processJsonObject(String key, JsonObject jsonObject, NodeView parent) {
        NodeView objectNode = view.getObjectNode(key, isNodeExpandable());
        parent.addView(objectNode);

        increaseDepth();
        objectNode.openBracket("{");

        Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> element = iterator.next();
            processElement(element.getKey(), element.getValue(), objectNode);
        }

        objectNode.closeBracket("}");
        decreaseDepth();
    }

    private void processJsonArray(String key, JsonArray jsonArray, NodeView parent) {
        NodeView arrayNode = view.getArrayNode(key, isNodeExpandable());
        parent.addView(arrayNode);

        arrayNode.openBracket("[");

        Iterator<JsonElement> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JsonElement element = iterator.next();
            processElement("", element, arrayNode);
        }
        arrayNode.closeBracket("]");
    }

}
