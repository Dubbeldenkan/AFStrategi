package afstrategi;

public class Sten extends NaturEnhet{
    
    public Sten(int xKordArg, int yKordArg)
    {   
        super(xKordArg, yKordArg);
        
        this.bild = Grafik.stenBild;
        this.resursNamn = "";
        this.ärStubbe = true;
        this.planteringsIntervallOriginal = 10000;
    }
}