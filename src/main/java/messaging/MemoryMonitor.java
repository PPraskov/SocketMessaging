package messaging;

public class MemoryMonitor extends Thread {

    private long heapMaxMemory;

    public MemoryMonitor(){
      this.setDaemon(true);
      this.setPriority(10);
      this.heapMaxMemory = Runtime.getRuntime().maxMemory();
    }

    @Override
    public void run() {
        while (true){
            while (((this.heapMaxMemory - getCurrentMemory())/this.heapMaxMemory)>0.7){
                System.gc();
            }
        }
    }

    private long getCurrentMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
