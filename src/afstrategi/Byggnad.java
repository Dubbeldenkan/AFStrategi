package afstrategi;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Byggnad extends SpelarEnhet{

    private double produktionProcent;
    protected boolean färdigbyggd;
    protected ArrayList <ProduktionsObjekt> produktionsVektor = new ArrayList<ProduktionsObjekt>();
    protected int antalArbetare;
    protected BufferedImage byggBild;
    protected boolean placerad = false;
    protected int räckvidd = 0;
    protected int anfallsIntervall = 0;
    protected double tidSedanAnfall;
    
    public int MAXPRODUKTION = 5;
    
    public Byggnad(int xKordArg, int yKordArg, int storlekX, int storlekY, 
            boolean färdigByggd, Spelare spelaren, int livArg){
        super(xKordArg, yKordArg, true, spelaren, livArg);
        this.produktionProcent = 0;
        
        this.storlekX = storlekX;
        this.storlekY = storlekY;
        this.färdigbyggd = färdigByggd;
        this.placerad = färdigByggd;
    }    
    
//    @Override
//    public int getYKordHöger()
//    {
//        return yKordNedre;
//    }
//    
//    @Override
//    public int getXKordHöger()
//    {
//        return xKordHöger;
//    }
    
    @Override
    public int getAntalProduktion()
    {
        return produktionsVektor.size();
    }
    
//    public void setProduktionsProcent(int inArg)
//    {
//        produktionProcent = inArg;
//    }
    
    @Override
    public double getProduktionsProcent()
    {
        return produktionProcent;
    }
    
    @Override
    public void addProduktionsVektor(ProduktionsObjekt objekt)
    {
        if(produktionsVektor.size() < MAXPRODUKTION)
        {
            produktionsVektor.add(objekt);
        }
    }
    
    public void producera(Spelare spelaren)
    {
        if(!produktionsVektor.isEmpty())
        {
            double temp1 = (double) AFStrategi.SLEEPTIME/1000;
            double temp2 = (double) 100/produktionsVektor.get(0).getProduktion();
            produktionProcent = produktionProcent +
                    temp1*temp2;
            if(produktionProcent >= 100)
            {
                produktionsVektor.get(0).produktionKlar(spelaren);
                produktionsVektor.remove(0);
                produktionProcent = 0;
            }
        }
        else if(!färdigbyggd && placerad)
        {
            double temp1 = (double) AFStrategi.SLEEPTIME/1000;
            double temp2 = (double) 100/produktion;
            produktionProcent = produktionProcent +
                    antalArbetare*temp1*temp2;
            if(produktionProcent >= 100)
            {
                färdigbyggd = true;
                produktionProcent = 0;
            }
        }
    }
    
    @Override
    public void addAntalArbetare()
    {
        antalArbetare = antalArbetare + 1;
    }
    
    @Override
    public void subAntalArbetare()
    {
        antalArbetare = antalArbetare - 1;
    }
    
    @Override
    public ProduktionsObjekt getProduktionObjekt(int index)
    {
        return produktionsVektor.get(index);
    }
    
    @Override
    public boolean getFärdigbyggd()
    {
        return färdigbyggd;
    }
    
    public void byggnadKlar()
    {
        färdigbyggd = true;
    }
    
    public boolean placeraByggnad(int xArg, int yArg, Spelare spelaren, Bana banan)
    {
        if(!placerad)
        {
//            int tempXKordHöger = xArg + xKordHöger - xKord;
//            int tempYKordNedre = yArg + yKordNedre - yKord;
            boolean rutaOk = true;
            for(int xIndex = xArg; xIndex <= xArg + storlekX; xIndex++)
            {
                for(int yIndex = yArg; yIndex <= yArg + storlekY; yIndex++)
                {
                    if(!checkaOmRutaÄrOk(banan, new PosPar(xIndex, yIndex), false))
                    {
                        rutaOk = false;
                    }
                }
            }
            if(rutaOk)
            {
                placerad = true;
//                xKordHöger = tempXKordHöger;
//                yKordNedre = tempYKordNedre;
                xKord = xArg;
                yKord = yArg;
                spelaren.resetByggnad();
                spelaren.addByggnad(this);
                for(int knappIndex = 0; knappIndex < antalKnappar; knappIndex++)
                {
                    knappVektor[knappIndex].xKord = xArg;
                    knappVektor[knappIndex].yKord = yArg;
                }
            }
            return rutaOk;
        }
        else
        {
            return true;
        }
    }
    
    @Override
    public BufferedImage getBild(){
        BufferedImage b;
        if(färdigbyggd || !placerad)
        {
            b = bild;
        }
        else
        {
            b = byggBild;
        }
        return b;
    }
    
    public void action(Spelare spelaren, Bana banan) throws IOException
    {
        if(!(anfallsEnhet == null) && (((Math.abs(xKord - anfallsEnhet.getXKord()) + 
                Math.abs(yKord - anfallsEnhet.getYKord())) > räckvidd) ||
                anfallsEnhet.getDöd()))
                {
                    anfallsEnhet = null;
                }
        if((anfallsEnhet == null) && färdigbyggd && (attackVärde > 0))
        {
            for(int xIndex = xKord - räckvidd; xIndex <= xKord + räckvidd; xIndex++)
            {
                for(int yIndex = yKord - räckvidd + Math.abs(xIndex - xKord);
                        yIndex <= yKord + räckvidd - Math.abs(xIndex - xKord);
                        yIndex++)
                {
                    if((xIndex >= 0) && (xIndex < banan.getXLängd()) &&
                            (yIndex >= 0) && (yIndex < banan.getYLängd()) &&
                            !banan.getMetaBanMatris(xIndex, yIndex).getÄrDummy() && 
                            (banan.getMetaBanMatris(xIndex, yIndex).getLag() != getLag()))
                    {
                        anfallsEnhet = banan.getMetaBanMatris(xIndex, yIndex);
                        break;
                    }
                }
            }
        }
        if((tidSedanAnfall >= anfallsIntervall) && !(anfallsEnhet == null))
        {
            if(spelaren.getAntalSkott() < AFStrategi.MAXSKOTT)
            {
                spelaren.addSkott(new Skott(xKord, yKord, spelaren,
                    anfallsEnhet.getXKord(), anfallsEnhet.getYKord(), banan,
                attackVärde, this));
                tidSedanAnfall = 0;
            }
        }
        else
        {
            tidSedanAnfall = tidSedanAnfall + (double) AFStrategi.SLEEPTIME/1000;
        }
    }
}
