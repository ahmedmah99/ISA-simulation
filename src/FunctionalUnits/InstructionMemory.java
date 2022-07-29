package FunctionalUnits;

public class InstructionMemory
{
    String[] instructions;
    int instCount = 0;

    public InstructionMemory(int size) { this.instructions = new String[size]; }

    public String getInstruction(int PC) { return instructions[PC]; }



    public void loadInstruction(String instruction)
    {
        this.instructions[instCount] = instruction;
        instCount = instCount + 4;
    }


    public int getInstCount() {
        return instCount;
    }

    public void setInstCount(int instCount) {
        this.instCount = instCount;
    }

}
