package pro.anton.averin.networking.testrest.ui.views.jsonviewer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by AAverin on 16.01.14.
 */
public class JsonTreeViewer extends LinearLayout {

    private Context context;
    private JSONObject mJsonObject;

    public JsonTreeViewer(Context context) {
        super(context);
        init(context);
    }

    public JsonTreeViewer(Context context, JSONObject object) {
        super(context);
        init(context);
        setJSONObject(object);
    }

    public JsonTreeViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setOrientation(VERTICAL);

        showTree();
    }

    public void setJSONObject(JSONObject object) {
        mJsonObject = object;
    }


    public void showTree() {
        if (mJsonObject == null) {
            return;
        }

        try {
            processJSONObject(mJsonObject, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void processJSONObject(JSONObject jsonObject, ViewGroup nodeRoot) throws JSONException{
        Iterator<?> jsonIterator = jsonObject.keys();
        while (jsonIterator.hasNext()) {
            String key = (String) jsonIterator.next();
            Object value = jsonObject.get(key);
            Log.d("JsonTreeViewer", key + " : " + value);
            processTreeObject(key, value, nodeRoot);
        }
    }

    private void processJSONArray(JSONArray jsonArray, ViewGroup nodeRoot) throws JSONException {
        TextView openBracketView = new TextView(context);
        openBracketView.setText("[");
        nodeRoot.addView(openBracketView);
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            processTreeObject(null, value, nodeRoot);
        }
        TextView closeBracketView = new TextView(context);
        closeBracketView.setText("]");
        nodeRoot.addView(closeBracketView);
    }

    private void processTreeObject(String key, Object value, ViewGroup nodeRoot) throws JSONException {
        if (value instanceof JSONObject) {
            NodeView node = new NodeView(context, key);
            nodeRoot.addView(node);
            processJSONObject((JSONObject) value, node);
        } else if (value instanceof JSONArray) {
            NodeView node = new NodeView(context, key);
            nodeRoot.addView(node);
            processJSONArray((JSONArray) value, node);
        } else {
            nodeRoot.addView(new LeafView(context, key, value));
        }
    }

}
