import processing.core.PImage;

import java.util.ArrayList;
import java.util.Random;

abstract class Factory {

    public WorldModel world;

    public Factory(WorldModel world)
    {
        this.world = world;
    }

    public void addWorldTiles(PImage obstacleImg, PImage enemyImg, ArrayList<PImage> backImgList, PImage virusImg, PImage splatPic, PImage fastPic)
    {

        ArrayList<Integer> obstaclePoints = addBackTilesAndAddObstaclePoints(backImgList, splatPic);

        ArrayList<ObstacleTile> obstacles = new ArrayList<>();
        for(Integer i: obstaclePoints)
        {
            int x = (i%world.numCols);
            int y = (i-(x))/(world.numCols);
            obstacles.add(new ObstacleTile(new Point(x,y), obstacleImg));
        }

        world.addObstacleTileList(obstacles);
        world.addEnemyTileList(this.getEnemies(enemyImg, obstaclePoints, virusImg, fastPic));
    }

    public ArrayList<Integer> addBackTilesAndAddObstaclePoints(ArrayList<PImage> backImgList, PImage splatPic)
    {
        ArrayList<BackTile> backTiles = new ArrayList<>();

        ArrayList<Integer> obstaclePoints = new ArrayList<>();

        Random rand = new Random();

        for(int i = 0; i<this.world.numCols; i++)
        {
            for(int j = 0; j<this.world.numRows; j++)
            {
                Point pos = new Point(i, j);

                int randInt = rand.nextInt(100);
                int picInterval = (((i*j))%3);

                if(randInt<20){picInterval=3;}

                PImage picture = backImgList.get(picInterval);
                backTiles.add(new BackTile(pos, picture, splatPic));

                int percent = this.getPercent();
                int gridPoint = j*this.world.numCols+i;

                ArrayList<Integer> direcs = new ArrayList<>();
                direcs.add(gridPoint - this.world.numCols);
                direcs.add(gridPoint + 1);
                direcs.add(gridPoint + this.world.numCols);
                direcs.add(gridPoint - 1);

                for(Integer k: direcs)
                {
                    if(obstaclePoints.contains(k))
                    {
                        percent+=this.getPercentInc();
                    }
                }
                if(randInt < percent)
                {
                    obstaclePoints.add(gridPoint);
                }
            }
        }

        world.addBackTileList(backTiles);
        return obstaclePoints;
    }

    public abstract int getPercent();

    public abstract int getPercentInc();

    public abstract ArrayList<EnemyTile> getEnemies(PImage enemyImg, ArrayList<Integer> obstaclePoints, PImage virusImg, PImage fastImg);



}
