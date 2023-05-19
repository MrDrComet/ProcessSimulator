import java.util.ArrayList;

/*
1 May 2023
Liam Pitchford

Process Object file: Contains Pages and Transmap generation




 */

public class Process {

    public static int setterID = 1;

    private int ID;

    public static final int PROC_READY = 1;
    public static final int PROC_RUN = 2;
    public static final int PROC_BLOCK = 3;
    public static final int PROC_TERM = 4;

    private boolean isNew;
    private Page[] pages;
    private int currentLife;
    private int processState;
    private double[][] pageTransMap;

    private Page lastPageAccessed;

    public Process(int MIN_PAGES, int MAX_PAGES, int MIN_LIFE, int MAX_LIFE){
        pages = new Page[(int) (MIN_PAGES + (Math.random() * (MAX_PAGES - MIN_PAGES)))];
        currentLife = (int) (MIN_LIFE + (Math.random() * (MAX_LIFE - MIN_LIFE)));
        processState = PROC_READY;
        isNew = true;
        setUpPages();
        pageTransMap = generatePageTransMap(pages.length);

        ID = setterID;
        setterID++;
    }
    public void setUpPages(){
        for(int i = 0; i < pages.length; i++){
            pages[i] = new Page();
        }
    }
    public boolean isNew(){
        if(isNew){
            isNew = false;
            return true;
        }
        return false;
    }

    public double[][] generatePageTransMap(int numPages){
        double[][] transMap = new double[numPages][numPages];
        for(int i = 0; i < numPages; i++){
            double[] randNums = new double[numPages];
            double sum = 0;
            for(int k = 0; k < numPages; k++){
                randNums[k] = Math.random();
                sum += randNums[k];
            }
            double runningValue = 0;
            for(int k = 0; k < numPages; k++){
                transMap[i][k] = (randNums[k] / sum) + runningValue;
                runningValue += randNums[k];
            }
        }
        return transMap;
    }

    public double[][] getPageTransMap(){
        return pageTransMap;
    }

    public Page[] getPages(){
        return pages;
    }
    public int getCurrentLife(){
        return currentLife;
    }
    public int getProcessState(){
        return processState;
    }
    public void setProcessState(int newState){
        processState = newState;
    }

    public void run(){
        currentLife--;
        if(currentLife <= 0){
            setProcessState(PROC_TERM);
        }
    }

    public void setLastPageAccessed(Page page){
        lastPageAccessed = page;
    }
    public Page getLastPageAccessed(){
        return lastPageAccessed;
    }

    public int getID(){
        return ID;
    }
    public String toString(){
        return "Process_" + ID + "\tLife: " + currentLife;
    }

}
