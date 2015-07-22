package afstrategi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

public class AFStrategi extends JPanel implements MouseListener{
    
    final int startX = 0;
    final int startY = 0;
    final int startSektion = 0;
    Spelare spelaren;
    Spelare motståndaren;
    Bana banan;
    Grafik Grafik;
    int koX;
    int koY;
    int[][] närhet;
    SpelarEnhet[][] metaNärhet;
    NaturEnhet[][] naturNärhet;
    int spelTillstånd; //bestämmer vad skärmen ska visa. 0 kan vara startskärm, 1 är världskartan, 2 är inventarieskärm, 3 kan vara stridsskärm.
    private boolean musTryckt;
    private static Point JFramePosition;
    private Spelare[] spelarVektor;
    private int antalSpelare = 2;
    private Natur natur;
    
    public int MAXVALDAGUBBAR = 10;
    public static int MAXGUBBAR = 20;
    public static int MAXBYGGNADER = 10;
    public static int MAXSKOTT = 30;
    public static int SLEEPTIME = 20;

    public AFStrategi() throws IOException{
        try{
            Grafik = new Grafik();   
        }
        catch (IOException e){}
        addMouseListener(this);
        setPreferredSize(getPreferredSize());
        spelarVektor = new Spelare[2];
        spelaren = new Spelare("spelare", startX, startY, 1000, 0);
        spelarVektor[0] = spelaren;
        motståndaren = new Spelare("motståndaren", startX, startY, 1000, 1);
        spelarVektor[1] = motståndaren;
        natur = new Natur();
        initiera();
        banan.uppdateraMetaBana(spelaren);
        banan.uppdateraMetaBana(motståndaren);
        spelTillstånd = 1;
        närhet = uppdateraNärhet();
        metaNärhet = uppdateraMetaNärhet();
        naturNärhet = uppdateraNaturNärhet();
        Thread process = new MinProcess();
        process.start();
    }

