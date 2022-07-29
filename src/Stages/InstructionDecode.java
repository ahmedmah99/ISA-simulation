package Stages;

import FunctionalUnits.DataMemory;
import FunctionalUnits.InstructionMemory;
import FunctionalUnits.RegisterFile;
import Simulation.Simulator;


public class InstructionDecode extends Stages
{

    boolean RegDst, Branch, MemRead, MemReg, MemWrite, ALUSrc, RegWrite,Jump;
    String ALUOp;
    int firstRegister, secRegister; //firstreg reads inst from 4 tp 8, secReg from 8 to 12, write usually from 12 to 16
    RegisterFile registerFile;
    DataMemory dataMemory;
    String readData1;
    String readData2;
    String writeRegister;
    String immediateExtended;
    boolean bgt=false;
    boolean rtype=false;




    public InstructionDecode(Simulator simulator)
    {
        super(simulator);
        this.registerFile = simulator.getRegisterFile();
        this.dataMemory = simulator.getDataMemory();
    }



    /**
     * main method of the class that It takes the whole instruction as an input,
     * decodes it and passes the rs,rt and rd values to the register �?le to return/output Readdata1 and readdata2.
     * The 6-bits of the code are passed to the control unit.
     * The 16-bits of the immediate are passed to the signextend the �?rst 5-bits which are function bits are passed to the next class to be used in the next stage
     */

    public void InstDecode()
    {
        String instruction = simulator.getIF_ID().getRegister("Instruction").getValue();
        int PC = simulator.getIF_ID().getRegister("PC").getInt();
        clear();


        if(simulator.getInstructionStages(1) != -2)
            InstDecode(instruction, PC);

        simulator.setInstructionStages(simulator.getInstructionStages(1),2);

    }


    public void InstDecode(String instruction,int pc)
    {

        String opCode = instruction.substring(0,4);

        ContUnit(opCode);

        if (opCode.charAt(0) == '0') {
            if (opCode.equals("0100") || opCode.equals("0101")) {

                writeRegister = instruction.substring(4, 8);
                firstRegister = Integer.parseInt(instruction.substring(8, 12),2);
                immediateExtended = SignExtend(instruction.substring(12));

                readData1 = registerFile.getRegData(firstRegister);

            } else {
                rtype = true;
                firstRegister = Integer.parseInt(instruction.substring(4, 8), 2);
                secRegister = Integer.parseInt(instruction.substring(8, 12), 2);
                writeRegister = instruction.substring(12);

                readData1 = registerFile.getRegData(firstRegister);
                readData2 = registerFile.getRegData(secRegister);

            }
        }
        else {
            if(opCode.equals("1000") || opCode.equals("1001") || opCode.equals("1011") || opCode.equals("1010")) { // lw, sw, add i , or i
                writeRegister = instruction.substring(4, 8);
                secRegister = Integer.parseInt(instruction.substring(8, 12), 2);
                immediateExtended = SignExtend(instruction.substring(12));

                readData1 = registerFile.getRegData(secRegister);


            }
            else if(opCode.equals("1100")) //bne
            {
                rtype=true;
                firstRegister = Integer.parseInt(instruction.substring(4, 8), 2);
                secRegister = Integer.parseInt(instruction.substring(8, 12), 2);
                immediateExtended = SignExtend(instruction.substring(12));

                readData1 = registerFile.getRegData(firstRegister);
                readData2 = registerFile.getRegData(secRegister);



            }
            else if (opCode.equals("1101")) // bgt
            {
                bgt=true;
                firstRegister = Integer.parseInt(instruction.substring(4, 8), 2);
                immediateExtended = SignExtend(instruction.substring(8));

                readData1 = registerFile.getRegData(firstRegister);

            }

            else if(opCode.equals("1111")) { //jump
                immediateExtended = SignExtend(instruction.substring(4));
            }

        }
        setToPipeline();

    }

    public void clear()
    {
        readData1 = null;
        readData2 = null;
        immediateExtended = null;
        writeRegister = null;
    }

    /**
     * send Registers to pipeline
     */

