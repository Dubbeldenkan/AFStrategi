package afstrategi;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Spelare {
    private final String namn;
    private int xKord;
    private int yKord;
    private int pengar;
    private int trä;
    private int mat;
    private int gödsel;
    //private Gubbe[] gubbVektor;
    private ArrayList <Gubbe> gubbVektor = new ArrayList<Gubbe>();
    private ArrayList <Byggnad> byggnadVektor = new ArrayList<Byggnad>();
    private ArrayList <Skott> skottVektor = new ArrayList<Skott>();
    private int antalGubbar = 0;
    private int antalByggnader = 0;
    private ArrayList <SpelarEnhet> valdaVektor = new ArrayList<SpelarEnhet>();
    private boolean byggerByggnad;
    private Byggnad byggByggnad;
    private final int lag;
    private int antalSkott = 0;
    
    public Spelare(String namnArg, int xKordArg, int yKordArg, int pengarArg, int lag) throws IOException {
        this.namn = namnArg;
        this.xKord = xKordArg;
        this.yKord = yKordArg;
        this.pengar = pengarArg;
        this.lag = lag;
        
        if(lag == 0)
        {
            gubbVektor.add(new Ko(9, 8, this));
            antalGubbar = antalGubbar + 1;
            gubbVektor.add(new Råtta(1, 1, this));
            antalGubbar = antalGubbar + 1;
            gubbVektor.add(new Råtta(1, 5, this));
            antalGubbar = antalGubbar + 1;
            gubbVektor.add(new Råtta(1, 7, this));
            antalGubbar = antalGubbar + 1;

            byggnadVektor.add(new Lada(5, 5, true, this));
            antalByggnader = antalByggnader + 1;
        }
        else
        {
            gubbVektor.add(new Ko(14, 14, this));
            antalGubbar = antalGubbar + 1;
            
            byggnadVektor.add(new Lada(10, 10, true, this));
            antalByggnader = antalByggnader + 1;
            
            byggnadVektor.add(new Piltorn(2, 10, true, this));
            antalByggnader = antalByggnader + 1;
        }
    }
    
    int getAntalGubbar(){
        return antalGubbar;
    }
    
    int getxKord(){
        return xKord;
    }
    
    int getyKord(){
        return yKord;
    }
    
    void setxKord(int x){
        xKord = x;
    }
    
    void setyKord(int y){
        yKord = y;
    }
    
    boolean finnsPlats(){
        return (antalGubbar < AFStrategi.MAXGUBBAR); 
    }           
    
    Gubbe getGubbe(int nr){
        return gubbVektor.get(nr);
    }
    
    int getPengar()
    {
        return pengar;
    }
    
    public int getTrä()
    {
        return trä;
    }
    
    public int getMat()
    {
        return mat;
    }
    
    public int getGödsel()
    {
        return gödsel;
    }
    
    int getAntalByggnader()
    {
        return antalByggnader;
    }
    
    Byggnad getByggnad(int inArg)
    {
        return byggnadVektor.get(inArg);
    }
    
    public void addGubbe(Gubbe enhet)
    {
        if((enhet.getPris() <= pengar) && (antalGubbar < AFStrategi.MAXGUBBAR))
        {
            //gubbVektor[antalGubbar] = enhet;
            gubbVektor.add(enhet);
            antalGubbar = antalGubbar + 1;
            pengar = pengar - enhet.getPris();
        }
    }
    
    public void addByggnad(Byggnad enhet)
    {
        //byggnadVektor[antalByggnader] = enhet;
        byggnadVektor.add(enhet);
        antalByggnader = antalByggnader + 1;
        pengar = pengar - enhet.getPris();
    }
    
    public void addVald(SpelarEnhet enhet)
    {
        valdaVektor.add(enhet);
    }
    
    public void resetAntalValda()
    {
        for(int enhetIndex = 0; enhetIndex < antalGubbar; enhetIndex++)
        {
            getGubbe(enhetIndex).vald = false;
        }
        valdaVektor.clear();
    }
    
    public SpelarEnhet getVald(int index)
    {
        return valdaVektor.get(index);
    }
    
    public int getAntalValda()
    {
        return valdaVektor.size();
    }
    
    public boolean getByggerByggnad()
    {
        return byggerByggnad;
    }
    
    public Byggnad getByggByggnad()
    {
        return byggByggnad;
    }
    
    public void setByggnad(Byggnad byggnadArg)
    {
        byggerByggnad = true;
        byggByggnad = byggnadArg;        
    }
    
    public void resetByggnad()
    {
        byggerByggnad = false;
        byggByggnad = null;        
    }
    
    public int getLag()
    {
        return lag;
    }
    
    public void addSkott(Skott skott)
    {
        skottVektor.add(skott);
        //skottVektor[antalSkott] = skott;
        antalSkott = antalSkott + 1;
    }
    
    public Skott getSkott(int index)
    {
        return skottVektor.get(index);
    }
    
    public void taBortSkott(Skott skott)
    {
        antalSkott = antalSkott - 1;
        skottVektor.remove(skott);
    }
    
    public void taBortGubbe(SpelarEnhet gubbe)
    {
        antalGubbar = antalGubbar - 1;
        gubbVektor.remove(gubbe);
    }
    
    public void taBortByggnad(SpelarEnhet byggnad)
    {
        antalByggnader = antalByggnader - 1;
        byggnadVektor.remove(byggnad);
    }
    
    public int getAntalSkott()
    {
        return antalSkott;
    }
    
    public void taBortSkott(SpelarEnhet skott)
    {
        antalSkott = antalSkott - 1;
        skottVektor.remove(skott);
    }
    
    public void läggTillResurs(int mängd, String resursTyp)
    {
        switch (resursTyp)
        {
            case "Trä": trä = trä + mängd;
                break;
            case "Pengar": pengar = pengar + mängd;
                break;
            case "Mat": mat = mat + mängd;
                break;
            case "Gödsel": gödsel = gödsel + mängd;
                break;
        }
    }
    
    public void hittaArbetslösGubbe()
    {
        for(int gubbIndex = 0; gubbIndex < antalGubbar; gubbIndex++)
        {
            SpelarEnhet tempSpelarEnhet = getGubbe(gubbIndex);
            if("Råtta".equals(tempSpelarEnhet.getNamn()) &&
                    (tempSpelarEnhet.getArbetsEnhet() == null) &&
                    (tempSpelarEnhet.getArbetsByggnad() == null))
            {
                resetAntalValda();
                addVald(tempSpelarEnhet);
                tempSpelarEnhet.vald = true;
                break;
            }
        }
    }
    
    public void hittaValdGubbe(Bana banan)
    {
        if(valdaVektor.size() > 0)
        {
            int tempX = getVald(0).xKord - Math.round(Grafik.antalCellerX/2);
            int tempY = getVald(0).yKord - Math.round(Grafik.antalCellerY/2);
            if(tempX < 0)
            {
                tempX = 0;
            }    
            else if(tempX > banan.getXLängd() - 
                    Grafik.antalCellerX)
            {
                tempX = banan.getXLängd() - Grafik.antalCellerX;
            }
            if(tempY < 0)
            {
                tempY = 0;
            }    
            else if(tempY > banan.getYLängd() - 
                    Grafik.antalCellerY)
            {
                tempY = banan.getYLängd() - Grafik.antalCellerY;
            }
            xKord = tempX;
            yKord = tempY;
        }
    }
}