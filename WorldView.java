import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Optional;

/*
WorldView ideally mostly controls drawing the current part of the whole world
that we can see based on the viewport
*/

final class WorldView
{
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;
   public Viewport viewport;
   private PImage tester;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

   public void shiftView(int colDelta, int rowDelta)
   {
      int newCol = clamp(this.viewport.getCol() + colDelta, 0,
              this.world.getNumCols() - this.viewport.getNumCols());
      int newRow = clamp(this.viewport.getRow() + rowDelta, 0,
              this.world.getNumRows() - this.viewport.getNumRows());

      this.viewport.shift(newCol, newRow);
   }

   private static int clamp(int value, int low, int high) ///***FH
   {
      return Math.min(high, Math.max(value, low));
   }



   public void drawViewport()
   {
      ArrayList<Tile> tileList = new ArrayList<>();

      for(BackTile bt: this.world.backTiles)
      {
         if(bt.getSplattered() == 1)
         {
            tileList.add(new BackTile(bt.position, bt.splatterBackground, bt.splatterBackground));
         }
         else
         {
            tileList.add(bt);
         }
      }


      for(ObstacleTile ot: this.world.obstacleTiles)
      {
         tileList.add(ot);
      }

      for(VirusTile vt: this.world.getVirusList())
      {
         tileList.add(vt);
      }

      for(EnemyTile et: this.world.enemyTiles)
      {
         tileList.add(et);
      }

      this.drawTiles(tileList);

      this.drawPlayer();
   }



   public void drawPlayer()
   {
      player p = this.world.Human;
      Point pos = p.position;
      if (this.viewport.contains(pos)) {
         Point viewPoint = this.viewport.worldToViewport(pos.getX(), pos.getY());

         //this.screen.rect(viewPoint.getX() * this.tileWidth, viewPoint.getY() * this.tileHeight,this.tileHeight,this.tileWidth);

         this.screen.image(p.imagio, viewPoint.getX() * this.tileWidth, viewPoint.getY() * this.tileHeight);
         //this.screen.image(VirtualWorld.getImages().getImageList(),
         // viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight);
         //viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight));
      }
   }

   public void drawTiles(ArrayList<Tile> tiles)
   {
      for(Tile tile: tiles)
      {
         Point pos = tile.position;
         Point pov = this.viewport.worldToViewport(pos.getX(), pos.getY());
         this.screen.image(tile.pic, pov.getX() * this.tileWidth, pov.getY() * this.tileHeight);
      }
   }

//   public ArrayList<VirusTile> getVirusTiles()
//   {
//      ArrayList<VirusTile> virusList = new ArrayList<>();
//      for(EnemyTile et: this.world.enemyTiles)
//      {
//         if(et.getVirusList() != null)
//         {
//            for(VirusTile vt: et.getVirusList())
//            {
//               virusList.add(vt);
//            }
//         }
//      }
//
//      ArrayList<VirusTile> virusTilesToDraw = new ArrayList<>();
//      for(VirusTile vt: virusList)
//      {
//         ArrayList<VirusTile> vtToDraw = vt.getDrawList();
//         for(VirusTile vtVT: vtToDraw)
//         {
//            virusTilesToDraw.add(vtVT);
//         }
//      }
//      return virusTilesToDraw;
//   }






}
