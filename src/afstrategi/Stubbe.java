package afstrategi;

public class Stubbe extends NaturEnhet{
    
    public Stubbe(int xKordArg, int yKordArg)
    {   
        super(xKordArg, yKordArg);
        
        this.bild = Grafik.stubbBild;
        this.resursNamn = "";
        this.ärStubbe = true;
        this.planteringsIntervallOriginal = 10000;
        this.ärHinder = false;
    }
    
        @Override
    public void föröka(int xArg, int yArg, Natur natur)
    {
    }
}