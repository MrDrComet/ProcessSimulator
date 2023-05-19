import java.util.ArrayList;

/*
1 May 2023
Liam Pitchford

Simulation Object file: Contains Processors and Pages in Memory




 */


public class Simulation {

    public static final int PROC_READY = 1;
    public static final int PROC_RUN = 2;
    public static final int PROC_BLOCK = 3;
    public static final int PROC_TERM = 4;

    private Processor[] processors;
    private Process[] processes;

    private Page[] pagesInMem;
    private Queue queuePagesInMem;

    private int time;
    private boolean currentPageFaultOccured;
    private boolean pageFaultOccured;
  //  private Page[] currentPages;
    private int accessType;

    private ArrayList<Page> pages;

    private String PROCESS_ALG;
    private String PAGING_ALG;

    public Simulation(int NUM_PROCESSORS, int MAX_MEM_PAGES, int NUM_PROCESSES, int MIN_PAGES, int MAX_PAGES, int MIN_LIFE, int MAX_LIFE, String processAlg, String pagingAlg){
        time = 0;
        processors = new Processor[NUM_PROCESSORS];
        processes = new Process[NUM_PROCESSES];
        PROCESS_ALG = processAlg;
        PAGING_ALG = pagingAlg;
        //currentPages = new Page[NUM_PROCESSORS];
        accessType = 0;
        pages = new ArrayList<Page>();

        pagesInMem = new Page[MAX_MEM_PAGES];
        queuePagesInMem = new Queue(MAX_MEM_PAGES);

        setUpProcessors();
        setUpProcesses(MAX_PAGES, MIN_PAGES, MAX_LIFE, MIN_LIFE);
    }
    public void setUpProcessors(){
        for(int i = 0; i < processors.length; i++){
            processors[i] = new Processor();
        }
    }
    public void setUpProcesses(int MIN_PAGES, int MAX_PAGES, int MIN_LIFE, int MAX_LIFE){
        for(int i = 0; i < processes.length; i++){
            processes[i] = new Process(MAX_PAGES, MIN_PAGES, MAX_LIFE, MIN_LIFE);
            for(int j = 0; j < processes[i].getPages().length; j++){
                pages.add(processes[i].getPages()[j]);
            }
        }
    }
    public boolean isFullyTerminated(){
        boolean output = true;
//        System.out.println("Current Process States:");
        for(int i = 0; i < processes.length; i++){
//            System.out.println(i + " : " + processes[i].getProcessState());
            if(processes[i].getProcessState() != PROC_TERM) {
                output = false;
            }
        }
        return output;
    }
    public void processSchedule(int processorNum){
        /*
         Select one process to run for the current moment. And, update all involved processes.
        :return: No explicit returned value.
         */


        Processor currentProcessor = processors[processorNum];
        int numOfProcesses = processes.length;
        Process[] readyProcesses = new Process[numOfProcesses];
        int readyProccessCount = 0;



        if(currentProcessor.getInitial() == 1)
            return;


        if(PROCESS_ALG.equals("Random")){

            if(currentProcessor.getInitial() == -1){
                currentProcessor.setInitial(0);
                currentProcessor.setCurrentProcess(processes[(int) (Math.random() * numOfProcesses)]);
                currentProcessor.getCurrentProcess().setProcessState(PROC_RUN);
                return;
            }
            int currentProcessState = currentProcessor.getCurrentProcess().getProcessState();

            if(currentProcessState == PROC_RUN){
                //Check if there is an interrupt
            }else if(currentProcessState == PROC_TERM){
                currentProcessor.getCurrentProcess().setProcessState(PROC_TERM);
                for(int i = 0; i < numOfProcesses; i++){
                    if(processes[i].getProcessState() == PROC_READY){
                        readyProcesses[readyProccessCount] = processes[i];
                        readyProccessCount++;
                    }
                }
                currentProcessor.setCurrentProcess(readyProcesses[(int) (Math.random() * readyProccessCount)]);
                if(currentProcessor.getCurrentProcess() == null)
                    currentProcessor.setInitial(1);
            }else if(currentProcessState == PROC_READY){
                currentProcessor.getCurrentProcess().setProcessState(PROC_RUN);
            }else if(currentProcessState == PROC_BLOCK){
                for(int i = 0; i < numOfProcesses; i++){
                    if(processes[i].getProcessState() == PROC_READY){
                        readyProcesses[readyProccessCount] = processes[i];
                        readyProccessCount++;
                    }
                }
            }
            return;
        }else if(PROCESS_ALG.equals("SJF")){
            if(currentProcessor.getInitial() == -1){
                currentProcessor.setInitial(0);
                int minLife = Integer.MAX_VALUE;
                int position = 0;
                for(int i = 0; i < processes.length; i++){
                    if(processes[i].getCurrentLife() < minLife){
                        if(processes[i].getProcessState() == PROC_READY){
                            minLife = processes[i].getCurrentLife();
                            position = i;
                        }
                    }
                }
                currentProcessor.setCurrentProcess(processes[position]);
                currentProcessor.getCurrentProcess().setProcessState(PROC_RUN);
                return;
            }
            int currentProcessState = currentProcessor.getCurrentProcess().getProcessState();

            if(currentProcessState == PROC_RUN){
                //Check if there is an interrupt
            }else if(currentProcessState == PROC_TERM){
                currentProcessor.getCurrentProcess().setProcessState(PROC_TERM);
                int minLife = Integer.MAX_VALUE;
                int position = -1;
                for(int i = 0; i < numOfProcesses; i++){
                    if(processes[i].getProcessState() == PROC_READY){
                        if(processes[i].getCurrentLife() < minLife) {
                            minLife = processes[i].getCurrentLife();
                            position = i;
                        }
                    }
                }
                if(position == -1) {
                    currentProcessor.setInitial(1);
                    currentProcessor.setCurrentProcess(null);
                }else{
                    currentProcessor.setCurrentProcess(processes[position]);
                }
            }else if(currentProcessState == PROC_READY){
                currentProcessor.getCurrentProcess().setProcessState(PROC_RUN);
            }else if(currentProcessState == PROC_BLOCK){
                for(int i = 0; i < numOfProcesses; i++){
                    if(processes[i].getProcessState() == PROC_READY){
                        readyProcesses[readyProccessCount] = processes[i];
                        readyProccessCount++;
                    }
                }
            }
            return;
        }
        //Fallback
        currentProcessor.setCurrentProcess(processes[(int) (Math.random() * numOfProcesses)]);
    }

