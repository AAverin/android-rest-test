package pro.anton.averin.networking.testrest.views.androidviews.jsonviewer;

public interface ViewContract {
    NodeView getRootNodeView();

    LeafView getPrimitiveLeaf(String key, String value);

    NodeView getObjectNode(String key, boolean expandable);

    NodeView getArrayNode(String key, boolean expandable);
}
