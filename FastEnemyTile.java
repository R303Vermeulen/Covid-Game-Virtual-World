import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class FastEnemyTile extends EnemyTile
{
    private int personalTimer = -1;
    public FastEnemyTile(Point position, PImage pic, int difficulty, PImage virusPic, PImage fastPic)
    {
        super(position, pic, difficulty, virusPic, fastPic);
    }

    private PathingStrategy strat = new Dijkstra();

    @Override
    public void checkMove(int timer, WorldModel world)
    {
        if(this.personalTimer<0)
        {
            personalTimer = timer;
        }

        Random rand = new Random();
        int timerInc = 20;
        if(timer%timerInc==0)
        {
            List<Point> points = null;

            points = this.strat.computePath(this.position,
                    world.Human.position,
                    p ->  this.withinBounds(p, world) && noObstacle(p, world),
                    (p1, p2) -> neighbors(p1, p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);

            if(points != null && points.size()>0)
            {
                this.move(points.get(points.size()-1));
            }
        }

        if((timer-personalTimer)>2500)
        {
            world.enemyTiles.remove(this);
        }


        if(timer%(8*timerInc) == 0)
        {
            cough(timer, this.virusPic);
        }
    }

}
