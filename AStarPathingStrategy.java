import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class AStarPathingStrategy
        implements PathingStrategy
{
    List<ASNode> OpenList = new ArrayList<>();
    List<ASNode> CheckedList = new ArrayList<>();
    Predicate<Point> canPassThrough;
    Point Goal;
    boolean found = false;

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        this.OpenList.clear();
        this.CheckedList.clear();
        this.found = false;
        this.canPassThrough = canPassThrough;

        this.Goal = end;
        OpenList.add(new ASNode(start, dFromG(start,end), 0, null));
        while (!this.found && this.OpenList.size() > 0){// && this.CheckedList.size()<10000000){
            //ystem.out.println(this.CheckedList.size());

            int OlistSize = this.OpenList.size();
            for (int k = 0; k < OlistSize; k++) {
                if(withinReach.test(this.OpenList.get(k).getP(), Goal)){
                    addValuesToOpen(this.OpenList.get(k));
                    //this.CheckedList.add(this.OpenList.get(k));
                    //this.OpenList.remove(k);
                    this.found = true;
                    break;
                }else{
                    int index = getMininOlist();
                    addValuesToOpen(this.OpenList.get(index));
                    this.CheckedList.add(this.OpenList.get(index));
                    this.OpenList.remove(index);
                    OlistSize = OlistSize-1;
                    if (inOlist(this.Goal)){this.found = true;
                        break;}

                }

                }

        }
        if (this.OpenList.size() == 0){List<Point> path = new ArrayList<>();return path;}
        return makePath();
    }

    public int ComputeH(int steps, Point e, Point c)// should have steps taken instead of Point S
     {
         int distance = Math.abs(c.x - e.x)+Math.abs(c.y - e.y)+steps;

        return distance;
    }

    public int getMininOlist(){
        ASNode temp = this.OpenList.get(0);
        int indexp = 0;
        for (int i = 0; i < this.OpenList.size(); i++) {
            if (this.OpenList.get(i).getF() < temp.getF()){
                temp = this.OpenList.get(i);
                indexp = i;}
        }
        return indexp;
    }
    public boolean inOlist(Point CheckedP){
        boolean out = false;
        for (int i = 0; i < this.OpenList.size(); i++) {
            if (this.OpenList.get(i).getP().x == CheckedP.x && this.OpenList.get(i).getP().y == CheckedP.y) {
                out = true;
                break;
            }
        }
        return out;
    }

    public boolean inClist(Point CheckedP){
        boolean out = false;
        for (int i = 0; i < this.CheckedList.size(); i++) {
            if (this.CheckedList.get(i).getP().x == CheckedP.x && this.CheckedList.get(i).getP().y == CheckedP.y) {
                out = true;
                break;
            }
        }
        return out;
    }

    public List<Point> makePath(){
        List<Point> path = new ArrayList<>();
        ASNode temp = null;
        for (int i = 0; i < this.OpenList.size(); i++) {
            if (this.OpenList.get(i).getP().x == this.Goal.x && this.OpenList.get(i).getP().y == Goal.y) {
                temp = OpenList.get(i);
                break;
            }
        }
        while (temp.getParentNode() != null){
            path.add(temp.getP());
            temp = temp.getParentNode();
        }
        return path;
    }

    public void addValuesToOpen(ASNode Cn){
        Point cp = Cn.getP();
        List<Point> npl = new ArrayList<>();

            npl.add(new Point(cp.x, cp.y - 1));
            npl.add(new Point(cp.x, cp.y + 1));
            npl.add(new Point(cp.x - 1, cp.y));
            npl.add(new Point(cp.x + 1, cp.y));
        for (int i = 0; i <= 3; i++) {
            if (canPassThrough.test(npl.get(i)) && !inOlist(npl.get(i)) && !inClist(npl.get(i))){
                OpenList.add(new ASNode(npl.get(i),dFromG(npl.get(i),this.Goal), Cn.getG()+1, Cn));

            }
        }
    }


    public Stream<Point> getN(Point p){
        Stream<Point> CN = Stream.<Point>builder()
                .add(new Point(p.x, p.y - 1))
                .add(new Point(p.x, p.y + 1))
                .add(new Point(p.x - 1, p.y))
                .add(new Point(p.x + 1, p.y))
                .build();
        return CN;
    }

    public int dFromG(Point e, Point c){return Math.abs(c.x - e.x)+Math.abs(c.y - e.y);}



}
