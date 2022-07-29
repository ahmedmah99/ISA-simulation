package FunctionalUnits;

public class Register
{

    String name;
    int size;
    String value;

    /**
     * initialization of the object Register, determining its size
     * @param size
     */
    public Register(int size) { this.size = size; }


    /**
     * segmentation of the value of register
     * @param startPoint
     * @param endPoint
     * @return
     */
    public String getPart(int startPoint, int endPoint) { return value.substring(startPoint,endPoint); }




    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public Integer getInt() {
        if(value != null)
            return Integer.parseInt(value,2);
        else return -1;
    }

    public Boolean getBool()
    {
        if(value != null) {
            if (value.equals("1"))
                return true;
            else if (value.equals("0"))
                return false;
        }
        return null;
    }

    public void setValue(String value) {
        this.value = value;
    }
}


