package afstrategi;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Lada extends Byggnad{
    
    public static int PRIS = 30;
    public static int PRODUKTION = 30;
    public static int STORLEKX = 2;
    public static int STORLEKY = 2;
    private static int startLiv = 100;
    
    public Lada(int xKordArg, int yKordArg, boolean färdigByggd, Spelare spelaren)
            throws IOException{        
        super(xKordArg, yKordArg, STORLEKX, STORLEKY, färdigByggd, spelaren, startLiv);
        antalKnappar = 2;
        
        this.produktion = PRODUKTION;
        this.bild = Grafik.lada;
        this.panelBild = Grafik.vit;
        this.byggBild = Grafik.ladaByggBild;
        this.antalKnappar = 2;
        this.knappVektor = new Knapp[antalKnappar];
        this.knappVektor[0] = new SkapaKoKnapp(xKord, yKord);
        this.knappVektor[1] = new SkapaRåttaKnapp(xKord, yKord);
        this.namn = "Lada";
        this.pris = PRIS;
    }
}
