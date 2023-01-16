import processing.core.PImage;

public class BackTile extends Tile{

    public PImage splatterBackground;
    private int splattered = 0;
    private int timer = 0;

    public BackTile(Point position, PImage pic, PImage splatPic) {
        super(position, pic);
        this.splatterBackground = splatPic;
    }

    public void makeSplattered()
    {
        this.splattered = 1;
    }

    public int getSplattered()
    {
        return this.splattered;
    }

    public void checkSplattered()
    {
        if(splattered == 1)
        {
            this.timer += 1;
        }
        if(this.timer == 300)
        {
            splattered = 0;
        }
    }

}
