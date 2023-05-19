
/*
1 May 2023
Liam Pitchford

Snapshot Object file: All necessary information for each time of the simulation




 */

public class Snapshot {

    private int savedTime;
    private boolean savedPageFault;
    private int[] savedProcesses;
    private int[] savedLife;
    private int[] savedPages;

    public Snapshot(int time, boolean pageFault, Processor[] processor, int[] currentLife, Page[] pages){
        savedTime = time;
        savedPageFault = pageFault;
        savedProcesses = new int[processor.length];
        for(int i = 0; i < processor.length; i++){
            if(processor[i].containsProcess())
                savedProcesses[i] = processor[i].getCurrentProcess().getID();
        }
        savedLife = new int[currentLife.length];
        for(int i = 0; i < currentLife.length; i++){
            savedLife[i] = currentLife[i];
        }
        savedPages = new int[pages.length];
        for(int i = 0; i < pages.length; i++){
            if(pages[i] == null)
                break;
            savedPages[i] = pages[i].getID();
        }

    }
    public void outputSnapshot(){
        System.out.println("Time: " + savedTime + "\t|\tPage Fault Occurred: " + savedPageFault);
        System.out.print("Processes: ");
        for(int i = 0; i < savedProcesses.length; i++){
            System.out.print("Process_" + savedProcesses[i] + "\tCurrent Life: " + savedLife[i] + "\t|\t");
        }
        System.out.print("\nPages: ");
        for(int i = 0; i < savedPages.length; i++){
            if(savedPages[i] == 0)
                System.out.print("NoPage ");
            else
                System.out.print("Pages_" + savedPages[i] + " ");
        }
        System.out.println("\n");
    }
}
