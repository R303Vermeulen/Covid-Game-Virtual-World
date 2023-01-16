import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Dijkstra implements PathingStrategy {
    List<DSnode> OpenList = new ArrayList<>();
    List<DSnode> CheckedList = new ArrayList<>();
    Predicate<Point> canPassThrough;
    Point Goal;
    boolean found = false;

    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
        Stream<Point> s = potentialNeighbors.apply(start);
//        getFullPath(start,end, canPassThrough, withinReach,s);

        this.Goal = end;
        this.OpenList.clear();
        this.CheckedList.clear();
        this.found = false;
        this.canPassThrough = canPassThrough;
        OpenList.add(new DSnode(start,0, null));

        while (!this.found && this.OpenList.size() > 0){

            int OlistSize = this.OpenList.size();
            for (int k = 0; k < OlistSize; k++) {
                if(withinReach.test(this.OpenList.get(k).getP(), Goal)){
                    addValuesToOpen(this.OpenList.get(k));
                    //this.CheckedList.add(this.OpenList.get(k));
                    //this.OpenList.remove(k);
                    this.found = true;
                    break;
                }else{

                    addValuesToOpen(this.OpenList.get(0));
                    this.CheckedList.add(this.OpenList.get(0));
                    this.OpenList.remove(0);
                    OlistSize = OlistSize-1;
                    if (inOlist(this.Goal)){this.found = true;
                        break;}

                }

            }

        }
        if (this.OpenList.size() == 0){List<Point> path = new ArrayList<>();return path;}
        return makePath();

    }

    public List<Point> makePath(){
        List<Point> path = new ArrayList<>();
        DSnode temp = null;
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

    public void addValuesToOpen(DSnode Cn){
        Point cp = Cn.getP();
        List<Point> npl = new ArrayList<>();

        npl.add(new Point(cp.x, cp.y - 1));
        npl.add(new Point(cp.x, cp.y + 1));
        npl.add(new Point(cp.x - 1, cp.y));
        npl.add(new Point(cp.x + 1, cp.y));
        for (int i = 0; i <= 3; i++) {
            if (canPassThrough.test(npl.get(i)) && !inOlist(npl.get(i)) && !inClist(npl.get(i))){
                OpenList.add(new DSnode(npl.get(i),Cn.getG()+1, Cn));

            }
        }
    }

    public List<Point> getFullPath(Point cp, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Stream<Point> potentialNeighbors){

        if (cp == end){


        //        potentialNeighbors.apply(start)
//                .filter(canPassThrough)
//                .filter(pt -> !pt.equals(start)
//                                && !pt.equals(end)
//                                && Math.abs(end.x - pt.x) <= Math.abs(end.x - start.x)
//                                && Math.abs(end.y - pt.y) <= Math.abs(end.y - start.y))
//                .limit(1)
//                .collect(Collectors.toList());

        return null;
    }
        potentialNeighbors.forEach((k) -> {


        });
        return null;
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

    public boolean inClist(Point CheckedP) {
        boolean out = false;
        for (DSnode dSnode : this.CheckedList) {
            if (dSnode.getP().x == CheckedP.x && dSnode.getP().y == CheckedP.y) {
                out = true;
                break;
            }
        }
        return out;
    }

    public boolean inOlist(Point CheckedP) {
        boolean out = false;
        for (DSnode dSnode : this.OpenList) {
            if (dSnode.getP().x == CheckedP.x && dSnode.getP().y == CheckedP.y) {
                out = true;
                break;
            }
        }
        return out;
    }
}


