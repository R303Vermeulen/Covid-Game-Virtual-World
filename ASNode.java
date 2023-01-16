import java.util.Objects;

public class ASNode {
    private int h;  // distace from goal
    private int g;  // distace from start node
    private int f;  // f+h
    private Point p;
    private ASNode parentNode;
    public ASNode(Point p, int h, int g, ASNode parentNode){
        this.h = h;
        this.g = g;
        this.f = h+g;
        this.p = p;
        this.parentNode = parentNode;
    }
    public int getF() {
        return f;
    }
    public int getG() {
        return g;
    }
    public int getH() {
        return h;
    }
    public ASNode getParentNode() {
        return parentNode;
    }
    public void addPNode(ASNode newNode){this.parentNode = newNode;}
    public Point getP() {return p;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ASNode asNode = (ASNode) o;
        return (p.x == asNode.p.x && p.y == asNode.p.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p);
    }
}
