package afstrategi;

import java.io.IOException;

public class Ko extends Gubbe{
    
    public static int PRIS = 20;
    public static int PRODUKTION = 5;//20;
    private static int startLiv = 10;
    private int produktionProcent = 0;
    private int matInnanGödsel = 100;
    
    public Ko(int xKordArg, int yKordArg, Spelare spelaren) throws IOException{
        super(xKordArg, yKordArg, spelaren, startLiv);
        this.bild = Grafik.ko;
        this.panelBild = Grafik.ko;
        this.aktivBild = bild;//ImageIO.read(getClass().getResource("Bilder/koÖst.png"));
        this.väntaFörFlytt = 10;
        this.produktion = 10;
        this.pris = PRIS;
        this.maxBärKapacitet = 40;

        this.namn = "Ko";
    }
    
    @Override
    public void FlyttaGubbe(Bana banan, Spelare spelaren)
    {
        if(((!vägVektor.isEmpty()))
                && (tidSedanFlytt > väntaFörFlytt))
        {
            int tempXRikt = vägVektor.get(0).getX() - xKord;
            int tempYRikt = vägVektor.get(0).getY() - yKord;
            //Om nästa ruta är Ok
            if(checkaOmRutaÄrOk(banan, new PosPar(xKord + tempXRikt, yKord + tempYRikt), false))
            {
                addXKord(tempXRikt);
                addYKord(tempYRikt);
                transformeraBild();
                vägVektor.remove(0);
                setVäntaFörFlyttNoll();
                banan.uppdateraMetaBana(spelaren);
            }
            // Om kon står bredvid målet och målet är en byggnad
            else if(!checkaOmRutaÄrOk(banan, new PosPar(xKordMål, yKordMål), false) &&
                    banan.getMetaBanMatris(xKordMål, yKordMål).getÄrByggnad() && 
                    (Math.abs(xKordMål - xKord + yKordMål - yKord) <= 1))
            {
                banan.getMetaBanMatris(xKordMål, yKordMål).addAntalArbetare();
                arbetsByggnad = banan.getMetaBanMatris(xKordMål, yKordMål);
            }
            // Om målet ruta ej är OK
            else if(!checkaOmRutaÄrOk(banan, new PosPar(xKordMål, yKordMål), false))
            {
                hittaVäg(banan);
                int slutgiltigtAvståndX = 
                        Math.abs(vägVektor.get(vägVektor.size() - 1).getX() - xKordMål);
                int slutgiltigtAvståndY = 
                        Math.abs(vägVektor.get(vägVektor.size() - 1).getY() - yKordMål);
                int nuvarandeAvståndX = 
                        Math.abs(xKord - xKordMål);
                int nuvarandeAvståndY = 
                        Math.abs(yKord - yKordMål);
                if((slutgiltigtAvståndX + slutgiltigtAvståndY) >= 
                        (nuvarandeAvståndX + nuvarandeAvståndY))
                {
                    vägVektor.clear();
                }
            }
        }
        // Om råttan står på målet och målet är en naturEnhet
        else if(!checkaOmRutaÄrOk(banan, new PosPar(xKordMål, yKordMål), false) &&
                !banan.getNaturBanMatris(xKordMål, yKordMål).getÄrDummy() && 
                (Math.abs(xKordMål - xKord + yKordMål - yKord) == 0) &&
                ((Math.abs(xKordMål - xKord) + Math.abs(yKordMål - yKord)) < 1))
        {
            banan.getNaturBanMatris(xKordMål, yKordMål).addAntalArbetare();
            arbetsEnhet = banan.getNaturBanMatris(xKordMål, yKordMål);
            if(!banan.getNaturBanMatris(xKordMål, yKordMål).getResurs().equals(arbetsEnhet.getResurs()))
            {
                resursNamn = arbetsEnhet.getResurs();
                resursMängd = 0;
            }
        }
        else
        {
            addTidSedanFlytt();
        }
    }
        
    @Override
    public void action(Natur natur, Bana banan)
    {
        if(arbetsByggnad != null)
        {
            lämnaResurser(arbetsByggnad, banan, spelaren, natur);
        }
        if(!(arbetsEnhet == null) && (((Math.abs(xKord - arbetsEnhet.getXKord()) + 
            Math.abs(yKord - arbetsEnhet.getYKord())) > 1) ||
            arbetsEnhet.getDöd()))
            {
                arbetsEnhet = null;
            }
        if(!(arbetsEnhet == null) && (resursMängd < maxBärKapacitet))
        {
            resursNamn = arbetsEnhet.getResurs();
            if(produktionProcent >= 100)
            {
                resursMängd = resursMängd + 1;
                arbetsEnhet.taResurs();
                produktionProcent = 0;
                if(arbetsEnhet.getResursMängd() < 1)
                {
                   arbetsEnhet.dö(arbetsEnhet.getXKord(), 
                           arbetsEnhet.getYKord(), natur, banan); 
                }
            }
            else
            {
                produktionProcent = produktionProcent + PRODUKTION;
            }
        }
        else if((resursMängd == maxBärKapacitet) &&
                ((Math.abs(xKordMål - xKord) + Math.abs(yKordMål - yKord)) < 1))
        {
            PosPar tempPosPar = hittaNärmasteByggnad(spelaren, "Lada");
            xKordMål = tempPosPar.getX();
            yKordMål = tempPosPar.getY();
            hittaVäg(banan);
        }
    }
    
    public void lämnaResurser(SpelarEnhet byggnad, Bana banan, Spelare spelaren,
            Natur natur)
    {
        PosPar tempPosPar = new PosPar(-1, -1);
        if("Mat".equals(resursNamn) && ("Lada".equals(byggnad.getNamn())))
        {
            spelaren.läggTillResurs(resursMängd, resursNamn);
            resursNamn = "";
            resursMängd = 0;
            tempPosPar = hittaNärmasteResurs(natur, "Mat");
        }
        if((tempPosPar.getX() != -1) && (tempPosPar.getY() != -1))
        {
            xKordMål = tempPosPar.getX();
            yKordMål = tempPosPar.getY();
            hittaVäg(banan);
        }
    }
}