    public boolean accessPage(Page page, int accessType, int processNum){

        //Check if page is in memory and Check if any memory spaces are empty

        if(page == null){
            return false;
        }

        if(PAGING_ALG.equals("Random")){
            currentPageFaultOccured = true;
            for (int i = 0; i < pagesInMem.length; i++) {
                if (page == pagesInMem[i]) {
                    currentPageFaultOccured = false;
                    return currentPageFaultOccured;
                }
                if (pagesInMem[i] == null) {
                    pagesInMem[i] = page;
                    currentPageFaultOccured = false;
                    return currentPageFaultOccured;
                }
            }

        }else if(PAGING_ALG.equals("FIFO")){
            currentPageFaultOccured = true;
            for (int i = 0; i < queuePagesInMem.getPages().length; i++) {
                if (queuePagesInMem.containsPage(page)) {
                    currentPageFaultOccured = false;
                    return currentPageFaultOccured;
                }
                if (queuePagesInMem.containsPage(null)) {
                    queuePagesInMem.enQueue(page);
                    currentPageFaultOccured = false;
                }
            }
        }
        if (currentPageFaultOccured) {
            paging(page, processNum);
            pageFaultOccured = true;
        }
        page.accessPageType(accessType);
        return currentPageFaultOccured;
    }

    public void paging(Page page, int processNum){
        if(PAGING_ALG.equals("Random")){
            boolean occupiedPageSlot = true;
            ArrayList<Integer> freePageSlots = new ArrayList<Integer>();
            for(int i = 0; i < pagesInMem.length; i++){ //Run through pages in memory
                for(int j = 0; j < processors[processNum].getCurrentProcess().getPages().length; j++) { //Run through the processes pages and check if they are in memory
                    if(pagesInMem[i] == processors[processNum].getCurrentProcess().getPages()[j]) {
                        //for(int k = 0; k < currentPages.length; k++){ //If the page is not contained in the current process, check to see if it has been put in by another this round
                           // if(pagesInMem[i] != currentPages[k]){
                                occupiedPageSlot = false;
                           // }
                       // }
                    }
                }
                if(!occupiedPageSlot){
                    freePageSlots.add(i);
                }
            }
            int removedPageIndex = (int) Math.random() * freePageSlots.size();
            setPage(removedPageIndex, page);
            //currentPages[processNum] = page;
        }else if(PAGING_ALG.equals("FIFO")){
            queuePagesInMem.deQueue();
            queuePagesInMem.enQueue(page);
        }

    }
    public void setPage(int index, Page page){
        pagesInMem[index] = page;
    }

