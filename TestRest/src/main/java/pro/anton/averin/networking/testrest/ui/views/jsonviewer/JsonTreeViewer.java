package pro.anton.averin.networking.testrest.ui.views.jsonviewer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by AAverin on 16.01.14.
 */
public class JsonTreeViewer extends ScrollView {

    private Context context;
    private JSONObject mJsonObject;

    private TreeProcessAsyncTask processAsyncTask;

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

        showTree();
    }

    public void setJSONObject(JSONObject object) {
        mJsonObject = object;
    }

    public void cancel() {
        processAsyncTask.cancel(true);
    }


    public void showTree() {
        if (mJsonObject == null) {
            return;
        }

        processAsyncTask = new TreeProcessAsyncTask();

        processAsyncTask.execute(mJsonObject);
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
            addView(viewGroup);
        }
    }

    private void processJSONObject(JSONObject jsonObject, NodeView nodeRoot) throws JSONException{
        Iterator<?> jsonIterator = jsonObject.keys();
        nodeRoot.openBracket("{");
        while (jsonIterator.hasNext()) {
            String key = (String) jsonIterator.next();
            Object value = jsonObject.get(key);
            Log.d("JsonTreeViewer", key + ":" + value);
            processTreeObject(key, value, nodeRoot);
        }
        nodeRoot.closeBracket("}");
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
