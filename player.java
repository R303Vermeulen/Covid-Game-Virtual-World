import processing.core.PImage;
import java.util.List;


public class player {
    public String id;
    public Point position;

    public PImage imagio;



    public player(String id, Point position, PImage imagio)
    {
        this.id = id;
        this.position = position;
        this.imagio = imagio;

    }

}