    public Page generateNextPage(int processorNum){
        if(!processors[processorNum].containsProcess()){
            return null;
        }
        Process currentProcess = processors[processorNum].getCurrentProcess();

        if(currentProcess.isNew()){
            int randomValue = (int) (Math.random() * currentProcess.getPages().length + currentProcess.getPages()[0].getID()) - 1;
            Page page = pages.get(randomValue);
            currentProcess.setLastPageAccessed(page);
            return page;
        }else{
            Page currentPage = currentProcess.getLastPageAccessed();


            double[][] transMap = processors[processorNum].getCurrentProcess().getPageTransMap();
            double randomValue = Math.random();

            int currentPageIDInMap = 0;

            for(int i = 0; i < currentProcess.getPages().length; i++){
                if(currentPage == currentProcess.getPages()[i]){
                    currentPageIDInMap = i;
                }
            }
//            int currentPageIDInMap = currentPage.getID() - currentProcess.getPages()[0].getID() - 1;


            for(int i = 0; i < transMap[currentPageIDInMap].length; i++){
                if(randomValue < transMap[currentPageIDInMap][i]){
                    Page page = currentProcess.getPages()[i];
                    currentProcess.setLastPageAccessed(page);
                    return page;
                }
            }
        }
        return currentProcess.getLastPageAccessed();
    }
    public int getCurrentAccessType(){
        return accessType;
    }

    public void clockInterrupt(){
        for(int p = 0; p < processors.length; p++){
            for(int i = 0; i < pages.size(); i++){
                pages.get(i).resetBit();
            }
        }
    }

    public void runProcesses(){
        for(int i = 0; i < processors.length; i++){
            if(processors[i].containsProcess())
                processors[i].runProcess();
        }
    }

    public Snapshot snapshot(){
        int[] currentLife = new int[processors.length];
        for(int i = 0; i < processors.length; i++){
            if(processors[i].containsProcess())
                currentLife[i] = processors[i].getCurrentProcess().getCurrentLife();
        }
        if(PAGING_ALG.equals("Random"))
            return new Snapshot(time, pageFaultOccured, processors, currentLife, pagesInMem);
        else if(PAGING_ALG.equals("FIFO")){
            return new Snapshot(time, pageFaultOccured, processors, currentLife, queuePagesInMem.getPages());
        }
        return new Snapshot(time, pageFaultOccured, processors, currentLife, pagesInMem);
    }

    public void incrementTime(){
        time++;
        pageFaultOccured = false;
    }
    public int getProcessorNum(){
        return processors.length;
    }
}
