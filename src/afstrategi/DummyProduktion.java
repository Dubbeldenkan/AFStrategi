package afstrategi;

public class DummyProduktion extends ProduktionsObjekt{
    
    DummyProduktion()
    {
        super(0, 0, 0, Grafik.svart);
        this.ärDummy = true;
    }
    
    @Override
    public void produktionKlar(Spelare spelaren)
    {
        
    }
}
