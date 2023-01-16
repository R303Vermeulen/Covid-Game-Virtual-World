import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyTile extends MovingTile
{
    private int difficultly;
    private int direction = 0;
    PImage virusPic;
    private ArrayList<VirusTile> virusList = new ArrayList<>();
    private int randInt;
    private int turnsBeforeCough = 8;
    public PathingStrategy strategy;
    private PImage fastPic;
    private int chanceOfAngryBoy = 3;


    public EnemyTile(Point position, PImage pic, int difficulty, PImage virusPic, PImage fastPic) {
        super(position, pic);
        this.difficultly = difficulty;
        this.virusPic = virusPic;
        Random rand = new Random();
        this.randInt = rand.nextInt(10);
        this.turnsBeforeCough += randInt;
        this.fastPic = fastPic;
        if(difficultly == 2)
        {
            strategy = new AStarPathingStrategy();
        }
        else
        {
            strategy = new SingleStepPathingStrategy();
        }
    }

    public ArrayList<VirusTile> getVirusList()
    {
        return this.virusList;
    }

    public void addToVirusList(VirusTile vt)
    {
        this.virusList.add(vt);
    }



    public PImage getVirusPic()
    {
        return this.virusPic;
    }

    public int getDifficultly()
    {
        return this.difficultly;
    }

    public int getDirection()
    {
        return this.direction;
    }

    public void removeFromVirusList(VirusTile virus, WorldModel world)
    {
        if(this.virusList.contains(virus))
        {
            this.virusList.remove(virus);
            int vx = virus.position.getX();
            int vy = virus.position.getY();
            boolean fastguy = false;
            for(BackTile bt: world.backTiles)
            {
                if(vx == bt.position.getX() && vy == bt.position.getY())
                {
                    Random rand = new Random();
                    int nextInt = rand.nextInt(100);
                    if(nextInt<this.chanceOfAngryBoy)
                    {
                        this.fastGuy(virus, world);
                    }
                    bt.makeSplattered();
                }
            }
        }
    }

    public void fastGuy(VirusTile virus, WorldModel world)
    {
        int vx = virus.position.getX();
        int vy = virus.position.getY();
        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(vx, vy -1));
        points.add(new Point(vx+1, vy ));
        points.add(new Point(vx, vy +1));
        points.add(new Point(vx-1, vy));
        world.addFastEnemy(virus.position, fastPic, virusPic);
        for(Point point: points)
        {
            for(BackTile bbt: world.backTiles)
            {
                if(point.x == bbt.position.x && point.y == bbt.position.y)
                {
                    bbt.makeSplattered();
                }
            }
            for(VirusTile vt: world.getVirusList())
            {
                if(point.x == vt.position.x && point.y == vt.position.y)
                {
                    this.virusList.remove(vt);
                }
            }

        }

    }



    @Override
    public void checkMove(int timer, WorldModel world)
    {
        Random rand = new Random();
        int timerInc = 80 - (difficultly*20);
        if(timer%timerInc==0)
        {
            List<Point> points = null;

            points = strategy.computePath(this.position,
                    world.Human.position,
                    p -> withinBounds(p, world) && noObstacle(p, world),
                    (p1, p2) -> neighbors(p1, p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);




            if(points != null && points.size() > 0)
            {
                this.move(points.get(points.size()-1));
            }

        }


        if(timer%(turnsBeforeCough*timerInc) == 0)
        {
            cough(timer, this.virusPic);
        }
    }

    public void cough(int timer, PImage virusPic)
    {
        this.virusList.add(new VirusTile(this.position, this, timer));
    }

    public static boolean withinBounds(Point p, WorldModel world)
    {
        return p.y >= 0 && p.y < world.numRows &&
                p.x >= 0 && p.x < world.numCols;
    }

    public boolean noObstacle(Point p, WorldModel world)
    {
        for(ObstacleTile ot: world.obstacleTiles)
        {
            if(ot.position.getX() == p.x && ot.position.getY() == p.y)
            {
                return false;
            }
        }
        return true;
    }

    public static boolean neighbors(Point p1, Point p2)
    {
        return p1.x+1 == p2.x && p1.y == p2.y ||
                p1.x-1 == p2.x && p1.y == p2.y ||
                p1.x == p2.x && p1.y+1 == p2.y ||
                p1.x == p2.x && p1.y-1 == p2.y;
    }

}

