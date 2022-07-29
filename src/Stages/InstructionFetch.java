package Stages;

import FunctionalUnits.*;
import Simulation.Simulator;

import javax.xml.crypto.Data;

public class InstructionFetch extends Stages
{

    int pc;
    String fetchInstruction;
    InstructionMemory instructionMemory;

    public InstructionFetch(Simulator simulator) {

        super(simulator);
        this.pc = 0;
        this.instructionMemory = simulator.getInstructionMemory();
    }


    public void InstFetch()
    {

        if(simulator.getIF_ID().getRegister("PC").getValue() != null) {

            pc = Integer.parseInt(simulator.getIF_ID().getRegister("PC").getValue(),2);
            String instruction = instructionMemory.getInstruction(pc);

            if (instruction == null) {
                simulator.setInstructionStages(-2, 0);
                simulator.setInstructionStages(-2, 1);
            } else {

                simulator.setInstructionIndex(simulator.getInstructionIndex() + 1);
                simulator.setInstructionStages(simulator.getInstructionIndex(), 0);
                simulator.setInstructionStages(simulator.getInstructionIndex(), 1);

                InstFetch(instruction);
            }
        }
    }
    /**
     * it returns the fetched instuction
     * @return the Instruction
     */
    public void InstFetch(String instruction)
    {

        fetchInstruction = instruction;

        ProgCount();
        setToPipeline(); // send it to pipeline



    }

    @Override
    public void setToPipeline() {

        simulator.getIF_ID().setRegister("PC",Integer.toBinaryString(pc));
        simulator.getIF_ID().setRegister("Instruction",fetchInstruction);
    }

    public int getPc() {
        return pc;
    }

    /**
     * increment the PC counter after every fetch of an instruction
     */
    public void ProgCount()
    {

        if(pc < instructionMemory.getInstCount())
        {
            pc = pc + 4;
        }
    }

}


