import java.util.ArrayList;

/*
1 May 2023
Liam Pitchford

Runner file: Create a simulation and then run it


 */




public class Runner {
    public static void main(String[] args){
        Simulation simulation = setUp();
        ArrayList<Snapshot> snapshots = runSimulation(simulation);
        for(int i = 0; i < snapshots.size(); i++){
            snapshots.get(i).outputSnapshot();
        }
    }
    public static Simulation setUp(){

        final int NUM_PROCESSORS = 2;
        final int NUM_PROCESSES = 7;
        final int MAX_MEM_PAGES = 5;
        final int MAX_PAGES = 5;
        final int MIN_PAGES = 3;
        final int MAX_LIFE = 50;
        final int MIN_LIFE = 10;

        final String PROCESS_ALG = "Random"; //Currently implemented "Random", "SJF"
        final String PAGING_ALG = "FIFO"; //Currently Implemented "Random", "FIFO"

        return new Simulation(NUM_PROCESSORS, MAX_MEM_PAGES, NUM_PROCESSES, MAX_PAGES, MIN_PAGES, MAX_LIFE, MIN_LIFE, PROCESS_ALG, PAGING_ALG);
    }
    public static ArrayList<Snapshot> runSimulation(Simulation simulation) {
        ArrayList<Snapshot> snapshots = new ArrayList<Snapshot>();
        while (true) {
            if(simulation.isFullyTerminated()){
                return snapshots;
            }
            for(int i = 0; i < simulation.getProcessorNum(); i++){
                simulation.processSchedule(i);
                Page nextPage = simulation.generateNextPage(i);
                int accessType = simulation.getCurrentAccessType();
                simulation.accessPage(nextPage, accessType, i);
            }
            simulation.runProcesses();
            snapshots.add(simulation.snapshot());
            simulation.clockInterrupt();
            simulation.incrementTime();
        }
    }

}
