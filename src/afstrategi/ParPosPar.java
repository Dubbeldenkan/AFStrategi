package afstrategi;

public class ParPosPar implements Comparable
{
    private PosPar pos;
    private PosPar förälder;
    private int gissningMål;
    private int sträcka;
    
    public ParPosPar(PosPar pos, PosPar förälder, PosPar mål, int sträcka)
    {
        this.pos = pos;
        this.förälder = förälder;
        this.gissningMål = Math.max(Math.abs(pos.getX() - mål.getX()), 
                Math.abs(pos.getY() - mål.getY()));
        
        this.sträcka = sträcka + 1;
    }
    
    public PosPar getPos()
    {
        return pos;
    }
    
    public PosPar getFörälder()
    {
        return förälder;
    }
    
    public int getFVärde(){
        return gissningMål;
    }
    
    public int getSträcka(){
        return sträcka;
    }
    
    
    @Override
    public int compareTo(Object jämförParPosPar) {
        int jämförelseFVärde = ((ParPosPar)jämförParPosPar).getFVärde();
        /* For Ascending order*/
        return this.gissningMål-jämförelseFVärde;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }    
}
