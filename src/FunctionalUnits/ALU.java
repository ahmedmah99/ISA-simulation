package FunctionalUnits;

public class ALU
{


    int zFlag;


    public String ALUEvaluator (String Op, String readData1 , String readData2)
    {
        int Operand1 = Integer.parseInt(readData1,2);
        int Operand2 = Integer.parseInt(readData2,2);


        int result = -1;
        switch (Op)
        {
            case "0011": result = Operand1 & Operand2;break; //AND
            case "1001": result = Operand1 | Operand2;break; //OR
            case "0000" : result = Operand1 + Operand2; break; //
            case "0010" : result = Operand1 * Operand2;break;
            case "0100" : result = Operand1 * 2^Operand2;break; //SLL
            case "0101" : result = Operand1 / 2^Operand2;break; //SLR
            case "0001": result = Operand1 - Operand2;break;
            case "0110": result = (Operand1 < Operand2)? 1 : 0; // SET less that
            default: System.out.println("Invalid Operation Name");
        }

        return SignExtend(Integer.toBinaryString(result));
    }


    public void zflag(int output)
    {
        if (output == 0)
            zFlag = 1;
        else zFlag = 0;
    }

    public String SignExtend(String _16bit)
    {
        StringBuilder _16bitBuilder = new StringBuilder(_16bit);
        while (_16bitBuilder.length() != 16)
            _16bitBuilder.insert(0, "0");
        _16bit = _16bitBuilder.toString();
        return _16bit;
    }

    public int getzFlag() {
        return zFlag;
    }

    public void setzFlag(int zFlag) {
        this.zFlag = zFlag;
    }

}