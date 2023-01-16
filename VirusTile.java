import processing.core.PImage;

import java.util.ArrayList;
import java.util.Random;

public class VirusTile extends MovingTile
{
    private int difficultly;
    private int timerStart = -1;
    public EnemyTile enemyDad;
    private int size;
    private int randInt;

    public VirusTile(Point position, EnemyTile enemyDad, int timerStart)
    {
        super(position, enemyDad.getVirusPic());
        this.enemyDad = enemyDad;
        this.difficultly = enemyDad.getDifficultly();
        this.timerStart = timerStart;
        this.size = 1;
        if(timerStart == -1)
        {
            this.size = 0;
        }
        Random rand = new Random();
        this.randInt = rand.nextInt(100);
    }

    @Override
    public void checkMove(int timer, WorldModel world)
    {
        int timerInc = 600 - (difficultly*100) + 2*randInt;

        if(timer%timerInc == 0)
        {
            size+=1;
            if(size>0)
            {
                this.spread(timer, world);
            }

        }

        if(size > 2)
        {
            this.enemyDad.removeFromVirusList(this, world);
        }
    }

    public void spread(int timer, WorldModel world)
    {
        ArrayList<Point> pointList = new ArrayList<>();
        int dx = this.position.getX();
        int dy = this.position.getY();
        pointList.add(new Point(dx,dy));

        if(size>1)
        {
            Point p1 = new Point(dx,dy-1);
            Point p2 = new Point(dx+1,dy);
            Point p3 = new Point(dx,dy+1);
            Point p4 = new Point(dx-1,dy);
            Boolean pb1 = true;
            Boolean pb2 = true;
            Boolean pb3 = true;
            Boolean pb4 = true;

            for(VirusTile vt: world.getVirusList())
            {
                if((vt.position.getX() == p1.getX() && vt.position.getY() == p1.getY()) || dy < 1)
                {
                    pb1 = false;
                }
                if((vt.position.getX() == p2.getX() && vt.position.getY() == p2.getY()) || dx >= world.numCols)
                {
                    pb2 = false;
                }
                if((vt.position.getX() == p3.getX() && vt.position.getY() == p3.getY()) || dy >= world.numRows)
                {
                    pb3 = false;
                }
                if((vt.position.getX() == p4.getX() && vt.position.getY() == p4.getY()) || dx < 1)
                {
                    pb4 = false;
                }
            }

            for(ObstacleTile ot: world.obstacleTiles)
            {
                if(ot.position.getX() == p1.getX() && ot.position.getY() == p1.getY())
                {
                    pb1 = false;
                }
                if(ot.position.getX() == p2.getX() && ot.position.getY() == p2.getY())
                {
                    pb2 = false;
                }
                if(ot.position.getX() == p3.getX() && ot.position.getY() == p3.getY())
                {
                    pb3 = false;
                }
                if(ot.position.getX() == p4.getX() && ot.position.getY() == p4.getY())
                {
                    pb4 = false;
                }
            }
            if(pb1)
            {
                pointList.add(p1);
            }
            if(pb2)
            {
                pointList.add(p2);
            }
            if(pb3)
            {
                pointList.add(p3);
            }
            if(pb4)
            {
                pointList.add(p4);
            }
        }

        for(Point pt: pointList)
        {
            this.enemyDad.addToVirusList(new VirusTile(pt, this.enemyDad, -1));
        }
    }

}
