public class Node {
    private int utility;
    private int x;
    private int y;

    private int board[][];
    private int weight[][];

    private int childNum = 0;
    private Node[] childList;

    public void setUtility(int utility) {
        this.utility = utility;
    }

    public int getUtility() {
        return utility;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getChildNum() {
        return childNum;
    }

    public void setChildNum(int childNum) {
        this.childNum = childNum;
    }

    public void setChildList(Node[] childList) {
        this.childList = childList;
    }

    public Node[] getChildList() {
        return childList;
    }

    public void createChildNode(Node node, int size, int[] x, int[] y) {
        node.childNum = size;
        node.childList = new Node[size];

        for (int i = 0; i < childList.length; ++i) {
            childList[i] = new Node();

            node.childList[i].utility = 0;
            node.childList[i].x = x[i];
            node.childList[i].y = y[i];

        }
    }

    void createAllTree(Node node, int level) {

    }

    void deleteNode(Node node) {
        int size = node.childNum;

        for(int i=0;i<size;++i )
            deleteNode(node.childList[i]);
    }

    Node firstLeafNode(Node node) {
        if(node.childList != null)
            return firstLeafNode(node.childList[0]);
        else
            return node;
    }
}