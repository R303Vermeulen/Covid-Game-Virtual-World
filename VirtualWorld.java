
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   private static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "world.sav";

//   private static final String FAST_FLAG = "-fast";
//   private static final String FASTER_FLAG = "-faster";
//   private static final String FASTEST_FLAG = "-fastest";
//   private static final double FAST_SCALE = 0.5;
//   private static final double FASTER_SCALE = 0.25;
//   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;


   private WorldModel world;
   private WorldView view;

   private long next_time;
   private int timer = 0;
   private int strainDifficulty = 1;
   private Factory fac;
   private boolean dead = false;
   private int partOfGame = 0;
   private ArrayList<Circle> circList = new ArrayList<>();



   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {

      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS);
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);

      for(int i= 0; i<200; i++)
      {
         Random rand = new Random();
         int x = rand.nextInt(640);
         int y = rand.nextInt(480);
         int r = 25+rand.nextInt(125);
         int c1 = 50 + rand.nextInt(150);
         int c2 = 50 + rand.nextInt(150);
         circList.add(new Circle(x,y,r,c1,c2));
      }


      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;



   }

   public void draw() {
      if (partOfGame == 0)
      {
         background(200, 200, 200);
         for (Circle circ : circList)
         {
            fill(circ.c1, circ.c2, circ.c2);
            ellipse(circ.x, circ.y, circ.r, circ.r);
         }

         fill(0);
         rect(100,25,440,100);
         rect(60, 200, 130, 200);
         rect(240, 200, 130, 200);
         rect(420, 200, 130, 200);

         fill(255);
         textSize(33);
         text("CHOOSE YOUR VARIANT", 125, 100);
         textSize(26);
         text("BETA", 91, 300);
         text("DELTA", 265, 300);
         text("OMICRON", 425, 300);
      }

      if (partOfGame == 1)
      {
         if (strainDifficulty == 1) {
            fac = new BetaFactory(world);
         }
         if (strainDifficulty == 2) {
            fac = new DeltaFactory(world);
         }
         if (strainDifficulty == 3) {
            fac = new OmicronFactory(world);
         }
         setDaWorld();
         partOfGame = 2;
      }

      if (partOfGame == 2)
      {
         long time = System.currentTimeMillis();
         if (time >= next_time) {
//         this.scheduler.updateOnTime(time);
            next_time = time + TIMER_ACTION_PERIOD;
         }

         timer++;

         for (BackTile bt : world.backTiles) {
            bt.checkSplattered();
         }

         collectTiles();
         checkCollisions();
         view.drawViewport();
      }

      if(partOfGame == 3)
      {
         background(255,0,0);
         fill(0);
         textSize(100);
         text("YOU DIED", 100, 200);
         text("HAHAHA", 100, 400);
      }
   }

   public void setDaWorld()
   {
      PImage humanimg = loadImage("images/wellMaskedBoy.bmp");
      player Jerry = new player("0", new Point(10, 7), humanimg);

      ArrayList<PImage> backImgList = new ArrayList<>();
      backImgList.add(loadImage("images/backrock1.bmp"));
      backImgList.add(loadImage("images/backrock3.bmp"));
      backImgList.add(loadImage("images/backrock5.bmp"));
      backImgList.add(loadImage("images/backrock7.bmp"));


      fac.addWorldTiles(loadImage("images/object.bmp"),
              loadImage("images/sickman.bmp"),
              backImgList,
              loadImage("images/virus.bmp"),
              loadImage("images/splatterRock.bmp"),
              loadImage("images/angryMan.bmp"));

      world.addHuman(Jerry);
   }



   public void collectTiles()
   {
      ArrayList<MovingTile> tiles = new ArrayList<>();
      for(EnemyTile et: world.enemyTiles)
      {
         tiles.add(et);
      }
      for(EnemyTile et: this.world.enemyTiles)
      {
         if(et.getVirusList() != null)
         {
            for(VirusTile vt: et.getVirusList())
            {
               tiles.add(vt);
            }
         }
      }
      checkMoves(tiles, timer);
   }

   public void checkCollisions()
   {
      Point human = this.world.Human.position;
      for(EnemyTile et: this.world.enemyTiles)
      {
         if(et.position.getX() == human.getX() && et.position.getY() == human.getY())
         {
            this.partOfGame = 3;
         }
      }

      for(VirusTile vt: this.world.getVirusList())
      {
         if(vt.position.getX() == human.getX() && vt.position.getY() == human.getY())
         {
            this.partOfGame = 3;
         }
      }

   }


   public void checkMoves(ArrayList<MovingTile> movingTiles, int timer)
   {
      for(MovingTile mt: movingTiles)
      {
         mt.checkMove(timer, world);
      }
   }

   public void mousePressed()
   {
      if(mousePressed == true)
      {
         if(partOfGame == 0)
         {
            rect(60, 200, 130, 200);
            rect(240, 200, 130, 200);
            rect(420, 200, 130, 200);
            if(mouseX > 60 && mouseX < 190 && mouseY > 200 && mouseY<400)
            {
               strainDifficulty = 1;
               partOfGame = 1;
            }
            if(mouseX > 240 && mouseX < 370 && mouseY > 200 && mouseY<400)
            {
               strainDifficulty = 2;
               partOfGame = 1;
            }
            if(mouseX > 420 && mouseX < 550 && mouseY > 200 && mouseY<400)
            {
               strainDifficulty = 3;
               partOfGame = 1;
            }

         }
         if(partOfGame == 2)
         {
            int mouseTileX = (mouseX-(mouseX%TILE_WIDTH))/TILE_WIDTH;
            int mouseTileY = (mouseY-(mouseY%TILE_HEIGHT))/TILE_HEIGHT;
            mouseTileX += this.view.viewport.getCol();
            mouseTileY += this.view.viewport.getRow();
            for(BackTile bt: this.world.backTiles)
            {
               if(bt.position.getX() == mouseTileX && bt.position.getY() == mouseTileY)
               {
                  for(VirusTile vt: this.world.getVirusList())
                  {
                     if(vt.position.getX() == mouseTileX && vt.position.getY() == mouseTileY)
                     {

                        vt.enemyDad.removeFromVirusList(vt, world);
                     }
                  }
               }
            }
         }
      }
   }


   public void keyPressed()
   {
      if (key == CODED && partOfGame == 2)
      {
         int dx = 0;
         int dy = 0;
         int vdx = 0;
         int vdy = 0;
         int hx = this.world.Human.position.getX();
         int hy = this.world.Human.position.getY();

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         if (hx+dx >= 0 && hy+dy >= 0 && hx+dx <= this.world.numCols-1 && hy+dy <= this.world.numRows-1) {// checks if out of world bounds
            Point ptm = new Point(hx+dx,hy+dy);
            Boolean Move = true;
            for(ObstacleTile ot: this.world.obstacleTiles)
            {
               if(ot.position.getX() == ptm.getX() && ot.position.getY() == ptm.getY())
               {
                  Move = false;
               }
            }
//            for (Entity k : this.world.entities)
//            {
//               if (k instanceof Obstacle && ptm.getX() == k.getPosition().getX() && ptm.getY() == k.getPosition().getY()) {
//                  //System.out.println("Stop");
//                  Move = false;
//               }
//            }
            if (Move){
               this.world.Human.position = new Point(this.world.Human.position.getX() + dx, this.world.Human.position.getY() + dy);
            }
         }
         //System.out.println("human pos: "+this.world.Human.position);
         //System.out.println(view.viewport.getCol() +" "+view.viewport.getNumCols());
         // && (this.world.Human.position.y>2 || (this.world.Human.position.x>2 && dy < 0)) && (this.world.Human.position.x>2 || (this.world.Human.position.y>2 && dy < 0))
//         if (((this.world.Human.position.getX() >= view.viewport.getNumCols()+view.viewport.getCol() || this.world.Human.position.getX() <= view.viewport.getCol()-2) || (this.world.Human.position.getY() >= view.viewport.getNumRows()+view.viewport.getRow())|| this.world.Human.position.getY() <= view.viewport.getRow()-1)){
//            view.shiftView(dx, dy);
         if ((this.world.Human.position.getX() >= view.viewport.getNumCols()+view.viewport.getCol()-5 && dx > 0) ||
                 (this.world.Human.position.getX() <= view.viewport.getCol()+4 && dx < 0) ||
                 (this.world.Human.position.getY() >= view.viewport.getNumRows()+view.viewport.getRow()-5 && dy > 0) ||
                 (this.world.Human.position.getY() <= view.viewport.getRow()+4 && dy < 0)){
            view.shiftView(dx, dy);
         }
      }
   }

   private static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }



   public static void main(String [] args)
   {
      PApplet.main(VirtualWorld.class);
   }
}
