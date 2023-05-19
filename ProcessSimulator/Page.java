
/*
1 May 2023
Liam Pitchford

Page Object file: ID and AccessBits




 */

public class Page {

    public static int setterID = 1;
    private int ID;
    private int R;
    private int M;

    public Page(){
        ID = setterID;
        setterID++;
        R = 0;
        M = 0;
    }
    public int getID(){
        return ID;
    }
    public void accessPageType(int accessType){
        R = 1;
        if(accessType == 1){
            M = 1;
        }
    }
    public void resetBit(){
        R = 0;
    }
    public int getR(){
        return R;
    }
    public int getM(){
        return M;
    }
    public String toString(){
        return "Page_" + ID;
    }
}
