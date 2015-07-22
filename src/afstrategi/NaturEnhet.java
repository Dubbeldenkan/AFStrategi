package afstrategi;

import java.awt.image.BufferedImage;
import java.util.Random;

public class NaturEnhet {
    
    protected final int xKord;
    protected final int yKord;
    protected BufferedImage bild;
    protected String resursNamn;
    protected int resursMängd;
    protected int resursMängdOriginal;
    protected boolean ärDummy = false;
    protected int planteringsIntervall = 5000;
    private int tidSedanPlantering = 0;
    protected int planteringsIntervallOriginal;
    protected boolean ärTräd = false;
    protected int antalArbetare = 0;
    protected boolean död = false;
    protected boolean ärStubbe = false;
    protected boolean ärHinder = true;
    
    protected int storlekY = 1;
    protected int storlekX = 1;
    
    public NaturEnhet(int xKordArg, int yKordArg)
    {
        this.xKord = xKordArg;
        this.yKord = yKordArg;
    }
    
    public int getXKord()
    {
        return xKord;
    }
    
    public int getYKord()
    {
        return yKord;
    }
    
    public BufferedImage getBild()
    {
        return bild;
    }
    
    public String getResurs()
    {
        return resursNamn;
    }
    
    public boolean getÄrDummy()
    {
        return ärDummy;
    }
    
    public void plantera(Natur natur, Bana banan)
    {
        if(tidSedanPlantering > planteringsIntervall)
        {
            int[] planteringsOrdning = new int[4];
            Random randomGenerator = new Random();
            int antalNummer = 0;
            while(antalNummer < 4)
            {
                int nummer = randomGenerator.nextInt(5);
                if(!checkaNummer(planteringsOrdning, nummer))
                {
                    planteringsOrdning[antalNummer] = nummer;
                    antalNummer = antalNummer + 1;
                }
            }
            int planteringsIndex = 0;
            while(planteringsIndex < 4)
            {
                PosPar tempPosPar;
                switch(planteringsOrdning[planteringsIndex])
                {
                    case 1: tempPosPar = new PosPar(xKord + 1, yKord);
                        break;
                    case 2: tempPosPar = new PosPar(xKord, yKord + 1);
                        break;
                    case 3: tempPosPar = new PosPar(xKord - 1, yKord);
                        break;
                    default: tempPosPar = new PosPar(xKord, yKord - 1);
                        break;
                }
                if(checkaOmRutaÄrOk(banan, tempPosPar))
                {
                    föröka(tempPosPar.getX(), tempPosPar.getY(), natur);
                    // För att avbryta whileloopen
                    planteringsIndex = 5;
                }
                planteringsIndex = planteringsIndex + 1;
            }
            tidSedanPlantering = 0;
            planteringsIntervall = planteringsIntervallOriginal + 
                    randomGenerator.nextInt(planteringsIntervallOriginal);
        }
        else
        {
            tidSedanPlantering = tidSedanPlantering + 1;
        }
    }
    
    private boolean checkaNummer(int[] vektor, int nummer)
    {
        boolean vektorInnehåller = false;
        for(int index = 0; index < 4; index++)
        {
            if(vektor[index] == nummer)
            {
                vektorInnehåller = true;
                break;
            }
        }
        return vektorInnehåller;
    }
    
    public void föröka(int xArg, int yArg, Natur natur)
    {}
    
    public boolean checkaOmRutaÄrOk(Bana banan, PosPar posPar)
    {
        int xArg = posPar.getX();
        int yArg = posPar.getY();
        return ((yArg >= 0) &&
                (yArg < banan.getYLängd()) &&
                (xArg >= 0) &&
                    (xArg < banan.getXLängd()) &&
                (banan.getBanMatris(xArg,
                        yArg) == 0) &&
                (banan.getMetaBanMatris(xArg,
                        yArg).getÄrDummy()) && 
//                ((banan.getNaturBanMatris(xArg, 
//                        yArg).getÄrDummy()) ||
//                !banan.getNaturBanMatris(xArg, 
//                        yArg).getÄrHinder()));
                !banan.getNaturBanMatris(xArg, 
                        yArg).getÄrHinder());
    }
    
    public void addAntalArbetare()
    {
        antalArbetare = antalArbetare + 1;
    }
    
    public int getStorlekY()
    {
        return storlekY;
    }
    
    public int getStorlekX()
    {
        return storlekX;
    }
    
    public int getResursMängd()
    {
        return resursMängd;
    }
    
    public int getResursMängdOriginal()
    {
        return resursMängdOriginal;
    }
    
    public void taResurs()
    {
        resursMängd = resursMängd - 1;
    }
    
    public void dö(int xArg, int yArg, Natur natur, Bana banan)
    {}
    
    public boolean getDöd()
    {
        return död;
    }
    
    public boolean getÄrRest()
    {
        return ärStubbe;
    }
    
    public boolean getÄrHinder()
    {
        return ärHinder;
    }
}


