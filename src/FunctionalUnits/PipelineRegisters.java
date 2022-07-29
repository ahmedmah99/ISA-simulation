package FunctionalUnits;

import java.util.Hashtable;
import java.util.Set;

public class PipelineRegisters
{
    public static final String WHITE_BOLD = "\033[1;37m";
    public static final String RESET = "\033[0m";  // Text Reset
    Hashtable<String,Register> pipeline;
    PipelineRegisters tmpPipeline;
    String type;


    public PipelineRegisters(String type)
    {
        this(type,false);
    }


    public PipelineRegisters(String type, boolean initializeTmp)
    {
        this.type = type;
        pipeline = new Hashtable<>();
        switch (type)
        {
            case "IF->ID":
                pipeline.put("PC",new Register(16));
                pipeline.get("PC").setValue("0");
                pipeline.put("Instruction",new Register(16)); // < -------------------
                break;

            case "ID->EX":
                pipeline.put("RegDst", new Register(1));
                pipeline.put("Branch", new Register(1));
                pipeline.put("MemRead", new Register(1));
                pipeline.put("MemReg", new Register(1));
                pipeline.put("ALUOp", new Register(2));  // < -------------------
                pipeline.put("MemWrite", new Register(1));
                pipeline.put("ALUSrc", new Register(1));
                pipeline.put("RegWrite", new Register(1));
                pipeline.put("readData1", new Register(16));
                pipeline.put("readData2", new Register(16));
                pipeline.put("immediateExtended", new Register(16));
                pipeline.put("writeBack",new Register(16));
                pipeline.put("booleanBGT",new Register(1));
                pipeline.put("booleanR",new Register(1));
                pipeline.put("address", new Register(16));
                pipeline.put("PC",new Register(16));
                pipeline.put("Jump", new Register(16));
                break;

            case "EX->MEM":
                pipeline.put("RegWrite", new Register(1));
                pipeline.put("MemReg", new Register(1));
                pipeline.put("MemRead", new Register(1));
                pipeline.put("MemWrite", new Register(1));
                pipeline.put("BranchAddress", new Register(16));
                pipeline.put("ALUResult", new Register(16));
                pipeline.put("readData1", new Register(16));
                pipeline.put("writeBack",new Register(16));
                pipeline.put("RegDst", new Register(1));
                pipeline.put("ZeroFlag",new Register(1));
                break;

            case "MEM->WB":
                pipeline.put("RegWrite", new Register(1));  // < --------------
                pipeline.put("MemReg", new Register(1));
                pipeline.put("ReadMemoryResult", new Register(16));
                pipeline.put("ALUResult", new Register(16));
                pipeline.put("writeBack",new Register(16));
                pipeline.put("RegDst", new Register(1));
                pipeline.put("MemRead", new Register(1));
                pipeline.put("MemWrite", new Register(1));
                break;

            default: System.out.println("INVALID TYPE");


        }

        if(!initializeTmp) //to initialize the tmpPipeLine only once
        {
            tmpPipeline = new PipelineRegisters(type, true);
        }
    }

    /**
     * get the register with the specific name from the Hashtable
     * @param name
     * @return
     */

    public Register getRegister(String name) { return pipeline.get(name); }


    /**
     * set in the tmpPipline Register with
     * @param name the value
     * @param value .
     */
    public void setRegister(String name, String value) {
        tmpPipeline.pipeline.get(name).setValue(value);
    }


    public void transfer()
    {
        Set<String> keys = pipeline.keySet();
        for(String key: keys)
        {
            pipeline.get(key).setValue(tmpPipeline.getRegister(key).getValue());
            tmpPipeline.getRegister(key).setValue(null);
        }
    }


    public void print()
    {

        String r = "";
        Set<String> keys = pipeline.keySet();
        for (String key : keys) {
            if (pipeline.get(key).value != null && !key.equals("booleanR") && !key.equals("booleanBGT"))
            {
                if (pipeline.get(key).value.length() > 1)
                    System.out.println(key + ": " + pipeline.get(key).value + "/" + Integer.parseInt(pipeline.get(key).value, 2));
                else
                    System.out.println(key + ": " + pipeline.get(key).value );
            }
        }
    }
}
