package Stages;

import FunctionalUnits.RegisterFile;
import Simulation.Simulator;

public class WriteBack extends Stages
{
    RegisterFile rF;

    public WriteBack(Simulator simulator)
    {
        super(simulator);
        this.rF = simulator.getRegisterFile();
    }

    public void WriteBack()
    {

        if(simulator.getInstructionStages(4) != -2) {
            WriteBack(simulator.getMEM_WB().getRegister("writeBack").getInt(), simulator.getMEM_WB().getRegister("ALUResult").getValue(),
                    simulator.getMEM_WB().getRegister("ReadMemoryResult").getValue(), simulator.getMEM_WB().getRegister("MemReg").getBool(),
                    simulator.getMEM_WB().getRegister("RegWrite").getBool());
        }

    }

    public void WriteBack(int writeback, String aluResult, String readMemoryResult, Boolean MemReg, Boolean RegWrite) // use it in lw, shifters, r-types.
    {
        if(RegWrite) {
            if (MemReg)
                rF.setRegData(writeback, readMemoryResult);
            else
                rF.setRegData(writeback, aluResult);
        }
    }

    public void setToPipeline(){};

}
