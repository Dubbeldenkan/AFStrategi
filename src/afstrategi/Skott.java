package afstrategi;

import static afstrategi.SpelarEnhet.Grafik;
import java.io.IOException;
import java.util.Random;

public class Skott extends Gubbe{
    
    private Byggnad byggnad;
    private static int startLiv = 1;
    
    public Skott(int xKordArg, int yKordArg, Spelare spelaren, int xMål, int yMål, 
            Bana banan, int attackVärdeArg, Byggnad byggnadArg) throws IOException{
        super(xKordArg, yKordArg, spelaren, startLiv);
        this.bild = Grafik.skottBild;
        this.aktivBild = bild;
        this.väntaFörFlytt = 2;
        setMål(banan, xMål, yMål);
        this.ärSkott = true;
        this.attackVärde = attackVärdeArg;
        this.byggnad = byggnadArg;

        this.namn = "Skott";
    }
    
    @Override
    public void FlyttaGubbe(Bana banan, Spelare spelaren)
    {
        if(((!vägVektor.isEmpty()))
                && (tidSedanFlytt > väntaFörFlytt))
        {
            int tempXRikt = vägVektor.get(0).getX() - xKord;
            int tempYRikt = vägVektor.get(0).getY() - yKord;
            if(!banan.getMetaBanMatris(xKord + tempXRikt, yKord + tempYRikt).getÄrDummy() &&
                    (banan.getMetaBanMatris(xKord + tempXRikt, yKord + tempYRikt).getLag() != getLag()))
            {
                banan.getMetaBanMatris(xKord + tempXRikt, yKord + tempYRikt).taSkada(attackVärde, byggnad);
                spelaren.taBortSkott(this);
            }
            addXKord(tempXRikt);
            addYKord(tempYRikt);
            transformeraBild();
            vägVektor.remove(0);
            setVäntaFörFlyttNoll();
            //banan.uppdateraMetaBana(spelaren);
        }
        else if(vägVektor.isEmpty())
        {
            spelaren.taBortSkott(this);
        }
        else
        {
            addTidSedanFlytt();
        }
    }
    
    @Override
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
        int xYSkillnad = (Math.abs(xKordMål - xArg) -
                Math.abs(yArg - yKordMål));
        if((xYSkillnad == 0) && 
                (randomGenerator.nextInt(2) == 1))
        {
            posPar = new PosPar(xArg + xFlytta, yArg);
        }
        else if(xYSkillnad <= 0)
        {
            posPar = new PosPar(xArg, yArg + yFlytta);
        }
        else
        {
            posPar = new PosPar(xArg + xFlytta, yArg);
        }
        return posPar;
    }
}
