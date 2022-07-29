package FunctionalUnits;

import java.util.Hashtable;

public class RegisterFile
{
    Register[] registers;
    boolean regWrite;

    public RegisterFile(int size)
    {
        this. registers = new Register[size]; // our ISA have 16 register
        fill();
        fillregisters();
        regWrite = false;
    }

    private void fill()
    {
        for(int i  =0; i < 16; i++)
            registers[i] = new Register(16);
    }

    private void fillregisters()
    {
        for(int i = 0; i < registers.length; i++)
            registers[i].setValue(SignExtend(Integer.toBinaryString(i)));
    }

    public String SignExtend(String _16bit)
    {
        StringBuilder _16bitBuilder = new StringBuilder(_16bit);
        while (_16bitBuilder.length() != 16)
            _16bitBuilder.insert(0, "0");
        _16bit = _16bitBuilder.toString();
        return _16bit;
    }

    /**
     * get the data of from register at the specified index
     * @param regNum the index of the register to get
     * @return data of the register
     */
    public String getRegData(int regNum) { return registers[regNum].value;}




    public void setRegData(int value , String instructionData)
    {
        registers[value].setValue(instructionData);
    }

}

