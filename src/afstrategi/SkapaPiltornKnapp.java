package afstrategi;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SkapaPiltornKnapp extends Knapp{
    
    public SkapaPiltornKnapp(int xArg, int yArg)
    {
        super(xArg, yArg, Grafik.piltornPanel);
    }
    
    @Override
    public void TryckHändelse(Spelare spelaren, int xArg, int yArg)
    {  
        if((spelaren.getAntalByggnader() < AFStrategi.MAXBYGGNADER) &&
                (spelaren.getPengar() >= Piltorn.PRIS))
        {
            try {
                spelaren.setByggnad(new Piltorn(-10, -10, false, spelaren));
                //return new LadaProduktion(xArg, yArg);
            } catch (IOException ex) {
                Logger.getLogger(SkapaLadaKnapp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}