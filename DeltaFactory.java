import processing.core.PImage;

import java.util.ArrayList;
import java.util.Random;

public class DeltaFactory extends Factory
{
    private int percent = 12;
    private int percentInc = 15;
    private int enemyMin = 5;
    private int potentialExtra = 0;

    public DeltaFactory(WorldModel world)
    {
        super(world);
    }

    @Override
    public int getPercent() {
        return percent;
    }

    @Override
    public int getPercentInc() {
        return percentInc;
    }


    @Override
    public ArrayList<EnemyTile> getEnemies(PImage enemyImg, ArrayList<Integer> obstaclePoints, PImage virusImg, PImage fastImg)
    {
        ArrayList<Integer> enemyPoints = new ArrayList<>();
        Random rand = new Random();

        for(int i = 0; i < enemyMin; i++)
        {
            int randInt = rand.nextInt(this.world.numCols*this.world.numRows);
            while(obstaclePoints.contains(randInt) || enemyPoints.contains(randInt))
            {
                randInt = rand.nextInt(this.world.numCols*this.world.numRows);
            }
            enemyPoints.add(randInt);
        }


        ArrayList<EnemyTile> enemies = new ArrayList<>();

        for(Integer i: enemyPoints)
        {
            int x = (i%this.world.numCols);
            int y = (i-(x))/this.world.numCols;
            enemies.add(new EnemyTile(new Point(x,y), enemyImg, 2, virusImg, fastImg));
        }

        return enemies;
    }
}
