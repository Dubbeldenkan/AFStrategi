package afstrategi;

public class Träd extends NaturEnhet{

    public static int RESURS = 100;
    
    public Träd(int xKordArg, int yKordArg)
    {   
        super(xKordArg, yKordArg);
        
        this.bild = Grafik.trädBild;
        this.resursNamn = "Trä";
        this.resursMängd = RESURS;
        this.resursMängdOriginal = RESURS;
        this.ärTräd = true;
        this.planteringsIntervallOriginal = 2000;
    }
    
    @Override
    public void föröka(int xArg, int yArg, Natur natur)
    {
        natur.addEnhet(new Träd(xArg, yArg));
    }
    
    @Override
    public void dö(int xArg, int yArg, Natur natur, Bana banan)
    {
        död = true;
        natur.taBortEnhet(this, banan, xKord, yKord);
        natur.addEnhet(new Stubbe(xArg, yArg));
    }
}