    @Override
    public void setToPipeline()
    {
        //System.out.println("MemRead1: " + MemRead);
        //System.out.println("MemWrite: " + MemWrite);
        simulator.getID_EX().setRegister("immediateExtended",immediateExtended);
        simulator.getID_EX().setRegister("readData1",readData1);
        simulator.getID_EX().setRegister("readData2",readData2);
        simulator.getID_EX().setRegister("writeBack",writeRegister);
        simulator.getID_EX().setRegister("booleanBGT",booleanToString(bgt));
        simulator.getID_EX().setRegister("booleanR",booleanToString(rtype));
        simulator.getID_EX().setRegister("RegDst",booleanToString(RegDst));
        simulator.getID_EX().setRegister("Branch",booleanToString(Branch));
        simulator.getID_EX().setRegister("MemRead",booleanToString(MemRead));
        simulator.getID_EX().setRegister("MemReg",booleanToString(MemReg));
        simulator.getID_EX().setRegister("ALUOp",ALUOp);
        simulator.getID_EX().setRegister("MemWrite",booleanToString(MemWrite));
        simulator.getID_EX().setRegister("ALUSrc",booleanToString(ALUSrc));
        simulator.getID_EX().setRegister("RegWrite",booleanToString(RegWrite));
        simulator.getID_EX().setRegister("Jump",booleanToString(Jump));
    }

    /**
     * its a method that extend the 16 bit instruction to 32 bit
     * @param _16bit is the last 16 bit of an immediate instruction
     * @return 32 bit instruction
     */
    private String SignExtend(String _16bit)
    {
        char leftmostChar = _16bit.charAt(0);
        StringBuilder _16bitBuilder = new StringBuilder(_16bit);
        while (_16bitBuilder.length() != 16)
            _16bitBuilder.insert(0, leftmostChar);
        _16bit = _16bitBuilder.toString();
        return _16bit;
    }


    public String booleanToString(boolean bool)
    {
        if(bool)
            return "1";
        else
            return "0";
    }

    /**
     * adjust the control unit signals according to type of instruction(from Opcode)
     * @param opCode is the first 6 bits of a 32 bit instruciton

     * Add - 0000 add
     * sub - 0001 sub
     * mult - 0010 mult
     * AND - 0011 AND
     * SLL - 0100 sll
     * SRL - 0101 srl
     * SLT - 0110 set less than
     * Add i - 1000 add
     * OR i - 1001 or
     * lw - 1010 add
     * sw - 1011 add
     * bne - 1100 sub
     * bgt - 1101 sub
     * jump - 1111 add
     */

    public void ContUnit(String opCode)
    {
        switch (opCode)
        {
            case "0000":
                RegDst = true; ALUSrc = false; MemReg = false; RegWrite = true; MemRead = false; MemWrite = false; Branch = false;Jump = false;
                ALUOp = "0000";
                break;
            case "0001":
                RegDst = true; ALUSrc = false; MemReg = false; RegWrite = true; MemRead = false; MemWrite = false; Branch = false;Jump = false;
                ALUOp = "0001";
                break;
            case "0010":
                RegDst = true; ALUSrc = false; MemReg = false; RegWrite = true; MemRead = false; MemWrite = false; Branch = false;Jump = false;
                ALUOp = "0010";
                break;
            case "0011":
                RegDst = true; ALUSrc = false; MemReg = false; RegWrite = true; MemRead = false; MemWrite = false; Branch = false;Jump = false;
                ALUOp = "0011";
                break;
            case "0100":
                RegDst = true; ALUSrc = true; MemReg = false; RegWrite = true; MemRead = false; MemWrite = false; Branch = false;Jump = false;
                ALUOp = "0100";
                break;
            case "0101":
                RegDst = true; ALUSrc = true; MemReg = false; RegWrite = true; MemRead = false; MemWrite = false; Branch = false;Jump = false;
                ALUOp = "0101";
                break;
            case "0110":
                RegDst = true; ALUSrc = false; MemReg = false; RegWrite = true; MemRead = false; MemWrite = false; Branch = false;Jump = false;
                ALUOp = "0110";
                break;

            case "1000":
                RegDst = false; ALUSrc = true; MemReg = false; RegWrite = true; MemRead = false; MemWrite = false; Branch = false;Jump = false;
                ALUOp = "0000";
                break;
            case "1001":
                RegDst = false; ALUSrc = true; MemReg = false; RegWrite = true; MemRead = false; MemWrite = false; Branch = false;Jump = false;
                ALUOp = "1001";
                break;
            case "1010":
                RegDst = false; ALUSrc = true; MemReg = true; RegWrite = true; MemRead = true; MemWrite = false; Branch = false;Jump = false;
                ALUOp = "0000";
                break;
            case "1011":
                RegDst = false; ALUSrc = true; MemReg = false; RegWrite = false; MemRead = false; MemWrite = true; Branch = false;Jump = false;
                ALUOp = "0000";
                break;
            case "1100":
            case "1101":
                RegDst = false; ALUSrc = false; MemReg = false; RegWrite = false; MemRead = false; MemWrite = false; Branch = true;Jump = false;
                ALUOp = "0001";
                break;
            case "1111":
                RegDst = false; ALUSrc = false; MemReg = false; RegWrite = false; MemRead = false; MemWrite = false; Branch = false; Jump = true;
                ALUOp = "1111";
        }
    }

}