package afstrategi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Grafik {
    static BufferedImage gräs;
    static BufferedImage golv;
    static BufferedImage orange;
    static BufferedImage röd;
    static BufferedImage svart;
    static BufferedImage turkos;
    static BufferedImage vit;
    static BufferedImage bakgrundBild;
    static BufferedImage gräsBakgrund;
    static BufferedImage ko;
    static BufferedImage koPanel;
    static BufferedImage målBild;
    static BufferedImage lada;
    static BufferedImage ladaPanel;
    static BufferedImage råtta;
    static BufferedImage ladaByggBild;
    static BufferedImage piltornPanel;
    static BufferedImage piltornBild;
    static BufferedImage skottBild;
    static BufferedImage trädBild;
    static BufferedImage stubbBild;
    static BufferedImage guldBild;
    static BufferedImage stenBild;
    static BufferedImage ängBild;
    
    static int bredd = 1050;
    static int höjd = 550;
    static int höjdPanel = 150 + höjd;
    static int förstaKnappenPosX = 300;
    static int förstaKnappenPosY = höjd + 10;
    static int storlekPåRuta = 50;
    static int höjdLiv = 3;
    static int antalCellerX = bredd / storlekPåRuta;
    static int antalCellerY = höjd / storlekPåRuta;
    private static int storlekPåKarta = 2;
    private PosPar målPosPar;
    boolean målaMålBoolean = false;
    private PosPar musTrycktPosPar;
    int MAXKNAPPARPÅPANEL = 5;
    int PLACERINGFÖRSTAKNAPPEN = 270;
    int PLACERINGKNAPPAR = 60;
    int PLACERINGFÖRSTAPRODUKTION = 
            PLACERINGFÖRSTAKNAPPEN + MAXKNAPPARPÅPANEL*PLACERINGKNAPPAR;
    int avståndKarta = 10;
    
    private final int förstaBildPåPanel = 200;
    
    public Grafik() throws IOException {
        gräs = ImageIO.read(getClass().getResource("Bilder/gräs.png"));
        golv = ImageIO.read(getClass().getResource("Bilder/golv.png"));
        orange = ImageIO.read(getClass().getResource("Bilder/orange.png"));
        röd = ImageIO.read(getClass().getResource("Bilder/röd.png"));
        svart = ImageIO.read(getClass().getResource("Bilder/svart.png"));
        turkos = ImageIO.read(getClass().getResource("Bilder/turkos.png"));
        vit = ImageIO.read(getClass().getResource("Bilder/vit.png"));
        gräsBakgrund = ImageIO.read(getClass().getResource("Bilder/gräsBakgrund.png"));
        ko = ImageIO.read(getClass().getResource("Bilder/koÖst.png"));
        koPanel = ImageIO.read(getClass().getResource("Bilder/koÖst.png"));
        målBild = ImageIO.read(getClass().getResource("Bilder/turkos.png"));
        lada = ImageIO.read(getClass().getResource("Bilder/rosa100x100.png"));
        råtta = ImageIO.read(getClass().getResource("Bilder/råtta.png"));
        ladaPanel = ImageIO.read(getClass().getResource("Bilder/vit.png"));
        ladaByggBild = ImageIO.read(getClass().getResource("Bilder/vit100x100.png"));
        piltornPanel = ImageIO.read(getClass().getResource("Bilder/turkos.png"));
        piltornBild = ImageIO.read(getClass().getResource("Bilder/turkos.png"));
        skottBild = ImageIO.read(getClass().getResource("Bilder/Skott.png"));
        trädBild = ImageIO.read(getClass().getResource("Bilder/träd.png"));
        stubbBild = ImageIO.read(getClass().getResource("Bilder/stubbe.png"));
        guldBild = ImageIO.read(getClass().getResource("Bilder/guld.png"));
        stenBild = ImageIO.read(getClass().getResource("Bilder/sten.png"));
        ängBild = ImageIO.read(getClass().getResource("Bilder/äng.png"));
    }
    
    public void målaMarkering(int xKoord, int yKoord, Graphics g, int storlekX,
            int storlekY, int storlekPåRutaArg)
    {
        int tjocklek = 1;
        int längd = storlekPåRutaArg;
        g.setColor(Color.white);
                                
        g.fillRect(xKoord*storlekPåRutaArg,
                yKoord*storlekPåRutaArg, tjocklek, längd*storlekY);
        g.fillRect(xKoord*storlekPåRutaArg,
                yKoord*storlekPåRutaArg, längd*storlekX, tjocklek);
        g.fillRect(xKoord*storlekPåRutaArg,
                yKoord*storlekPåRutaArg + längd*storlekY - tjocklek, längd*storlekX, tjocklek);
        g.fillRect(xKoord*storlekPåRutaArg + längd*storlekX - tjocklek, 
                yKoord*storlekPåRutaArg, tjocklek, längd*storlekY);
    }
    
    public void målaMusTryckt(int xKord, int yKord, Graphics g, Spelare spelaren)
    {
        målaMarkering(musTrycktPosPar.getX() - spelaren.getxKord(),
                musTrycktPosPar.getY() - spelaren.getyKord(), g, 
                xKord - musTrycktPosPar.getX(), 
                yKord - musTrycktPosPar.getY(), storlekPåRuta);
    }
    
    public void målaByggByggnad(int xKord, int yKord, Graphics g, 
            Spelare spelaren, BufferedImage rutBild)
    {
        g.drawImage(rutBild, storlekPåRuta*(xKord - spelaren.getxKord()),
                storlekPåRuta*(yKord - spelaren.getyKord()), null);
    }
    
    public void målaLiv(Graphics g, SpelarEnhet enhet, 
            Spelare spelaren)
    {
        g.setColor(Color.red);
        g.fillRect((enhet.getXKord() - spelaren.getxKord())*storlekPåRuta + 1,
                (enhet.getYKord() - spelaren.getyKord())*storlekPåRuta +
                        storlekPåRuta*enhet.getStorlekY() - höjdLiv,
                enhet.getStorlekX()*storlekPåRuta - 1, höjdLiv);
        g.setColor(Color.green);
        g.fillRect(((enhet.getXKord() - spelaren.getxKord())*storlekPåRuta) + 1,
                (enhet.getYKord() - spelaren.getyKord())*storlekPåRuta +
                        storlekPåRuta*enhet.getStorlekY() - höjdLiv,
                (int) Math.round((double) enhet.getStorlekX()*storlekPåRuta*
                ((double) enhet.getLiv()/enhet.getUrsprungligtLiv())) - 1, höjdLiv);
    }
    
    public void målaPanelen(Graphics g, Spelare spelaren)
    {
        BufferedImage rutBild;
        g.setColor(Color.black);
        g.fillRect(0,höjd, bredd, höjdPanel);
        g.setColor(Color.white);
        g.fillRect(0,höjd, bredd,1);
        g.drawString("Pengar: " + spelaren.getPengar(), 20, höjd + 20);
        g.drawString("Trä: " + spelaren.getTrä(), 20, höjd + 40);
        g.drawString("Mat: " + spelaren.getMat(), 20, höjd + 60);
        g.drawString("Gödsel: " + spelaren.getGödsel(), 20, höjd + 80);
        g.drawString("Population: " + spelaren.getAntalGubbar() + "/" + AFStrategi.MAXGUBBAR, 20, höjd + 100);
        g.drawString("Antal Byggnader: " + spelaren.getAntalByggnader() + "/" + AFStrategi.MAXBYGGNADER, 20, höjd + 120);
        int nrValdaGubbar = 0;
        for (int gubbIndex = 0; gubbIndex < spelaren.getAntalGubbar(); gubbIndex++)
        {
            if (spelaren.getGubbe(gubbIndex).getVald())
            {
                int placeringY;
                int placeringX;
                if(nrValdaGubbar < MAXKNAPPARPÅPANEL)
                {
                    placeringY = höjd + 10;
                    placeringX = förstaBildPåPanel + 60*nrValdaGubbar; 
                }
                else
                {
                    placeringY = höjd + storlekPåRuta + 30;
                    placeringX = förstaBildPåPanel + 60*(nrValdaGubbar - MAXKNAPPARPÅPANEL); 
                }
                rutBild = spelaren.getGubbe(gubbIndex).getPanelBild();
                g.drawImage(rutBild, placeringX, placeringY, null);
                g.drawString(spelaren.getGubbe(gubbIndex).getNamn() + "", placeringX, placeringY + 5);
                g.drawString("Liv: " + spelaren.getGubbe(gubbIndex).getLiv(), placeringX, placeringY + 60);
                nrValdaGubbar = nrValdaGubbar + 1;
                if(spelaren.getAntalValda() == 1)
                {
                    målaKnappar(g, spelaren.getGubbe(gubbIndex));
                }
            }
        }
        for (int byggnadIndex = 0; byggnadIndex < spelaren.getAntalByggnader(); byggnadIndex++)
        {
            if(spelaren.getByggnad(byggnadIndex).getVald())
            {
                if(spelaren.getByggnad(byggnadIndex).getFärdigbyggd())
                    {
                    SpelarEnhet tempEnhet = spelaren.getByggnad(byggnadIndex);
                    rutBild = tempEnhet.getPanelBild();
                    g.drawImage(rutBild, förstaBildPåPanel , höjd + 10, null);
                    g.drawString("Liv: " + tempEnhet.getLiv(), förstaBildPåPanel, höjd + 70);
                    målaKnappar(g, tempEnhet);
                    målaProduktion(g, tempEnhet);
                }
                else
                {
                    SpelarEnhet tempEnhet = spelaren.getByggnad(byggnadIndex);
                    rutBild = tempEnhet.getPanelBild();
                    g.drawImage(rutBild, förstaBildPåPanel , höjd + 10, null);
                    g.drawString( (int) tempEnhet.getProduktionsProcent() + " %", förstaBildPåPanel + 30, höjd + 70);
                }
            }
        }
    }
    
    public void målaKnappar(Graphics g, SpelarEnhet enhet)
    {
        BufferedImage rutBild;
        g.fillRect(PLACERINGFÖRSTAKNAPPEN, höjd + 10, 1, 80);
        int antalBilder = 0;
        for(int knappIndex = 0; knappIndex < enhet.antalKnappar; knappIndex++)
        {
            rutBild = enhet.knappVektor[knappIndex].getBild();
            g.drawImage(rutBild, förstaKnappenPosX + PLACERINGKNAPPAR*antalBilder, förstaKnappenPosY, null);
            antalBilder = antalBilder + 1;
        }
        if(!(enhet.getResursNamn() == ""))
        {
            g.drawString(enhet.getResursNamn() + ": " +
                    enhet.getResursMängd(), förstaBildPåPanel,
                    höjd + storlekPåRuta + 50);
        }
    }
    
    public void målaProduktion(Graphics g, SpelarEnhet enhet)
    {
        if(enhet.getAntalProduktion() > 0)
        {
            BufferedImage rutBild;
            g.fillRect(PLACERINGFÖRSTAPRODUKTION, höjd + 10, 1, 80);
            int antalBilder = 0;
            for(int produktionsIndex = 0; produktionsIndex < enhet.getAntalProduktion(); produktionsIndex++)
            {
                rutBild = enhet.getProduktionObjekt(produktionsIndex).getBild();
                g.drawImage(rutBild, PLACERINGFÖRSTAPRODUKTION + 30 + PLACERINGKNAPPAR*antalBilder,
                        förstaKnappenPosY, null);
                antalBilder = antalBilder + 1;
            }
            g.drawString( (int) enhet.getProduktionsProcent() + " %", PLACERINGFÖRSTAPRODUKTION + 30, höjd + 70);
        }
    }
    
    public void målaNärhet(Graphics g, int[][] närhet,
            SpelarEnhet[][] metaNärhet, Spelare spelaren)
    {
        BufferedImage rutBild;
        int typAvRuta;
        for (int xIndex = 0; xIndex < getAntalCellerX(); xIndex++) {
            for (int yIndex = 0; yIndex < getAntalCellerY(); yIndex++) {
                typAvRuta = närhet[xIndex][yIndex];
                rutBild = bildFrånRutTyp(typAvRuta);
                g.drawImage(rutBild, xIndex*getStorlekPåRuta(), yIndex*getStorlekPåRuta(), null);
            }
        }
        for (int xIndex = 0; xIndex < getAntalCellerX(); xIndex++) {
            for (int yIndex = 0; yIndex < getAntalCellerY(); yIndex++) {
                
                SpelarEnhet tempSpelarEnhet = metaNärhet[xIndex][yIndex];
                // Om det är en byggnad
                if((!tempSpelarEnhet.getÄrDummy()) && tempSpelarEnhet.getÄrByggnad())
                {
                    rutBild = tempSpelarEnhet.getBild();
                    g.drawImage(rutBild, (tempSpelarEnhet.getXKord() - spelaren.getxKord())*storlekPåRuta, 
                            (tempSpelarEnhet.getYKord() - spelaren.getyKord())*storlekPåRuta, null);
                    målaLag(g, tempSpelarEnhet, spelaren);
                    målaLiv(g, tempSpelarEnhet, spelaren);
                    if(tempSpelarEnhet.getVald())
                    {
                          målaMarkering(tempSpelarEnhet.getXKord() - spelaren.getxKord(),
                                  tempSpelarEnhet.getYKord() - spelaren.getyKord(), g,
                                  tempSpelarEnhet.getStorlekX(),
                                  tempSpelarEnhet.getStorlekY(),
                                  getStorlekPåRuta());                      
                    }
                }
                // Om det är en gubbe
                else if((!tempSpelarEnhet.getÄrDummy()) && !tempSpelarEnhet.getÄrByggnad())
                {
                    rutBild = tempSpelarEnhet.getBild();
                    g.drawImage(rutBild, xIndex*getStorlekPåRuta(), yIndex*getStorlekPåRuta(), null);
                    if(!tempSpelarEnhet.getÄrSkott())
                    {
                        målaLiv(g, tempSpelarEnhet, spelaren);
                        målaLag(g, tempSpelarEnhet, spelaren);
                    }
                    if(tempSpelarEnhet.getVald())
                    {
                          målaMarkering(tempSpelarEnhet.getXKord() - spelaren.getxKord(),
                                  tempSpelarEnhet.getYKord() - spelaren.getyKord(), g,
                                  1, 1, getStorlekPåRuta());                      
                    }
                }
            }
        }
    }
    
    public void målaNaturNärhet(Graphics g, NaturEnhet[][] naturNärhet, 
            Spelare spelaren)
    {
        BufferedImage rutBild;
        for (int xIndex = 0; xIndex < getAntalCellerX(); xIndex++) {
            for (int yIndex = 0; yIndex < getAntalCellerY(); yIndex++) 
            {               
                NaturEnhet tempNaturEnhet = naturNärhet[xIndex][yIndex];
                if(!tempNaturEnhet.getÄrDummy())
                {
                    rutBild = tempNaturEnhet.getBild();
                    g.drawImage(rutBild, xIndex*getStorlekPåRuta(), yIndex*getStorlekPåRuta(), null);
                    målaResursPåNaturEnhet(g, tempNaturEnhet, spelaren);
                }
            }
        }
    }
    
    public void målaResursPåNaturEnhet(Graphics g, NaturEnhet enhet, 
            Spelare spelaren)
    {
        if(!enhet.ärStubbe)
        {
            g.setColor(Color.red);
            g.fillRect((enhet.getXKord() - spelaren.getxKord())*storlekPåRuta + 1,
                    (enhet.getYKord() - spelaren.getyKord())*storlekPåRuta +
                            storlekPåRuta*enhet.getStorlekY() - höjdLiv,
                    enhet.getStorlekX()*storlekPåRuta - 1, höjdLiv);
            g.setColor(Color.blue);
            g.fillRect(((enhet.getXKord() - spelaren.getxKord())*storlekPåRuta) + 1,
                    (enhet.getYKord() - spelaren.getyKord())*storlekPåRuta +
                            storlekPåRuta*enhet.getStorlekY() - höjdLiv,
                    (int) Math.round((double) enhet.getStorlekX()*storlekPåRuta*
                    ((double) enhet.getResursMängd()/enhet.getResursMängdOriginal())) - 1,
                    höjdLiv);
        }
    }
    
    public void målaLag(Graphics g, SpelarEnhet tempSpelarEnhet, Spelare spelaren)
    {
        g = getLagFärg(tempSpelarEnhet.getLag(), g);
        g.fillRect((tempSpelarEnhet.getXKord() - spelaren.getxKord())*storlekPåRuta, 
                (tempSpelarEnhet.getYKord() - spelaren.getyKord())*storlekPåRuta, 
                10, 10);
    }
    
    public void målaMål(int xArg, int yArg, Graphics g)
    {
        g.drawImage(målBild, xArg*getStorlekPåRuta(), yArg*getStorlekPåRuta(), null);
    }
    
    public void målaKarta(Graphics g, Bana banan, Spelare spelaren)
    {
        int kartaX = bredd - storlekPåKarta*banan.getXLängd() - avståndKarta;
        int kartaY = höjdPanel - storlekPåKarta*banan.getYLängd() - avståndKarta;
        for(int xKord = 0; xKord < banan.getXLängd(); xKord++)
        {
            for(int yKord = 0; yKord < banan.getYLängd(); yKord++)
            {
                SpelarEnhet tempSpelarenhet = banan.getMetaBanMatris(xKord, yKord);
                int banInt = banan.getBanMatris(xKord, yKord);
                NaturEnhet naturEnhet = banan.getNaturBanMatris(xKord, yKord);
                if((tempSpelarenhet.getLag() != -1) &&
                        (!tempSpelarenhet.getÄrSkott()))
                {
                    g = getLagFärg(banan.getMetaBanMatris(xKord, yKord).getLag(), g);
                }
                else if(banInt == 1)
                {
                    g.setColor(Color.black);
                }
                else if(!naturEnhet.getÄrDummy())
                {
                    g.setColor(Color.lightGray);
                }
                else
                {
                    g.setColor(Color.green);
                }
                g.fillRect(kartaX + storlekPåKarta*xKord,
                        kartaY + storlekPåKarta*yKord, 
                        storlekPåKarta, storlekPåKarta);
            }
        }
        målaMarkering(kartaX + spelaren.getxKord()*storlekPåKarta,
                kartaY + spelaren.getyKord()*storlekPåKarta,
                g, antalCellerX*storlekPåKarta, antalCellerY*storlekPåKarta, 1);
    }
    
    public BufferedImage bildFrånRutTyp(int typAvRuta) {
    BufferedImage b = vit;
    switch (typAvRuta) {
        case 0:
            b = gräs;
            break;
        case 1:
            b = röd;
            break;
        case 2:
            b = svart;
            break;
        case 3:
            b = golv;
            break;
        case 4:
            b = turkos;
            break;
        case 5:
            b = orange;
            break;
        case 8:
        case 9:
        case 10:
            b = vit;
            break;
    }
    return b;
    }
    
    public int getBredd()
    {
        return bredd;
    }
    
    public int getHöjd()
    {
        return höjd;
    }
    
    public int getHöjdPanel()
    {
        return höjdPanel;
    }
    
    public int getAntalCellerX()
    {
        return antalCellerX;
    }
    
    public int getAntalCellerY()
    {
        return antalCellerY;
    }
    
    public int getStorlekPåRuta()
    {
        return storlekPåRuta;
    }
    
    public int getFörstaKnappenPosX()
    {
        return förstaKnappenPosX;
    }
    
    public int getFörstaKnappenPosY()
    {
        return förstaKnappenPosY;
    }
    
    public PosPar getMålPosPar()
    {
        return målPosPar;
    }
    
    public void setMålPosPar(int xArg, int yArg)
    {
        målaMålBoolean = true;
        målPosPar = new PosPar(xArg, yArg);
    }
    
    public void setMusTrycktPosPar(int xArg, int yArg)
    {
        musTrycktPosPar = new PosPar(xArg, yArg);
    }
    
    public PosPar getMusTrycktPosPar()
    {
        return musTrycktPosPar;
    }
    
    public Graphics getLagFärg(int färgInt, Graphics g)
    {
        switch (färgInt)
        {
            case 0: g.setColor(Color.red);
                break;
            case 1: g.setColor(Color.blue);
                break;
            default: g.setColor(Color.green);
                break;
        }
        return g;
    }
    
    public int getAvståndKarta()
    {
        return avståndKarta;
    }
    
    public int getStorlekPåKarta()
    {
        return storlekPåKarta;
    }
}
