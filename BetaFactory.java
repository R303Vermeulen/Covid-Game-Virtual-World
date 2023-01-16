import processing.core.PImage;

import java.util.ArrayList;
import java.util.Random;

public class BetaFactory extends Factory
{
    private int percent = 8;
    private int percentInc = 30;
    private int enemyMin = 8;
    private int potentialExtra = 4;

    public BetaFactory(WorldModel world)
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

        enemyMin += rand.nextInt(potentialExtra+1);

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
            enemies.add(new EnemyTile(new Point(x,y), enemyImg, 1, virusImg, fastImg));
        }

        return enemies;
    }
}
