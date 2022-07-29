package Simulation;

public class Controller
{

    public static void main(String[] args)
    {
        Simulator simulator = new Simulator();


        simulator.getInstructionMemory().loadInstruction("1000000010000000"); // addi inst, v in reg 0 + v in reg 8, save in reg 0
        simulator.getInstructionMemory().loadInstruction("1111000000000010"); //jump to address 5
        simulator.getInstructionMemory().loadInstruction("1010111110010010"); // lw inst, v in reg 9 + 2(imm) <== address , get from memory and set in reg 15
        simulator.getInstructionMemory().loadInstruction("0011101010110011"); // AND inst, v in reg 10 ANDING v in reg 11, save in reg 3
        simulator.getInstructionMemory().loadInstruction("1001110111100000"); // OR i inst, v in reg 13 OR v in reg 14, save in reg 0
        simulator.getInstructionMemory().loadInstruction("1011110001110100"); // sw inst, v in reg 7 + 4(imm) <== address , save v of reg 12 in memory with previous address
        simulator.getInstructionMemory().loadInstruction("1100100110010011"); // bne inst, v in reg 9 - 9 , if result == 0, branch to immediate value (3)
        simulator.getInstructionMemory().loadInstruction("0010000001100001"); // mult inst, v i reg 0 MUL BY v in reg 6 , save it in reg 1
        simulator.getInstructionMemory().loadInstruction("1101000000000001"); // bgt inst, v in reg 0 if > 0 , branch to imm v (1)
        simulator.getInstructionMemory().loadInstruction("0100110111110011"); // sll inst, v in reg 15 * 2^imm(3) , save in reg 13
        simulator.getInstructionMemory().loadInstruction("0101011001010010"); // slr inst, v in reg 5 / 2^imm(2) , save in reg 6
        simulator.getInstructionMemory().loadInstruction("0110010101100011"); //slt inst, if v in reg 5 < v in reg 6, write result in reg 3
        simulator.getInstructionMemory().loadInstruction("1111000000000001"); //jump to address 5


        simulator.simulate();
    }
}
