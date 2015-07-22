package afstrategi;

public class SkapaRåttaKnapp extends Knapp{
    
    public SkapaRåttaKnapp(int xArg, int yArg)
    {
        super(xArg, yArg, Grafik.råtta);
    }
    
    @Override
    public ProduktionsObjekt TryckHändelse(Spelare spelaren)
    {  
        if((spelaren.getAntalGubbar() < AFStrategi.MAXGUBBAR) &&
                (spelaren.getPengar() >= Råtta.PRIS))
        {
            return new RåttaProduktion(xKord - 1, yKord - 1);
        }
        else
        {
            return new DummyProduktion();
        }
    }
}
