import java.util.Optional;

public class WorkerThread implements Runnable {
    private TaskQueue tasks;
    private ResultTable results;

    public WorkerThread(TaskQueue queue, ResultTable table) {
        tasks = queue;
        results = table;
    }

    @Override
    public synchronized void run() {
        boolean done = false;
        while (!done) {
            try {
                Optional<Integer> digit = tasks.dequeueTask();
                if (digit.isPresent()) {
                    int result = new PiDigit().compute(digit.get());
                    results.storeResult(digit.get(), result);
                } else {
                    done = true;
                }
                if (tasks.getDequeueCount() % 10 == 0 && tasks.getDequeueCount() < 1000) {
                    System.out.flush();
                    System.out.print(".");
                    if (tasks.getDequeueCount() % 200 == 0) {
                        System.out.println();
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
        }
    }
}
