package afstrategi;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpelarEnhet {
    protected int xKord;
    protected int yKord;
    protected int liv;
    public boolean vald;
    protected BufferedImage bild;
    protected static Grafik Grafik;
    protected BufferedImage panelBild;
    protected Boolean ärByggnad;
    protected String namn;
    protected int pris;
    protected int produktion;
    protected int antalKnappar;
    protected Knapp[] knappVektor;
    protected boolean ärDummy = false;
    protected int attackVärde;
    protected Spelare spelaren;
    protected SpelarEnhet anfallsEnhet;
    protected boolean död = false;
    protected int storlekX = 1;
    protected int storlekY = 1;
    protected int ursprungligtLiv;
    protected NaturEnhet arbetsEnhet;
    protected int resursMängd = 0;
    protected String resursNamn = "";
    protected SpelarEnhet arbetsByggnad;
    
    public SpelarEnhet(int xKordArg, int yKordArg, boolean ärByggnadArg, 
            Spelare spelarenArg, int liv)
    {
        this.xKord = xKordArg;
        this.yKord = yKordArg;
        vald = false;
        this.ärByggnad = ärByggnadArg;
        this.spelaren = spelarenArg;
        
        this.liv = liv;
        this.ursprungligtLiv = liv;
        
        try{
            Grafik = new Grafik();
        }
        catch (IOException e){}
    }
    
    int getXKord(){
        return xKord;
    }
    
    int getYKord(){
        return yKord;
    }
    
    void setVald(boolean valdBoolean)
    {
        vald = valdBoolean;
    }
    
    boolean getVald()
    {
        return vald;
    }
    
    int getLiv()
    {
        return liv;
    }
    
    public BufferedImage getBild(){
        BufferedImage b = bild;
        return b;
    }
    
    public BufferedImage getPanelBild(){
        BufferedImage b = panelBild;
        return b;
    }
    
    public boolean getÄrByggnad()
    {
        return ärByggnad;
    }
    
//    public int getXKordHöger()
//    {
//        return xKordHöger;
//    }
//    
//    public int getYKordNedre()
//    {
//        return yKordNedre;
//    }
    
    public int getStorlekX()
    {
        return storlekX;
    }
    
    public int getStorlekY()
    {
        return storlekY;
    }
    
    public String getNamn()
    {
        return namn;
    }
    
    public int getPris()
    {
        return pris;
    }
    
    public Knapp getKnapp(int index)
    {
        return knappVektor[index];
    }
    
    public boolean getÄrDummy()
    {
        return ärDummy;
    }
    
    public void addProduktionsVektor(ProduktionsObjekt objekt)
    {
        
    }
    
    public ProduktionsObjekt getProduktionObjekt(int index)
    {
        return new DummyProduktion();
    }
    
    public int getAntalProduktion()
    {
        return 0;
    }
    
    public double getProduktionsProcent()
    {
        return 0;
    }
    
    public boolean getFärdigbyggd()
    {
        return false;
    }
    
    public void setMål(Bana banan, int xArg, int yArg)
    {
        
    }
    
    public boolean checkaOmRutaÄrOk(Bana Banan, PosPar posPar, boolean läggTillNy)
    {
        //läggTillNy används då man testar för att lägga till en ny ruta
        int xArg = posPar.getX();
        int yArg = posPar.getY();
        return ((yArg >= 0) &&
                (yArg <= Banan.getYLängd()) &&
                (xArg >= 0) &&
                    (xArg <= Banan.getXLängd()) &&
                (Banan.getBanMatris(xArg,
                        yArg) == 0) &&
                (Banan.getMetaBanMatris(xArg,
                        yArg).getÄrDummy()) && 
                (!läggTillNy ||
                !checkaOmRutanFinns(xArg, yArg)));
    }
    
    public boolean checkaOmRutanFinns(int xArg, int yArg)
    {
        return false;
    }
    
    public void addAntalArbetare()
    {}
    
    public void subAntalArbetare()
    {}
    
    public int getLag()
    {
        if(spelaren == null)
        {
            return -1;
        }
        else
        {
            return spelaren.getLag();
        }
    }
    
    public boolean getÄrSkott()
    {
        return false;
    }
    
    public void taSkada(int skada, SpelarEnhet anfallare)
    {
        liv = liv - skada;
        if(liv <= 0)
        {
            anfallare.setAnfallsEnhet(null);
            död = true;
            if(this.getÄrByggnad())
            {
                this.spelaren.taBortByggnad(this);
            }
            else if(this.getÄrSkott())
            {
                this.spelaren.taBortSkott(this);
            }
            else
            {
                this.spelaren.taBortGubbe(this);
            }
        }
    }
    
    public void setAnfallsEnhet(SpelarEnhet enhet)
    {
        this.anfallsEnhet = enhet;
    }
    
    public boolean getDöd()
    {
        return död;
    }
    
    public int getUrsprungligtLiv()
    {
        return ursprungligtLiv;
    }
    
    public void action(Natur natur, Bana banan)
    {}
    
    public NaturEnhet getArbetsEnhet()
    {
        return arbetsEnhet;
    }
      
    public int getResursMängd()
    {
        return resursMängd;
    }
    
    public String getResursNamn()
    {
        return resursNamn;
    }
    
    public PosPar hittaNärmasteByggnad(Spelare spelaren, String Namn)
    {
        PosPar resultPosPar = new PosPar(-1, -1);
        int närmasteByggnad = 0;
        boolean enByggnadHittad = false;
        for(int byggnadIndex = 0; byggnadIndex < spelaren.getAntalByggnader();
                byggnadIndex++)
        {
            if(spelaren.getByggnad(byggnadIndex).getNamn().equals(Namn))
            {
                if(!enByggnadHittad || ((Math.abs(xKord -
                        spelaren.getByggnad(byggnadIndex).getXKord()) +
                        Math.abs(yKord - spelaren.getByggnad(byggnadIndex).getYKord())) 
                        < närmasteByggnad))
                {
                    enByggnadHittad = true;
                    närmasteByggnad = Math.abs(xKord -
                        spelaren.getByggnad(byggnadIndex).getXKord()) +
                        Math.abs(yKord - spelaren.getByggnad(byggnadIndex).getYKord());
                    resultPosPar = new PosPar(spelaren.getByggnad(byggnadIndex).getXKord(),
                            spelaren.getByggnad(byggnadIndex).getYKord()); 
                }
            }
        }
        return resultPosPar;
    }
    
    public PosPar hittaNärmasteResurs(Natur natur, String Namn)
    {
        PosPar resultPosPar = new PosPar(-1, -1);
        int närmasteEnhet = 0;
        boolean enEnhetHittad = false;
        for(int enhetIndex = 0; enhetIndex < natur.getAntalObjekt();
                enhetIndex++)
        {
            if(natur.getEnhet(enhetIndex).getResurs().equals(Namn))
            {
                if(!enEnhetHittad || ((Math.abs(xKord -
                        natur.getEnhet(enhetIndex).getXKord()) +
                        Math.abs(yKord - natur.getEnhet(enhetIndex).getYKord())) 
                        < närmasteEnhet))
                {
                    enEnhetHittad = true;
                    närmasteEnhet = Math.abs(xKord -
                        natur.getEnhet(enhetIndex).getXKord()) +
                        Math.abs(yKord - natur.getEnhet(enhetIndex).getYKord());
                    resultPosPar = new PosPar(natur.getEnhet(enhetIndex).getXKord(),
                            natur.getEnhet(enhetIndex).getYKord()); 
                }
            }
        }
        return resultPosPar;
    }
    
    public SpelarEnhet getArbetsByggnad()
    {
        return arbetsByggnad;
    }
}
