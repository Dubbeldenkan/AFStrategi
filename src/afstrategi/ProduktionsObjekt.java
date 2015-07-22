package afstrategi;

import java.awt.image.BufferedImage;

public class ProduktionsObjekt {
    
    protected final double produktion;
    protected final int xKord;
    protected final int yKord;
    protected boolean ärDummy = false;
    private final BufferedImage bild;
    
    public ProduktionsObjekt(double produktion, int xArg, int yArg, 
            BufferedImage bild)
    {
        this.produktion = produktion;
        this.xKord = xArg;
        this.yKord = yArg;
        this.bild = bild;
    }
    
    public void produktionKlar(Spelare spelaren)
    {
        
    }
    
    public double getProduktion()
    {
        return produktion;
    }
    
    public boolean getÄrDummy()
    {
        return ärDummy;
    }
    
    public BufferedImage getBild()
    {
        return bild;
    }
    
    public Byggnad startaProduktion()
    {
        return null;
    }
}