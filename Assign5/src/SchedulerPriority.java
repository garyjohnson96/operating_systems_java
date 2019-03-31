import java.util.LinkedList;

/**
 * Priority Scheduler
 */
public class SchedulerPriority extends SchedulerBase implements Scheduler {
    private Platform platform;  //Reference to the platform this scheduler is executing on
    private LinkedList<Process> processes;  //A list of the processes that need to be scheduled
    private int scheduleCount;  //Count of the number of processes that have been scheduled

    /**
     * Constructor for Priority Scheduler
     * @param platform reference to the platform that executes the scheduler
     */
    public SchedulerPriority(Platform platform) {
        this.platform = platform;
        this.processes = new LinkedList<>();
        this.scheduleCount = 1;
    }

    /**
     * Getter for the number of context switches that occur during this simulation.
     * @return The count of context switches that occurred.
     */
    @Override
    public int getNumberOfContextSwitches() {
        return super.contextSwitches;
    }

    /**
     * Used to add a new process to the scheduler.
     */
    @Override
    public void notifyNewProcess(Process p) {
        //add new process ordered by highest priority
        if (!p.isExecutionComplete()) {  //add process to queue if it hasn't finished executing
            this.processes.add(p);
            sortProcesses();
        }
    }

    /**
     * Update the scheduling algorithm for a single CPU.
     * @return Reference to the process that is executing on the CPU; result might be null
     *         if no process available for scheduling.
     *         Return the next process in the queue if the current process has completed.
     *         Process has completed when either the process has completed its burst or completed its execution
     */
    @Override
    public Process update(Process cpu) {
        Process nextProcess = cpu;  //tracks the next process in the queue that should be scheduled
        boolean processComplete = false;    //boolean to check if the current process has completed

        if (!this.processes.isEmpty()) {
            if (cpu == null) {
                cpu = this.processes.get(0);
                nextProcess = cpu;
                scheduleProcess(cpu);
            }
            if (cpu.isBurstComplete()) {
                this.platform.log("Process " + cpu.getName() + " burst complete");
                if (this.processes.size() > 1) {
                    nextProcess = this.processes.get(1);  //should be next process in queue
                } else {
                    nextProcess = null;
                }
                processComplete = true;
            }
            if (cpu.isExecutionComplete()) {
                this.platform.log("Process " + cpu.getName() + " execution complete");
            }
            if (processComplete) {
                notifyNewProcess(cpu);
                scheduleProcess(cpu);
                this.processes.remove();
                super.contextSwitches++;
            }
        }
        return nextProcess;
    }

    /**
     * Works in conjunction with notifyNewProcess to separate adding new processes from reporting a newly scheduled process.
     * If they were together, the scheduler would schedule the wrong process first.
     * @param p - the next process to be scheduled.
     */
    private void scheduleProcess(Process p) {
        if (scheduleCount == 1) {
            this.platform.log("Scheduled: " + p.getName());
            super.contextSwitches++;
            scheduleCount++;
        } else if (this.processes.size() > 1 && p.isBurstComplete()) {
            Process nextProcess = this.processes.get(1);
            this.platform.log("Scheduled: " + nextProcess.getName());
            super.contextSwitches++;
        }
    }

    /**
     * Sorts the processes by highest priority in the list first
     */
    private void sortProcesses() {
        //sorts this.processes by highest priority first
        int n = this.processes.size();

        for (int i = 0; i < n-1; i++) {
            int highestPriority = i;
            for (int j = highestPriority + 1; j < n; j++)
                if (this.processes.get(j).getPriority() < this.processes.get(highestPriority).getPriority()) {
                    highestPriority = j;
                }
            Process temp = this.processes.get(highestPriority);
            this.processes.set(highestPriority, this.processes.get(i));
            this.processes.set(i, temp);
        }
    }
}
