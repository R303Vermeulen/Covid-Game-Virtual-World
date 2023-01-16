import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
   public int numRows;
   public int numCols;



   public player Human;
   public ArrayList<BackTile> backTiles;
   public ArrayList<ObstacleTile> obstacleTiles;
   public ArrayList<EnemyTile> enemyTiles;


   public WorldModel(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
//      this.background = new Background[numRows][numCols];
   }

   public void addHuman(player human)
   {
      this.Human = human;
   }

   public void addFastEnemy(Point position, PImage fastPic, PImage virusPic)
   {
      this.enemyTiles.add(new FastEnemyTile(position, fastPic, 5, virusPic, fastPic));
   }

   public void clear()
   {
      this.Human = null;
      this.backTiles = null;
      this.obstacleTiles = null;
      this.enemyTiles = null;
   }


   public void addBackTileList(ArrayList<BackTile> backTiles)
   {
      this.backTiles = backTiles;
   }

   public void addObstacleTileList(ArrayList<ObstacleTile> obstacleTiles)
   {
      this.obstacleTiles = obstacleTiles;
   }

   public void addEnemyTileList(ArrayList<EnemyTile> enemyTiles)
   {
      this.enemyTiles = enemyTiles;
   }

   public ArrayList<VirusTile> getVirusList()
   {
      ArrayList<VirusTile> virusList = new ArrayList<>();
      for(EnemyTile et: this.enemyTiles)
      {
         if(et.getVirusList() != null)
         {
            for(VirusTile vt: et.getVirusList())
            {
               virusList.add(vt);
            }
         }
      }
      return virusList;
   }

   public int getNumRows() {
      return numRows;
   }

   public int getNumCols()
   {
      return numCols;
   }

}
