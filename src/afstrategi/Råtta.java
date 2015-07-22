package afstrategi;

import java.io.IOException;

public class Råtta extends Gubbe{

    public static int PRIS = 10;
    public static int PRODUKTION = 10;
    private static int startLiv = 5;
    private int produktionProcent = 0;
    
        public Råtta(int xKordArg, int yKordArg, Spelare spelaren) throws IOException{
        super(xKordArg, yKordArg, spelaren, startLiv);
        this.bild = Grafik.råtta;
        this.väntaFörFlytt = 2;
        this.pris = PRIS;
        this.antalKnappar = 2;
        this.maxBärKapacitet = 10;
        
        knappVektor = new Knapp[antalKnappar];

        this.aktivBild = bild;
        this.panelBild = bild;
        this.namn = "Råtta";
        
        
        this.knappVektor[0] = new SkapaLadaKnapp(0, 0);
        this.knappVektor[1] = new SkapaPiltornKnapp(0, 0);
    }
        
    @Override
    public void FlyttaGubbe(Bana banan, Spelare spelaren)
    {
        if(arbetsByggnad != null)
        {
            arbetsByggnad.subAntalArbetare();
            arbetsByggnad = null;
        }
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
            // Om råttan står bredvid målet och målet är en byggnad
            else if(!checkaOmRutaÄrOk(banan, new PosPar(xKordMål, yKordMål), false) &&
                    banan.getMetaBanMatris(xKordMål, yKordMål).getÄrByggnad() && 
                    (Math.abs(xKordMål - xKord + yKordMål - yKord) <= 1))
            {
                banan.getMetaBanMatris(xKordMål, yKordMål).addAntalArbetare();
                arbetsByggnad = banan.getMetaBanMatris(xKordMål, yKordMål);
            }
            // Om råttan står bredvid målet och målet är en naturEnhet
            else if(!checkaOmRutaÄrOk(banan, new PosPar(xKordMål, yKordMål), false) &&
                    !banan.getNaturBanMatris(xKordMål, yKordMål).getÄrDummy() && 
                    (Math.abs(xKordMål - xKord + yKordMål - yKord) <= 1))
            {
                banan.getNaturBanMatris(xKordMål, yKordMål).addAntalArbetare();
                arbetsEnhet = banan.getNaturBanMatris(xKordMål, yKordMål);
                if(!banan.getNaturBanMatris(xKordMål, yKordMål).getResurs().equals(arbetsEnhet.getResurs()))
                {
                    resursNamn = arbetsEnhet.getResurs();
                    resursMängd = 0;
                }
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
        else if(resursMängd == maxBärKapacitet)
        {
            PosPar tempPosPar = hittaNärmasteByggnad(spelaren, "Lada");
            xKordMål = tempPosPar.getX();
            yKordMål = tempPosPar.getY();
            //hittaVäg(banan);
        }
    }
    
    public void lämnaResurser(SpelarEnhet byggnad, Bana banan, Spelare spelaren,
            Natur natur)
    {
        PosPar tempPosPar = new PosPar(-1, -1);
        if("Trä".equals(resursNamn) && ("Lada".equals(byggnad.getNamn())))
        {
            spelaren.läggTillResurs(resursMängd, resursNamn);
            resursNamn = "";
            resursMängd = 0;
            tempPosPar = hittaNärmasteResurs(natur, "Trä");
        }
        else if("Pengar".equals(resursNamn) && ("Lada".equals(byggnad.getNamn())))
        {
            spelaren.läggTillResurs(resursMängd, resursNamn);
            resursNamn = "";
            resursMängd = 0;
            tempPosPar = hittaNärmasteResurs(natur, "Pengar");
        }
        if((tempPosPar.getX() != -1) && (tempPosPar.getY() != -1))
        {
            xKordMål = tempPosPar.getX();
            yKordMål = tempPosPar.getY();
            hittaVäg(banan);
        }
    }
}
