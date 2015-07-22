
package afstrategi;

import java.util.ArrayList;

public class Natur {
    
    private ArrayList <NaturEnhet> naturVektor = new ArrayList<NaturEnhet>();
               
    public Natur()
    {
        naturVektor.add(new Träd(12, 12));
        naturVektor.add(new Träd(3, 3));
        naturVektor.add(new Guld(4, 4));
        naturVektor.add(new Guld(2, 7));
        naturVektor.add(new Guld(16, 15));
        naturVektor.add(new Äng(15, 15));
        naturVektor.add(new Äng(9, 6));
    }
    
    public int getAntalObjekt()
    {
        return naturVektor.size();
    }
    
    public NaturEnhet getEnhet(int index)
    {
        return naturVektor.get(index);
    }
    
    public void addEnhet(NaturEnhet enhet)
    {
        naturVektor.add(enhet);
    }
    
    public void taBortEnhet(NaturEnhet enhet, Bana banan, int xKord, int yKord)
    {
        banan.taBortNaturEnhet(xKord, yKord);
        naturVektor.remove(enhet);
    }
}
