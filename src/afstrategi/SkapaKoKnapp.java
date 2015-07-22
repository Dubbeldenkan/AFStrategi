package afstrategi;

public class SkapaKoKnapp extends Knapp{
    
    public SkapaKoKnapp(int xArg, int yArg)
    {
        super(xArg, yArg, Grafik.koPanel);
    }
    
    @Override
    public ProduktionsObjekt TryckHändelse(Spelare spelaren)
    {  
        if((spelaren.getAntalGubbar() < AFStrategi.MAXGUBBAR) &&
                (spelaren.getPengar() >= Ko.PRIS))
        {
            return new KoProduktion(xKord - 1, yKord - 1);
            //spelaren.addGubbe(new Ko(xKord - 1, yKord - 1));
        }
        else
        {
            return new DummyProduktion();
        }
    }
}
