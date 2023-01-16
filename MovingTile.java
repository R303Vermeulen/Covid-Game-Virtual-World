import processing.core.PImage;

public abstract class MovingTile extends Tile{


    public MovingTile(Point position, PImage pic)
    {
        super(position, pic);
    }

    public abstract void checkMove(int timer, WorldModel world);

    public void move(Point newPos)
    {
        this.position = newPos;
    };



}