    public static void main(String[] args) throws IOException {
        JFrame fönster = new JFrame("AnimalFarm");
        JPanel spel = new AFStrategi();
        fönster.add(spel);
        fönster.pack();
        fönster.setLocationRelativeTo(null);
        fönster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fönster.setVisible(true);
        JFramePosition = fönster.getLocation();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Grafik.getBredd(), Grafik.getHöjdPanel());
    }

    private int[][] uppdateraNärhet() {
        int[][] m = new int[Grafik.getAntalCellerX()][Grafik.getAntalCellerY()];
        int lokalKordX = spelaren.getxKord();
        int lokalKordY = spelaren.getyKord();
        for (int i = 0; i < Grafik.getAntalCellerX(); i++) {
            for (int j = 0; j < Grafik.getAntalCellerY(); j++) {
                m[i][j] = banan.getBanMatris(i + lokalKordX, j + lokalKordY);
            }
        }
        return m;
    }
    
    private SpelarEnhet[][] uppdateraMetaNärhet()
    {
        SpelarEnhet[][] m = new SpelarEnhet[Grafik.getAntalCellerX()][Grafik.getAntalCellerY()];
        int lokalKordX = spelaren.getxKord();
        int lokalKordY = spelaren.getyKord();
        for (int i = 0; i < Grafik.getAntalCellerX(); i++) {
            for (int j = 0; j < Grafik.getAntalCellerY(); j++) {
                m[i][j] = banan.getMetaBanMatris(i + lokalKordX, j + lokalKordY);
            }
        }
        return m;
    }
    
    private NaturEnhet[][] uppdateraNaturNärhet()
    {
        NaturEnhet[][] m = new NaturEnhet[Grafik.getAntalCellerX()][Grafik.getAntalCellerY()];
        int lokalKordX = spelaren.getxKord();
        int lokalKordY = spelaren.getyKord();
        for (int i = 0; i < Grafik.getAntalCellerX(); i++) {
            for (int j = 0; j < Grafik.getAntalCellerY(); j++) {
                m[i][j] = banan.getNaturBanMatris(i + lokalKordX, j + lokalKordY);
            }
        }
        return m;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getY() < Grafik.getHöjd())
        {
            int xKord = e.getX()/Grafik.getStorlekPåRuta() + spelaren.getxKord();
            int yKord = e.getY()/Grafik.getStorlekPåRuta() + spelaren.getyKord();
            if((e.getButton() == 1) && spelaren.getByggerByggnad())
            {
                boolean rutaOk = spelaren.getByggByggnad().placeraByggnad(xKord, yKord,
                        spelaren, banan);
                if(rutaOk)
                {
                    spelaren.getVald(0).setMål(banan, xKord, yKord);
                }
            }
            else if(e.getButton() == 1)
            {
                spelaren.resetAntalValda();
                for (int gubbIndex = 0; gubbIndex < spelaren.getAntalGubbar(); gubbIndex++)
                {
                    if((xKord == spelaren.getGubbe(gubbIndex).getXKord()) &&
                            (yKord == spelaren.getGubbe(gubbIndex).getYKord()))
                    {
                        spelaren.getGubbe(gubbIndex).setVald(true);
                        spelaren.addVald(spelaren.getGubbe(gubbIndex));
                    }
                    else
                    {
                        spelaren.getGubbe(gubbIndex).setVald(false);
                    }
                }
                for (int byggnadIndex = 0; byggnadIndex < spelaren.getAntalByggnader(); byggnadIndex++)
                {
                    if((xKord >= spelaren.getByggnad(byggnadIndex).getXKord()) && 
                            (xKord <= (spelaren.getByggnad(byggnadIndex).getXKord() + 
                            spelaren.getByggnad(byggnadIndex).getStorlekX()) - 1) &&
                            (yKord >= spelaren.getByggnad(byggnadIndex).getYKord() &&
                            (yKord <= spelaren.getByggnad(byggnadIndex).getYKord() + 
                            spelaren.getByggnad(byggnadIndex).getStorlekY() - 1)))
                    {
                        spelaren.getByggnad(byggnadIndex).setVald(true);
                        spelaren.addVald(spelaren.getByggnad(byggnadIndex));
                    }
                    else
                    {
                        spelaren.getByggnad(byggnadIndex).setVald(false);
                    }
                }
            }
            else if(e.getButton() == 3)
            {
                for (int gubbIndex = 0; gubbIndex < spelaren.getAntalValda(); gubbIndex++)
                {
                    spelaren.getVald(gubbIndex).setMål(banan, xKord, yKord);
                    Grafik.setMålPosPar(xKord - spelaren.getxKord(), 
                        yKord - spelaren.getyKord());
                }
            }   
        }
        else
        {
        if(spelaren.getAntalValda() == 1)
            if((!spelaren.getVald(0).getÄrByggnad())
                || (spelaren.getVald(0).getFärdigbyggd()))
            {
                for(int panelIndex = 0; panelIndex < spelaren.getVald(0).antalKnappar; panelIndex++)
                    {
                    if((e.getX() > (Grafik.getFörstaKnappenPosX() + panelIndex*60)) &&
                            (e.getX() < (Grafik.getFörstaKnappenPosX() + Grafik.getStorlekPåRuta() + panelIndex*60)) &&
                            (e.getY() > Grafik.getFörstaKnappenPosY()) &&
                            (e.getY() < (Grafik.getFörstaKnappenPosY() + Grafik.getStorlekPåRuta())))
                    {
                        ProduktionsObjekt tempObjekt = spelaren.getVald(0).getKnapp(panelIndex).TryckHändelse(spelaren);
                        if(tempObjekt.getÄrDummy())
                        {
                            //Om en byggnad ska byggas hamnar man här
                            spelaren.getVald(0).getKnapp(panelIndex).TryckHändelse(spelaren, 10, 10);
                            if(tempObjekt.getÄrDummy())
                            {
                                //spelaren.addByggnad(tempObjekt);
                            }
                        }
                        else
                        {
                            spelaren.getVald(0).addProduktionsVektor(tempObjekt);
                        }
                    }
                }
            }
            if((e.getX() < (Grafik.getBredd() - Grafik.getAvståndKarta())) && 
                    (e.getX() > (Grafik.getBredd() - Grafik.getAvståndKarta() - 
                    banan.getXLängd()*Grafik.getStorlekPåKarta())) &&
                    (e.getY() < (Grafik.getHöjdPanel() - Grafik.getAvståndKarta())) && 
                    (e.getY() > (Grafik.getHöjdPanel() - Grafik.getAvståndKarta()) - 
                    banan.getYLängd()*Grafik.getStorlekPåKarta()))
            {
                int tempX = (e.getX() - (Grafik.getBredd() - Grafik.getAvståndKarta() - 
                        banan.getXLängd()*Grafik.getStorlekPåKarta()))/Grafik.getStorlekPåKarta();
                int tempY = (e.getY() - (Grafik.getHöjdPanel() - Grafik.getAvståndKarta() - 
                        banan.getYLängd()*Grafik.getStorlekPåKarta()))/Grafik.getStorlekPåKarta();
                if(tempX > (banan.getXLängd() - Grafik.getAntalCellerX()))
                {
                    tempX = banan.getXLängd() - Grafik.getAntalCellerX();
                }
                if(tempY > (banan.getYLängd() - Grafik.getAntalCellerY()))
                {
                    tempY = banan.getYLängd() - Grafik.getAntalCellerY();
                }
                spelaren.setxKord(tempX);
                spelaren.setyKord(tempY);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getY() < Grafik.getHöjd() && (e.getButton() == 1) &&
                (!spelaren.getByggerByggnad()))
        {
            spelaren.resetAntalValda();
            int xKord = e.getX()/Grafik.getStorlekPåRuta();
            int yKord = e.getY()/Grafik.getStorlekPåRuta();
            //System.out.println("Mouse Pressed at X: " + xKord + " - Y: " + yKord);
            if(yKord > Grafik.getAntalCellerY())
            {
                yKord = Grafik.getAntalCellerY();
            }
            setMusTryckt(true, xKord + spelaren.getxKord(), yKord  + spelaren.getyKord());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if((e.getY() < Grafik.getHöjd()) && (!spelaren.getByggerByggnad()))
            {
            int xKord = (int) (Math.floor(e.getX()/Grafik.getStorlekPåRuta()) + spelaren.getxKord());
            int yKord = (int) (Math.floor(e.getY()/Grafik.getStorlekPåRuta()) + spelaren.getyKord());
            //System.out.println("Mouse Released at X: " + xKord + " - Y: " + yKord);
            if(e.getButton() == 1)
            {
                int minstXPos;
                int minstYPos;
                int störstXPos;
                int störstYPos;
                if (xKord < Grafik.getMusTrycktPosPar().getX())
                {
                    minstXPos = xKord;
                    störstXPos = Grafik.getMusTrycktPosPar().getX();
                }
                else
                {
                    störstXPos = xKord;
                    minstXPos = Grafik.getMusTrycktPosPar().getX();
                }
                if (yKord < Grafik.getMusTrycktPosPar().getY())
                {
                    minstYPos = yKord;
                    störstYPos = Grafik.getMusTrycktPosPar().getY();
                }
                else
                {
                    störstYPos = yKord;
                    minstYPos = Grafik.getMusTrycktPosPar().getY();
                }
                int valdaGubbar = 0;
                for (int gubbIndex = 0; gubbIndex < spelaren.getAntalGubbar(); gubbIndex++)
                {
                    Gubbe tempGubbe = spelaren.getGubbe(gubbIndex);
                    if((tempGubbe.getXKord() >= minstXPos) &&
                            (tempGubbe.getXKord() <= störstXPos) &&
                            (tempGubbe.getYKord() >= minstYPos) &&
                            (tempGubbe.getYKord() <= störstYPos) &&
                            valdaGubbar < MAXVALDAGUBBAR)
                    {
                        spelaren.getGubbe(gubbIndex).setVald(true);
                        valdaGubbar = valdaGubbar + 1;
                        spelaren.addVald(spelaren.getGubbe(gubbIndex));
                    }
                    else
                    {
                        spelaren.getGubbe(gubbIndex).setVald(false);
                    }
                }
                for (int byggnadIndex = 0; byggnadIndex < spelaren.getAntalByggnader(); byggnadIndex++)
                {
                    Byggnad tempByggnad = spelaren.getByggnad(byggnadIndex);
                    if((tempByggnad.getXKord() >= minstXPos) &&
                            (tempByggnad.getXKord() <= störstXPos) &&
                            (tempByggnad.getYKord() >= minstYPos) &&
                            (tempByggnad.getYKord() <= störstYPos) && 
                            (valdaGubbar < 1))
                    {
                        spelaren.getByggnad(byggnadIndex).setVald(true);
                        spelaren.addVald(spelaren.getByggnad(byggnadIndex));
                        break;
                    }
                    else
                    {
                        spelaren.getByggnad(byggnadIndex).setVald(false);
                    }
                }
                setMusTryckt(false, xKord, yKord);
                //System.out.println("enGubbeVald: " + enGubbeVald);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println("Mouse Entered at X: " + e.getX() + " - Y: " + e.getY());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //System.out.println("Mouse Exited at X: " + e.getX() + " - Y: " + e.getY());
    }
    
    private void uppdateraEnheter()
    {
        for(int spelarIndex = 0; spelarIndex < antalSpelare; spelarIndex++)
        {
            for (int gubbIndex = 0; gubbIndex < spelarVektor[spelarIndex].getAntalGubbar(); gubbIndex++)
            {
                spelarVektor[spelarIndex].getGubbe(gubbIndex).FlyttaGubbe(banan, spelarVektor[spelarIndex]);
                spelarVektor[spelarIndex].getGubbe(gubbIndex).action(natur, banan);
            }
            for (int ByggnadIndex = 0; ByggnadIndex < spelarVektor[spelarIndex].getAntalByggnader(); ByggnadIndex++)
            {
                spelarVektor[spelarIndex].getByggnad(ByggnadIndex).producera(spelarVektor[spelarIndex]);
                try {
                    spelarVektor[spelarIndex].getByggnad(ByggnadIndex).action(spelarVektor[spelarIndex], banan);
                } catch (IOException ex) {
                    Logger.getLogger(AFStrategi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for (int SkottIndex = 0; SkottIndex < spelarVektor[spelarIndex].getAntalSkott(); SkottIndex++)
            {
                spelarVektor[spelarIndex].getSkott(SkottIndex).FlyttaGubbe(banan, spelarVektor[spelarIndex]);
            }
        }
        for(int naturIndex = 0; naturIndex < natur.getAntalObjekt(); naturIndex++)
        {
            natur.getEnhet(naturIndex).plantera(natur, banan);
        }
    }

    private class MinProcess extends Thread {

        @Override
        public void run() {
            while (true) {
                repaint();
                uppdateraEnheter();
                try {
                    Thread.sleep(SLEEPTIME);
                } catch (InterruptedException ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        banan.nollställMetaMatris();
        for(int spelarIndex = 0; spelarIndex < antalSpelare; spelarIndex++)
        {
            banan.uppdateraMetaBana(spelarVektor[spelarIndex]);
            banan.uppdateraNaturBana(natur);
        }
        närhet = uppdateraNärhet();
        metaNärhet = uppdateraMetaNärhet();
        naturNärhet = uppdateraNaturNärhet();
        Grafik.målaNärhet(g, närhet, metaNärhet, spelaren);
        Grafik.målaNaturNärhet(g, naturNärhet, spelaren);
        Grafik.målaPanelen(g, spelaren);
        Grafik.målaKarta(g, banan, spelaren);
        //Hämta nuvarande musposition
        Point musPos = MouseInfo.getPointerInfo().getLocation();
        int tempPosY = (int) musPos.getY();
        if(musTryckt)
        {
            int xKord = (int) ((musPos.getX() - JFramePosition.getX())/Grafik.getStorlekPåRuta()
                + spelaren.getxKord());
            if(tempPosY > (Grafik.höjd))
            {
                tempPosY = Grafik.höjd;
            }
            int yKord = (int) ((tempPosY - JFramePosition.getY())/Grafik.getStorlekPåRuta()
                    + spelaren.getyKord()); 
            Grafik.målaMusTryckt(xKord, yKord, g, spelaren);
        }
        if(spelaren.getByggerByggnad())
        {
            if(tempPosY > Grafik.höjd - spelaren.getByggByggnad().getBild().getHeight())
            {
                tempPosY = Grafik.höjd - spelaren.getByggByggnad().getBild().getHeight();
            }
            int xKord = (int) ((musPos.getX() - JFramePosition.getX())/Grafik.getStorlekPåRuta()
                + spelaren.getxKord());
            int yKord = (int) ((tempPosY - JFramePosition.getY())/Grafik.getStorlekPåRuta()
                + spelaren.getyKord()); 
            BufferedImage rutBild = spelaren.getByggByggnad().getBild();
            Grafik.målaByggByggnad(xKord, yKord, g, spelaren, rutBild);
        }
        if(Grafik.målaMålBoolean)
        {
            Grafik.målaMål(Grafik.getMålPosPar().getX(), Grafik.getMålPosPar().getY(), g);
            Grafik.målaMålBoolean = false; 
        }

        g.setColor(Color.white);
        g.drawString(banan.getBanMatris(spelaren.getGubbe(0).getXKord(),spelaren.getGubbe(0).getYKord()) + "", 20,20);
        g.drawString(spelaren.getxKord() + "", 40,20);
        g.drawString(spelaren.getyKord() + "", 60,20);
//        g.drawString(antalCellerY + "", 80,20);
//        g.drawString(spelaren.getAntalGubbar() + "", 100,20);
        g.drawString(spelaren.getGubbe(0).getXKord() + "", 130,20);
        g.drawString(spelaren.getGubbe(0).getYKord() + "", 160,20);
    }

    private void initieraTangenter() {            
        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "Flytta_västerut");
        this.getActionMap().put("Flytta_västerut", new Händelse("VÄSTERUT_FLYTT"));

        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "Flytta_österut");
        this.getActionMap().put("Flytta_österut", new Händelse("ÖSTERUT_FLYTT"));

        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "Flytta_norrut");
        this.getActionMap().put("Flytta_norrut", new Händelse("NORRUT_FLYTT"));

        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "Flytta_söderut");
        this.getActionMap().put("Flytta_söderut", new Händelse("SÖDERUT_FLYTT"));
        
        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "Hitta_arbetslös_gubbe");
        this.getActionMap().put("Hitta_arbetslös_gubbe", new Händelse("HITTA_ARBETSLÖS_GUBBE"));
        
        this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "Hitta_vald_gubbe");
        this.getActionMap().put("Hitta_vald_gubbe", new Händelse("HITTA_VALD_GUBBE"));
    }

    class Händelse extends AbstractAction {

        public Händelse(String keyStrokeName) {
            putValue(Action.NAME, keyStrokeName);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getValue(Action.NAME).equals("VÄSTERUT_FLYTT")) {
                spelTillstånd = 1;
                testaSikt(-1, 0);
            }
            if (getValue(Action.NAME).equals("ÖSTERUT_FLYTT")) {
                spelTillstånd = 1;
                testaSikt(1, 0);
            }
            if (getValue(Action.NAME).equals("NORRUT_FLYTT")) {
                spelTillstånd = 1;
                testaSikt(0, -1);
            }
            if (getValue(Action.NAME).equals("SÖDERUT_FLYTT")) {
                spelTillstånd = 1;
                testaSikt(0, 1);
            }
            if (getValue(Action.NAME).equals("HITTA_ARBETSLÖS_GUBBE")) {
                spelaren.hittaArbetslösGubbe();
            }
            if (getValue(Action.NAME).equals("HITTA_VALD_GUBBE")) {
                spelaren.hittaValdGubbe(banan);
            }
        }

        private void testaSikt(int x, int y) {
            if((spelaren.getxKord() + x >= 0) &&
                (spelaren.getxKord() + x <= (banan.getXLängd()) - Grafik.getAntalCellerX())
                && (spelaren.getyKord() + y >= 0) &&
                (spelaren.getyKord() + y <= (banan.getYLängd() - Grafik.getAntalCellerY()))){
                spelaren.setxKord(spelaren.getxKord() + x);
                spelaren.setyKord(spelaren.getyKord() + y);
                närhet = uppdateraNärhet();
                metaNärhet = uppdateraMetaNärhet();
                repaint();
            }
        }
    }
    
    public void setMusTryckt(boolean inArg, int xArg, int yArg)
    {
        musTryckt = inArg;
        Grafik.setMusTrycktPosPar(xArg, yArg);
    }

    private void initiera() {
        initieraTangenter();
        banan = new Bana();
        try {
        Grafik = new Grafik();
        }
        catch (IOException e){}
    }
}