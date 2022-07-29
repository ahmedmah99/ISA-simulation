package Stages;

import FunctionalUnits.DataMemory;
import FunctionalUnits.RegisterFile;
import Simulation.Simulator;

public class MemoryAccess extends Stages{

    DataMemory dataMemory;
    String ReadMemoryResult;

    //print purposes
    int address;

    RegisterFile registerFile;

    String ALUResult,writeRegister;

    public MemoryAccess(Simulator simulator) {
        super(simulator);
        this.dataMemory = simulator.getDataMemory();
        this.registerFile = simulator.getRegisterFile();
    }


    public void MemAccess()
    {

       if(simulator.getInstructionStages(3) != -2) {

           //System.out.println("memerevbfreuvbcuvtr  "+simulator.getEX_MEM().getRegister("MemRead").getBool());
           //System.out.println(simulator.getEX_MEM().getRegister("MemWrite").getBool());

           if(simulator.getEX_MEM().getRegister("MemRead").getBool() || simulator.getEX_MEM().getRegister("MemWrite").getBool()) {
               //System.out.println("haaaaaaaaany");
               MemAccess(simulator.getEX_MEM().getRegister("ALUResult").getValue(),
                       simulator.getEX_MEM().getRegister("MemRead").getBool(), simulator.getEX_MEM().getRegister("MemWrite").getBool(),
                       simulator.getEX_MEM().getRegister("writeBack").getInt());
           }
           else
               setToPipeline();
        }
       else
            setToPipeline();

        simulator.setInstructionStages(simulator.getInstructionStages(3),4);
    }


    public void MemAccess(String AlUResult, Boolean MemRead, Boolean MemWrite,int writeRegister ) {
        address = Integer.parseInt(AlUResult, 2);

        if (MemWrite) {

            String write = registerFile.getRegData(writeRegister);
            dataMemory.writeToMem(address, write);
        }
        else if (MemRead) {
            ReadMemoryResult = dataMemory.readMem(address);
        }

        setToPipeline();
    }

    @Override
    public void setToPipeline() {

        simulator.getMEM_WB().setRegister("ALUResult",simulator.getEX_MEM().getRegister("ALUResult").getValue());
        simulator.getMEM_WB().setRegister("writeBack",simulator.getEX_MEM().getRegister("writeBack").getValue());
        simulator.getMEM_WB().setRegister("ReadMemoryResult",ReadMemoryResult);
        simulator.getMEM_WB().setRegister("MemReg",simulator.getEX_MEM().getRegister("MemReg").getValue());
        simulator.getMEM_WB().setRegister("RegWrite",simulator.getEX_MEM().getRegister("RegWrite").getValue());
        simulator.getMEM_WB().setRegister("RegDst",simulator.getEX_MEM().getRegister("RegDst").getValue());
        simulator.getMEM_WB().setRegister("MemRead",simulator.getEX_MEM().getRegister("MemRead").getValue());
        simulator.getMEM_WB().setRegister("MemWrite",simulator.getEX_MEM().getRegister("MemWrite").getValue());



    }

}
