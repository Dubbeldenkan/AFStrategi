package afstrategi;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RåttaProduktion extends ProduktionsObjekt{
    
    RåttaProduktion(int xKord, int yKord)
    {
        super(Råtta.PRODUKTION, xKord, yKord, Grafik.råtta);
    }
    
    @Override
    public void produktionKlar(Spelare spelaren)
    {
        try {
            spelaren.addGubbe(new Råtta(xKord, yKord, spelaren));
        } catch (IOException ex) {
            Logger.getLogger(RåttaProduktion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
