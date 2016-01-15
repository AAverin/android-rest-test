package pro.anton.averin.networking.testrest.views.androidviews.jsonviewer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.JsonElement;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class JsonTreeView extends ScrollView implements ViewContract {

    private Logic logic;
    private Context context;

    public JsonTreeView(Context context) {
        super(context);
        init(context);
    }

    public JsonTreeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JsonTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        logic = new Logic(this);
        this.context = context;
    }

    public void setJson(JsonElement jsonObject) {
        logic.processJson(jsonObject)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NodeView>() {
                    @Override
                    public void call(NodeView nodeView) {
                        addView(nodeView);
                    }
                });
    }

    @Override
    public NodeView getRootNodeView() {
        NodeView rootNodeView = new NodeView(context);
        rootNodeView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootNodeView.setOrientation(LinearLayout.VERTICAL);

        return rootNodeView;
    }

    @Override
    public LeafView getPrimitiveLeaf(String key, String value) {
        return new LeafView(context, key, value);
    }

    @Override
    public NodeView getObjectNode(String key, boolean expandable) {
        return new NodeView(context, key, expandable);
    }

    @Override
    public NodeView getArrayNode(String key, boolean expandable) {
        return new NodeView(context, key, expandable);
    }
}