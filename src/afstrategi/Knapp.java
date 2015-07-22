package afstrategi;

import java.awt.image.BufferedImage;

public class Knapp {
    
    private BufferedImage Bild;
    protected int xKord;
    protected int yKord;
//    protected int lag;
    
    public Knapp(int xKord, int yKord, BufferedImage Bild)
    {
        this.xKord = xKord;
        this.yKord = yKord;
        this.Bild = Bild;
    }
    
    //Dummy funktion
    public ProduktionsObjekt TryckHändelse(Spelare spelaren)
    {
        return new DummyProduktion();
    }
    
    //Dummy funktion
    public void TryckHändelse(Spelare spelaren, int xArg, int yArg)
    {
        //return new DummyProduktion();
    }
    
    public BufferedImage getBild()
    {
        return Bild;
    }
}
