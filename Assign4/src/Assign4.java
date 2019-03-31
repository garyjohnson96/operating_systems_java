public class Assign4 {
    public static void main(String[] args) {
        TaskQueue tasks = new TaskQueue(1000);
        ResultTable results = new ResultTable();

        try {
            Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];
            long startTime = System.currentTimeMillis();
            for (int thread = 0; thread < threads.length; thread++) {
                threads[thread] = new Thread(new WorkerThread(tasks, results));
                threads[thread].start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
            long endTime = System.currentTimeMillis();
            long runDuration = endTime - startTime;
            results.printResults();
            System.out.printf("\n\nPi Computation took %d milliseconds.\n\n", runDuration);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
