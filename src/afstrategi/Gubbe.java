package afstrategi;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Gubbe extends SpelarEnhet{
    
    protected int xKordMål;
    protected int yKordMål;
    protected int tidSedanFlytt;
    protected int väntaFörFlytt;
    protected ArrayList <PosPar> vägVektor = new ArrayList<PosPar>();
    private int riktningX = 1;
    private int riktningY = 1;
    protected BufferedImage aktivBild;
    protected boolean ärSkott = false;
    protected int maxBärKapacitet;
    
    public Gubbe(int xKordArg, int yKordArg, Spelare spelaren, int liv){
        super(xKordArg, yKordArg, false, spelaren, liv);
        this.xKordMål = xKordArg;
        this.yKordMål = yKordArg;
                
        tidSedanFlytt = 100;
    }   
    
    @Override
    public void setMål(Bana Banan, int xMål, int yMål)
    {
        xKordMål = xMål;
        yKordMål = yMål;
        hittaVäg(Banan);
    }
    
    public int getXMål()
    {
        return xKordMål;
    }
    
    public int getYMål()
    {
        return yKordMål;
    }
    
    @Override
    public BufferedImage getBild()
    {
        return aktivBild;
    }
    
    public int getTidSedanFlytt()
    {
        return tidSedanFlytt;
    }
    
    public void addTidSedanFlytt()
    {
        tidSedanFlytt = tidSedanFlytt + 1;
    }
    
    public int getVäntaFörFlytt()
    {
        return väntaFörFlytt;
    }
    
    public void setVäntaFörFlyttNoll()
    {
        tidSedanFlytt = 0;
    }
    
    public void addYKord(int yKordArg)
    {
        yKord = yKord + yKordArg;
        if(yKordArg == 1)
        {
            riktningY = 1;
        }
        else if(yKordArg == -1)
        {
            riktningY = -1;
        }
    }
    
    public void addXKord(int xKordArg)
    {
        xKord = xKord + xKordArg;
        if(xKordArg == 1)
        {
            riktningX = 1;
            riktningY = 0;
        }
        else if(xKordArg == -1)
        {
            riktningX = -1;
            riktningY = 0;
        }
    }
    
    protected void transformeraBild()
    {
        AffineTransform tx = AffineTransform.getScaleInstance(riktningX, 1);
        tx.translate(Math.min(riktningX, 0)*bild.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        aktivBild = op.filter(bild, null);
        
        if(riktningY != 0)
        {
            AffineTransform at = new AffineTransform();
            at.translate(aktivBild.getWidth() / 2, aktivBild.getHeight() / 2);
            if(((riktningY == 1) && (riktningX == 1)) ||
                    ((riktningY == -1) && (riktningX == -1)))
            {
                if(getÄrSkott())
                {
                    at.rotate(Math.PI*1/2);
                }
                else
                {
                    at.rotate(Math.PI*1/8);
                }
            }
            else if(((riktningY == -1) && (riktningX == 1)) ||
                    ((riktningY == 1) && (riktningX == -1)))
            {
                if(getÄrSkott())
                {
                    at.rotate(Math.PI*3/2);
                }
                else
                {
                    at.rotate(Math.PI*15/8);
                }
            }
            at.scale(0.9, 0.9);
            at.translate(-aktivBild.getWidth()/2, -aktivBild.getHeight()/2);
              
            AffineTransformOp opR = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            aktivBild = opR.filter(aktivBild, null);
        }
    }
    
    public void FlyttaGubbe(Bana Banan, Spelare spelaren)
    {
        if(((!vägVektor.isEmpty()))
                && (tidSedanFlytt > väntaFörFlytt))
        {
            int tempXRikt = vägVektor.get(0).getX() - xKord;
            int tempYRikt = vägVektor.get(0).getY() - yKord;
            if(checkaOmRutaÄrOk(Banan, new PosPar(xKord + tempXRikt, yKord + tempYRikt), false))
            {
                addXKord(tempXRikt);
                addYKord(tempYRikt);
                transformeraBild();
                vägVektor.remove(0);
                setVäntaFörFlyttNoll();
                Banan.uppdateraMetaBana(spelaren);
            }
            else if(!checkaOmRutaÄrOk(Banan, new PosPar(xKordMål, yKordMål), false))
            {
                hittaVäg(Banan);
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
    
//    public PosPar hittaNästaRuta(Bana Banan, PosPar posPar)
//    {
//        int xArg = posPar.getX();
//        int yArg = posPar.getY();
//        int xFlytta = 0;
//        if (xKordMål - xArg != 0)
//        {
//            xFlytta = ((xKordMål - xArg)/
//                            (Math.abs(xArg - xKordMål)));
//        }
//        int yFlytta = 0;
//        if ((yKordMål - yArg) != 0)
//        {
//            yFlytta = ((yKordMål - yArg)/
//                            (Math.abs(yArg - yKordMål)));
//        }
//        Random randomGenerator = new Random();
//        boolean yFlyttaOk = checkaOmRutaÄrOk(Banan, posPar, yFlytta, true, true);
//        boolean xFlyttaOk = checkaOmRutaÄrOk(Banan, posPar, xFlytta, false, true);
//        int xYSkillnad = (Math.abs(xKordMål - xArg) -
//                Math.abs(yArg - yKordMål));
//        if((xYSkillnad == 0) && 
//                (randomGenerator.nextInt(2) == 1) &&
//                yFlyttaOk && xFlyttaOk)
//        {
//            posPar = new PosPar(xArg + xFlytta, yArg);
//        }
//        else if(((xYSkillnad <= 0) || !xFlyttaOk) && yFlyttaOk)
//        {
//            posPar = new PosPar(xArg, yArg + yFlytta);
//        }
//        else if(xFlyttaOk)
//        {
//            posPar = new PosPar(xArg + xFlytta, yArg);
//        }
//        //Om XKord är rätt men YKord inte kan flyttas på
//        else if((!yFlyttaOk) && (xArg == xKordMål))
//        {
//            if(checkaOmRutaÄrOk(Banan, posPar, 1, false, true) &&
//                    checkaOmRutaÄrOk(Banan, posPar, -1, false, true))
//            {
//                int nyXFlytta = 1 - randomGenerator.nextInt(2)*2;
//                posPar = new PosPar(xArg + nyXFlytta, yArg);
//            }
//            else if(checkaOmRutaÄrOk(Banan, posPar, 1, false, true))
//            {
//                posPar = new PosPar(xArg + 1, yArg);
//            }
//            else if(checkaOmRutaÄrOk(Banan, posPar, -1, false, true))
//            {
//                posPar = new PosPar(xArg - 1, yArg);
//            }
//        }
//        else if((!xFlyttaOk) && (yArg == yKordMål))
//        {
//            if(checkaOmRutaÄrOk(Banan, posPar, 1, true, true) &&
//                    checkaOmRutaÄrOk(Banan, posPar, -1, true, true))
//            {
//                int nyYFlytta = 1 - randomGenerator.nextInt(2)*2;
//                posPar = new PosPar(xArg, yArg + nyYFlytta);
//            }
//            else if(checkaOmRutaÄrOk(Banan, posPar, 1, false,true))
//            {
//                posPar = new PosPar(xArg, yArg  + 1);
//            }
//            else if(checkaOmRutaÄrOk(Banan, posPar, -1, false, true))
//            {
//                posPar = new PosPar(xArg, yArg  - 1);
//            }
//        }
//        return posPar;
//    }
    
    public PosPar hittaNästaRuta(Bana Banan, PosPar posPar)
    {
        int xArg = posPar.getX();
        int yArg = posPar.getY();
        int xFlytta = 0;
        if (xKordMål - xArg != 0)
        {
            xFlytta = ((xKordMål - xArg)/
                            (Math.abs(xArg - xKordMål)));
        }
        int yFlytta = 0;
        if ((yKordMål - yArg) != 0)
        {
            yFlytta = ((yKordMål - yArg)/
                            (Math.abs(yArg - yKordMål)));
        }
        Random randomGenerator = new Random();
        boolean yFlyttaOk = checkaOmRutaÄrOk(Banan, 
                new PosPar(posPar.getX(), posPar.getY() + yFlytta), true);
        boolean xFlyttaOk = checkaOmRutaÄrOk(Banan, 
                new PosPar(posPar.getX() + xFlytta, posPar.getY()), true);
        int xYSkillnad = (Math.abs(xKordMål - xArg) -
                Math.abs(yArg - yKordMål));
        if((xYSkillnad == 0) && 
                (randomGenerator.nextInt(2) == 1) &&
                yFlyttaOk && xFlyttaOk)
        {
            posPar = new PosPar(xArg + xFlytta, yArg);
        }
        else if(((xYSkillnad <= 0) || !xFlyttaOk) && yFlyttaOk)
        {
            posPar = new PosPar(xArg, yArg + yFlytta);
        }
        else if(xFlyttaOk)
        {
            posPar = new PosPar(xArg + xFlytta, yArg);
        }
        //Om XKord är rätt men YKord inte kan flyttas på
        else if((!yFlyttaOk) && (xArg == xKordMål))
        {
            if(checkaOmRutaÄrOk(Banan, new PosPar(posPar.getX() + 1, posPar.getY()), true) &&
                    checkaOmRutaÄrOk(Banan, new PosPar(posPar.getX() - 1, posPar.getY()), true))
            {
                int nyXFlytta = 1 - randomGenerator.nextInt(2)*2;
                posPar = new PosPar(xArg + nyXFlytta, yArg);
            }
            else if(checkaOmRutaÄrOk(Banan, new PosPar(posPar.getX() + 1, posPar.getY()), true))
            {
                posPar = new PosPar(xArg + 1, yArg);
            }
            else if(checkaOmRutaÄrOk(Banan, new PosPar(posPar.getX() - 1, posPar.getY()), true))
            {
                posPar = new PosPar(xArg - 1, yArg);
            }
        }
        else if((!xFlyttaOk) && (yArg == yKordMål))
        {
            if(checkaOmRutaÄrOk(Banan, new PosPar(posPar.getX(), posPar.getY() + 1), true) &&
                    checkaOmRutaÄrOk(Banan, new PosPar(posPar.getX(), posPar.getY() - 1), true))
            {
                int nyYFlytta = 1 - randomGenerator.nextInt(2)*2;
                posPar = new PosPar(xArg, yArg + nyYFlytta);
            }
            else if(checkaOmRutaÄrOk(Banan, new PosPar(posPar.getX(), posPar.getY() + 1), true))
            {
                posPar = new PosPar(xArg, yArg  + 1);
            }
            else if(checkaOmRutaÄrOk(Banan, new PosPar(posPar.getX(), posPar.getY() - 1), true))
            {
                posPar = new PosPar(xArg, yArg  - 1);
            }
        }
        return posPar;
    }
    
    @Override
    public boolean checkaOmRutaÄrOk(Bana banan, PosPar posPar, boolean läggTillNy)
    {
        //läggTillNy används då man testar för att lägga till en ny ruta
        int xArg = posPar.getX();
        int yArg = posPar.getY();
        return ((yArg >= 0) &&
                (yArg <= banan.getYLängd()) &&
                (xArg >= 0) &&
                    (xArg <= banan.getXLängd()) &&
                (banan.getBanMatris(xArg,
                        yArg) == 0) &&
                (banan.getMetaBanMatris(xArg,
                        yArg).getÄrDummy()) && 
                ((banan.getNaturBanMatris(xArg, 
                        yArg).getÄrDummy()) ||
                (!banan.getNaturBanMatris(xArg, 
                        yArg).getÄrHinder())) &&
                (!läggTillNy ||
                !checkaOmRutanFinns(xArg, yArg)));
    }
    
    @Override
    public boolean checkaOmRutanFinns(int xArg, int yArg)
    {
        boolean Svar = false;
        for(int rutIndex = 0; rutIndex < vägVektor.size(); rutIndex++)
        {
            if((vägVektor.get(rutIndex).getX() == xArg) &&
                    (vägVektor.get(rutIndex).getY() == yArg))
            {
                Svar = true;
                break;
            }
        }
        return Svar;
    }
    
    public void hittaVäg(Bana banan)
    {
        vägVektor.clear();
        PosPar posPar;
        int senastXKord = getXKord();
        int senastYKord = getYKord();
        while(((senastXKord != xKordMål) ||
                (senastYKord != yKordMål)) && (vägVektor.size() < 1000))
        {
            posPar = hittaNästaRuta(banan, new PosPar(senastXKord, senastYKord));
            vägVektor.add(posPar);
            senastXKord = posPar.getX();
            senastYKord = posPar.getY();
        } 
    }
    
    @Override
    public boolean getÄrSkott()
    {
        return ärSkott;
    }
}
