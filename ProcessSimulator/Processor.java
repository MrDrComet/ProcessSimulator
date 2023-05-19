
/*
1 May 2023
Liam Pitchford

Processor Object file: Contains Process Objects




 */


public class Processor {

    public static int setterID = 1;

    private Process currentProcess;
    private int ID;
    private int initial;

    public Processor(){
        currentProcess = null;
        ID = setterID;
        setterID++;
        initial = -1;
    }

    public Process getCurrentProcess(){
        return currentProcess;
    }
    public void setCurrentProcess(Process process){
        currentProcess = process;
    }
    public void runProcess(){
        currentProcess.run();
    }
    public int getID(){
        return ID;
    }
    public boolean containsProcess(){
        if(currentProcess == null){
            return false;
        }
        return true;
    }
    public void setInitial(int setter){
        initial = setter;
    }
    public int getInitial(){
        return initial;
    }
    public String toString(){
        return "Processor_" + ID + ": " + currentProcess;
    }
}
