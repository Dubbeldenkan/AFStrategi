package afstrategi;

import static afstrategi.SpelarEnhet.Grafik;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Piltorn extends Byggnad{
    
    public static int PRIS = 10;
    public static int PRODUKTION = 5;
    public static int STORLEKX = 1;
    public static int STORLEKY = 1;
    private static int startLiv = 50;
    
    public Piltorn(int xKordArg, int yKordArg, boolean färdigByggd, Spelare spelaren)
            throws IOException{        
        super(xKordArg, yKordArg, STORLEKX, STORLEKY, färdigByggd, spelaren, startLiv);
        antalKnappar = 2;
        
        this.produktion = PRODUKTION;
        this.bild = Grafik.piltornBild;
        this.panelBild = Grafik.vit;
        this.byggBild = Grafik.röd;
        this.antalKnappar = 0;
        this.namn = "Piltorn";
        this.pris = PRIS;
        this.räckvidd = 4;
        this.anfallsIntervall = 2;
        this.attackVärde = 2;
    }
}