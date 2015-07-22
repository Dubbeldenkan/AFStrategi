package afstrategi;

public class Guld extends NaturEnhet{

    public static int RESURS = 1000;
    
    public Guld(int xKordArg, int yKordArg)
    {   
        super(xKordArg, yKordArg);
        
        this.bild = Grafik.guldBild;
        this.resursNamn = "Pengar";
        this.resursMängd = RESURS;
        this.resursMängdOriginal = RESURS;
        this.ärTräd = false;
        this.planteringsIntervallOriginal = 2000;
    }
    
    @Override
    public void dö(int xArg, int yArg, Natur natur, Bana banan)
    {
        död = true;
        natur.taBortEnhet(this, banan, xKord, yKord);
        natur.addEnhet(new Sten(xArg, yArg));
    }
}
