package pro.anton.averin.networking.testrest.ui.views.jsonviewer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import pro.anton.averin.networking.testrest.Config;

/**
 * Created by AAverin on 16.01.14.
 */
public class JsonTreeViewer extends ScrollView {

    JsonTreeViewerListener callback = null;
    private Context context;
    private JSONObject mJsonObject;
    private TreeProcessAsyncTask processAsyncTask;
    private int jsonDepthLevel = 0;

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
    }

    public void setJSONObject(JSONObject object) {
        mJsonObject = object;
    }

    public void cancel() {
        if (processAsyncTask != null) {
            processAsyncTask.cancel(true);
        }
    }

    public void showTree(JsonTreeViewerListener callback) {
        if (mJsonObject == null) {
            return;
        }
        this.callback = callback;

        removeAllViews();
        processAsyncTask = new TreeProcessAsyncTask();

        processAsyncTask.execute(mJsonObject);
    }

    private void processResult(ViewGroup viewGroup) {
        final ViewGroup instance = this;
        ViewTreeObserver vto = instance.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                instance.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (callback != null) {
                    callback.onFinish();
                }
            }
        });
        addView(viewGroup);
    }

    private void processJSONObject(JSONObject jsonObject, NodeView nodeRoot) throws JSONException {
        jsonDepthLevel++;
        Iterator<?> jsonIterator = jsonObject.keys();
        nodeRoot.openBracket("{");
        while (jsonIterator.hasNext()) {
            String key = (String) jsonIterator.next();
            Object value = jsonObject.get(key);
            processTreeObject(key, value, nodeRoot);
        }
        nodeRoot.closeBracket("}");
        jsonDepthLevel--;
    }

    private void processJSONArray(JSONArray jsonArray, NodeView nodeRoot) throws JSONException {
        nodeRoot.openBracket("[");
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            processTreeObject(null, value, nodeRoot);
        }
        nodeRoot.closeBracket("]");
    }

    private void processTreeObject(String key, Object value, NodeView nodeRoot) throws JSONException {
        if (value instanceof JSONObject) {
            NodeView node = new NodeView(context, key, jsonDepthLevel <= Config.MAX_JSON_COLLAPSE_LEVEL);
            nodeRoot.addView(node);
            processJSONObject((JSONObject) value, node);
        } else if (value instanceof JSONArray) {
            NodeView node = new NodeView(context, key, jsonDepthLevel <= Config.MAX_JSON_COLLAPSE_LEVEL);
            nodeRoot.addView(node);
            processJSONArray((JSONArray) value, node);
        } else {
            nodeRoot.addView(new LeafView(context, key, value));
        }
    }

    public interface JsonTreeViewerListener {
        public void onFinish();
    }

    private class TreeProcessAsyncTask extends AsyncTask<JSONObject, Void, ViewGroup> {

        @Override
        protected ViewGroup doInBackground(JSONObject... params) {
            NodeView resultView = new NodeView(context);
            resultView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            resultView.setOrientation(LinearLayout.VERTICAL);
            try {
                processJSONObject(mJsonObject, resultView);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resultView;
        }

        @Override
        protected void onPostExecute(ViewGroup viewGroup) {
            processResult(viewGroup);
        }
    }

}
