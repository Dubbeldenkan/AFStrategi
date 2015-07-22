package afstrategi;

public class Äng extends NaturEnhet{

    public static int RESURS = 50;
    
    public Äng(int xKordArg, int yKordArg)
    {   
        super(xKordArg, yKordArg);
        
        this.bild = Grafik.ängBild;
        this.resursNamn = "Mat";
        this.resursMängd = RESURS;
        this.resursMängdOriginal = RESURS;
        this.ärHinder = false;
        this.planteringsIntervallOriginal = 500;
    }
    
    @Override
    public void föröka(int xArg, int yArg, Natur natur)
    {
        natur.addEnhet(new Äng(xArg, yArg));
    }
    
    @Override
    public void dö(int xArg, int yArg, Natur natur, Bana banan)
    {
        död = true;
        natur.taBortEnhet(this, banan, xKord, yKord);
    }
}
