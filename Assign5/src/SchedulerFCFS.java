import java.util.LinkedList;

/**
 * First Come, First Serve Scheduler
 */
public class SchedulerFCFS extends SchedulerBase implements Scheduler {
    private Platform platform;  //Reference to the platform this scheduler is executing on
    private LinkedList<Process> processes;  //A list of the processes that need to be scheduled
    private int scheduleCount;  //Count of the number of processes that have been scheduled

    /**
     * Constructor for First Come, First Serve Scheduler
     * @param platform reference to the platform that executes the scheduler
     */
    SchedulerFCFS(Platform platform) {
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
     * Used to notify the scheduler a new process has just entered the ready state.
     */
    @Override
    public void notifyNewProcess(Process p) {
        if (!p.isExecutionComplete()) {  //add process to queue if it hasn't finished executing
            this.processes.add(p);
        }
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
                this.processes.remove();
                super.contextSwitches++;
            }
        }
        return nextProcess;
    }
}
