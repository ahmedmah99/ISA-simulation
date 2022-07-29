package Stages;
import FunctionalUnits.ALU;
import FunctionalUnits.DataMemory;
import FunctionalUnits.RegisterFile;
import Simulation.Simulator;


public class Execute extends Stages
{


    String aluResult;

    DataMemory dataMemory;
    RegisterFile registerFile;
    String branchAddressResult;
    String Zero;
    String readData1;


    public Execute(Simulator simulator)
    {
        super(simulator);
        this.dataMemory = simulator.getDataMemory();
        this.registerFile = simulator.getRegisterFile();
    }



    public void Execute()
    {
        clear();
        if(simulator.getInstructionStages(2) != -2) {
            //System.out.println("taaaaa7t "+simulator.getID_EX().getRegister("MemRead").getValue());
            //System.out.println(simulator.getID_EX().getRegister("MemWrite").getValue());
            Execute(simulator.getID_EX().getRegister("ALUOp").getValue(), simulator.getID_EX().getRegister("readData1").getValue(),
                    simulator.getID_EX().getRegister("readData2").getValue(), simulator.getID_EX().getRegister("immediateExtended").getValue(),
                    simulator.getID_EX().getRegister("Branch").getBool(),
                    simulator.getID_EX().getRegister("booleanBGT").getBool(),
                    simulator.getID_EX().getRegister("ALUSrc").getBool(),simulator.getID_EX().getRegister("Jump").getBool());

        }

        setToPipeline();
        simulator.setInstructionStages(simulator.getInstructionStages(2),3);
    }

    /**
     * excute the mips instruction command
     * @param ALUop is the 2 bits that determine the command with the help of param
     * @param readData1 data in first register
     * @param readData2 data in sec register
     */
    public void Execute(String ALUop, String readData1, String readData2,String Immediate,Boolean Branch,Boolean bgt,Boolean ALUSrc, Boolean Jump)
    {
        ALU alu = new ALU();
        if(!bgt&&!ALUSrc &&!Jump)
            aluResult = alu.ALUEvaluator(ALUop,readData1,readData2);
        else if(!bgt &&!Jump)
            aluResult = alu.ALUEvaluator(ALUop,readData1,Immediate);
        else if(Jump) {
            branchAddressResult = Integer.toBinaryString(
                    simulator.getIF_ID().getRegister("PC").getInt() + Integer.parseInt(Immediate, 2) * 4 + 4);

            simulator.getIF_ID().setRegister("PC",Integer.toBinaryString(
                    simulator.getIF_ID().getRegister("PC").getInt() + Integer.parseInt(Immediate, 2) * 4 + 4));
        }
        else if(bgt)
            aluResult = alu.ALUEvaluator(ALUop,readData1,"0");



        if(Branch && alu.getzFlag() == 0) {
            branchAddressResult = Integer.toBinaryString(
                    simulator.getIF_ID().getRegister("PC").getInt() + Integer.parseInt(Immediate, 2) * 4 + 4);

            simulator.getIF_ID().setRegister("PC", Integer.toBinaryString(
                    simulator.getIF_ID().getRegister("PC").getInt() + Integer.parseInt(Immediate, 2) * 4 + 4));
        }


        this.readData1 = readData1;
        this.branchAddressResult = branchAddressResult;
        this.Zero = Integer.toBinaryString(alu.getzFlag());

        //sent to pipeline.


    }

    public void clear()
    {
        this.Zero = null;
        this.branchAddressResult = null;
    }

    public void setToPipeline()
    {
        //System.out.println("taaaaa7t "+simulator.getID_EX().getRegister("MemRead").getValue());
        //System.out.println(simulator.getID_EX().getRegister("MemWrite").getValue());

        simulator.getEX_MEM().setRegister("ALUResult",aluResult);
        simulator.getEX_MEM().setRegister("readData1",readData1);
        simulator.getEX_MEM().setRegister("BranchAddress",this.branchAddressResult);
        simulator.getEX_MEM().setRegister("ZeroFlag",Zero);
        simulator.getEX_MEM().setRegister("MemRead",simulator.getID_EX().getRegister("MemRead").getValue());
        simulator.getEX_MEM().setRegister("MemWrite",simulator.getID_EX().getRegister("MemWrite").getValue());
        simulator.getEX_MEM().setRegister("RegWrite",simulator.getID_EX().getRegister("RegWrite").getValue());
        simulator.getEX_MEM().setRegister("MemReg",simulator.getID_EX().getRegister("MemReg").getValue());
        simulator.getEX_MEM().setRegister("RegDst",simulator.getID_EX().getRegister("RegDst").getValue());
        simulator.getEX_MEM().setRegister("writeBack",simulator.getID_EX().getRegister("writeBack").getValue());

    }

    public String booleanToString(boolean bool)
    {
        if(bool)
            return "1";
        else return "0";
    }

    public boolean memAccess(boolean memRead, boolean memWrite)
    {
        return memRead || memWrite;
    }



    public String toString() {
        return "-----------Execute Stage---------: " + "\n" + "ALUResult: " + aluResult+"/"+Integer.parseInt(aluResult,2) +
                "\n" + "BranchAddressResult: " + branchAddressResult;
    }

}
