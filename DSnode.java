import java.util.Objects;

class DSnode{

    private int g;  // distace from start node
    private Point p;
    private DSnode parentNode;
    public DSnode(Point p, int g, DSnode parentNode){
        this.g = g;
        this.p = p;
        this.parentNode = parentNode;
    }

    public DSnode getParentNode() {
        return parentNode;
    }

    public int getG() {
        return g;
    }

    public Point getP() {
        return p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DSnode dSnode = (DSnode) o;
        return g == dSnode.g && Objects.equals(p, dSnode.p) && Objects.equals(parentNode, dSnode.parentNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(g, p, parentNode);
    }
}