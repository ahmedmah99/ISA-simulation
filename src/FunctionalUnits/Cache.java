package FunctionalUnits;

public class Cache
{
    boolean validBit;
    int Tag;
    int Index;
    String Data;

    public Cache(){this.validBit = false;}


    public void setAll(String Data, int Index, int Tag, boolean validBit)
    {
        this.Data = Data; this.Index = Index; this.Tag = Tag; this.validBit = validBit;
    }

}
