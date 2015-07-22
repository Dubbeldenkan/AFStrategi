package afstrategi;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KoProduktion extends ProduktionsObjekt{
    
    KoProduktion(int xKord, int yKord)
    {
        super(Ko.PRODUKTION, xKord, yKord, Grafik.ko);
    }
    
    @Override
    public void produktionKlar(Spelare spelaren)
    {
        try {
            spelaren.addGubbe(new Ko(xKord, yKord, spelaren));
        } catch (IOException ex) {
            Logger.getLogger(KoProduktion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
