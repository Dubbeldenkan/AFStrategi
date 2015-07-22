package afstrategi;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SkapaLadaKnapp extends Knapp{
    
    public SkapaLadaKnapp(int xArg, int yArg)
    {
        super(xArg, yArg, Grafik.ladaPanel);
    }
    
    @Override
    public void TryckHändelse(Spelare spelaren, int xArg, int yArg)
    {  
        if((spelaren.getAntalByggnader() < AFStrategi.MAXBYGGNADER) &&
                (spelaren.getPengar() >= Lada.PRIS))
        {
            try {
                spelaren.setByggnad(new Lada(-10, -10, false, spelaren));
                //return new LadaProduktion(xArg, yArg);
            } catch (IOException ex) {
                Logger.getLogger(SkapaLadaKnapp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
