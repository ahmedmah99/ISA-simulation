package Simulation;

import FunctionalUnits.DataMemory;
import FunctionalUnits.InstructionMemory;
import FunctionalUnits.PipelineRegisters;
import FunctionalUnits.RegisterFile;
import Stages.*;
import org.w3c.dom.css.RGBColor;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;

public class Simulator
{
    // colors used in printing
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RED = "\033[0;31m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String YELLOW_BOLD = "\033[1;33m";

    int instructionIndex = 0;

    int[] instructionStages = new int[5];
    int[] tmpInstructionStages = new int[5];

    PipelineRegisters IF_ID;
    PipelineRegisters ID_EX;
    PipelineRegisters EX_MEM;
    PipelineRegisters MEM_WB;

    DataMemory dataMemory;
    RegisterFile registerFile;
    InstructionMemory instructionMemory;


    InstructionFetch instructionFetch;
    InstructionDecode instructionDecode;
    Execute execute;
    MemoryAccess memoryAccess;
    WriteBack writeBack;

    public Simulator()
    {

        Arrays.fill(instructionStages,-2);
        Arrays.fill(tmpInstructionStages,-2);
        instructionStages[0] = 0;

        //create the four main pipeline registers
         IF_ID = new PipelineRegisters("IF->ID");
         ID_EX = new PipelineRegisters("ID->EX");
         EX_MEM = new PipelineRegisters("EX->MEM");
         MEM_WB = new PipelineRegisters("MEM->WB");

        /**
         * create units
         */
        dataMemory = new DataMemory(1024); // size of data memory

        registerFile = new RegisterFile(16); //16 register in register file

        instructionMemory = new InstructionMemory(1024); //size of instruction memory


        /**
         * create stages
         */
        instructionFetch = new InstructionFetch(this);
        instructionDecode = new InstructionDecode(this);
        execute = new Execute(this);
        memoryAccess = new MemoryAccess(this);
        writeBack = new WriteBack(this);
    }

    /**
     * simulate
     */
    public void simulate()
    {
        int clockCycle = 1;



        while (processing())
        {

            instructionFetch.InstFetch();
            instructionDecode.InstDecode();
            execute.Execute();
            memoryAccess.MemAccess();
            writeBack.WriteBack();

            //update pipeline registers with the new values
            transferAllPipelineRegister();


            toString(clockCycle++);

            arrayCopy();
        }
    }

    /**
     * transfer all pipeline register values from the temp one to the Original
     */
    public void transferAllPipelineRegister()
    {
        IF_ID.transfer();
        ID_EX.transfer();
        EX_MEM.transfer();
        MEM_WB.transfer();
    }


    /**
     * check if there is still more instructions processed
     * @return a boolean value, true if there are still more instruction to continue process
     * false if no more
     */

    public boolean processing()
    {
        int count = 0;
        for (int instructionStage : instructionStages) {
            if (instructionStage == -2)
                count++;
        }
        return count != 5;
    }

    public PipelineRegisters getIF_ID() {
        return IF_ID;
    }
    public PipelineRegisters getID_EX() {
        return ID_EX;
    }
    public PipelineRegisters getEX_MEM() {
        return EX_MEM;
    }
    public PipelineRegisters getMEM_WB() {
        return MEM_WB;
    }
    public DataMemory getDataMemory() {
        return dataMemory;
    }
    public RegisterFile getRegisterFile() {
        return registerFile;
    }
    public InstructionMemory getInstructionMemory() {
        return instructionMemory;
    }


    public int getInstructionStages(int index) { return instructionStages[index]; }

    public void setInstructionStages(int value, int index) { tmpInstructionStages[index] = value; }


    public void arrayCopy() { System.arraycopy(tmpInstructionStages, 0, instructionStages, 0, tmpInstructionStages.length); }


    public int getInstructionIndex() {
        return instructionIndex;
    }

    public void setInstructionIndex(int instructionIndex) {
        this.instructionIndex = instructionIndex;
    }



    public void toString(int clockCycle)
    {
        System.out.println("\n");
        System.out.println("Clock cycle: " + clockCycle);

        if(tmpInstructionStages[0] != -2) {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("                INSTRUCTION FETCH STAGE             ");
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("Instruction " + tmpInstructionStages[0] + " " + "is in Fetch Stage");
            System.out.println("Fetched Instruction: "+ getIF_ID().getRegister("Instruction").getValue());
            System.out.println("next PC: "+ getIF_ID().getRegister("PC").getInt());


            System.out.println("\n");
        }




        if(instructionStages[1] != -2) {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("               INSTRUCTION DECODE STAGE             ");
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("Instruction "+ instructionStages[1] + " " + "is in Decode Stage");
            ID_EX.print();
            System.out.println("\n");
        }




        if(instructionStages[2] != -2) {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("                INSTRUCTION EXECUTE STAGE             ");
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("Instruction " + instructionStages[2] + " " + "is in Execute Stage");
            EX_MEM.print();
            System.out.println("\n");
        }




        if(instructionStages[3] != -2) {

            //System.out.println("h" + getEX_MEM().getRegister("MemRead").getValue());
            //System.out.println("hh" +getEX_MEM().getRegister("MemWrite").getValue());
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("                MEMORY ACCESSING STAGE             ");
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("Instruction " + instructionStages[3] + " " + "is in Memory Accessing Stage");
            MEM_WB.print();

            System.out.println("\n");
        }


        if(instructionStages[4] != -2) {

            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("                WRITE BACK STAGE             ");
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("Instruction " + instructionStages[4]  + " " + "is in WB Stage");
        }
    }
}

