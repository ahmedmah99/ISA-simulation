package FunctionalUnits;


public class DataMemory
{
    String[] Memory;
    Cache[] _Cache;

    public DataMemory(int size) {
        _Cache = new Cache[16];
        Memory = new String[size];
        fill();
        for(int i = 0; i < 16; i++)
        {
           _Cache[i] = new Cache();
        }
    }

    private void fill()
    {
        for(int i = 0; i < Memory.length; i++)
            Memory[i] = Integer.toBinaryString(i);
    }

    public void writeToMem(int address, String data)
    {
        //write to Cache

        int Index = address%16;
        int Tag = address/16;
        _Cache[Index].setAll(data,Index,Tag,true);

        Memory[address] = _Cache[Index].Data;

    }

    public String readMem(int address)
    {
        //read from cache
        int Index = address%16;
        int Tag = address/16;
        String data = "";

        if(_Cache[Index].validBit) {
            if (_Cache[Index].Tag == Tag)
                data = _Cache[Index].Data;
            else {
                data = Memory[address];
                _Cache[Index].setAll(data,Index,Tag,true);
            }
        }
        else
        {
            data = Memory[address];
            _Cache[Index].setAll(data,Index,Tag,true);
        }

        return data;
    }



}


