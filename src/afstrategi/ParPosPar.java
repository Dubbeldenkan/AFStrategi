package afstrategi;

public class ParPosPar
{
    private PosPar Pos1;
    private PosPar Pos2;
    
    
    public ParPosPar(PosPar Pos1, PosPar Pos2)
    {
        this.Pos1 = Pos1;
        this.Pos2 = Pos2;
    }   
    
    public PosPar getPos1()
    {
        return Pos1;
    }
    
    public PosPar getPos2()
    {
        return Pos2;
    }
    
}
