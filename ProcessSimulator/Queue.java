
/*
1 May 2023
Liam Pitchford

Queue Object file: Used for FIFO pages in Memory




 */
public class Queue {

    private Page[] pages;
    private int head;
    private int tail;
    private int size;

    private boolean hasElement;
    private int minSize;
    public Queue(int length){
        pages = new Page[length];
        head = 0;
        tail = 0;
        size = length;
        hasElement = false;
        minSize = length;
    }
    public void enQueue(Page page){ // 1 run time
        hasElement = true;
        pages[head] = page;
        head = (head + 1) % size;
    }
    public Page deQueue(){ // 1 run time
        Page page = pages[tail];
        pages[tail] = null;
        tail = (tail + 1) % size;
        if(head == tail){
            hasElement = false;
        }
        return page;
    }
    public String toString(){ // n run time
        String s = "";
        int distance = Math.abs(head - tail);
        for(int i = 0; i < distance; i++){
            s += pages[(tail + i) % size];
        }
        return s;
    }
    public Page[] getPages(){
        return pages;
    }
    public boolean containsPage(Page page){
        for(int i = 0; i < pages.length; i++){
            if(page == pages[i]){
                return true;
            }
        }
        return false;
    }
}
