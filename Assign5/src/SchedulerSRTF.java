import java.util.LinkedList;

/**
 * Shortest Remaining Time First Scheduler
 */
public class SchedulerSRTF extends SchedulerBase implements Scheduler {
    private Platform platform;  //Reference to the platform this scheduler is executing on
    private LinkedList<Process> processes;  //A list of the processes that need to be scheduled
    private int scheduleCount;  //Count of the number of processes that have been scheduled
    private boolean preemptivelyRemoved;  //Boolean to check if a process has been preemptively removed

    /**
     * Constructor for Shortest Time Remaining First Scheduler
     * @param platform reference to the platform that executes the scheduler
     */
    public SchedulerSRTF(Platform platform) {
        this.platform = platform;
        this.processes = new LinkedList<>();
        this.scheduleCount = 1;
        this.preemptivelyRemoved = false;
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
        if (isRemainingTimeShorter(p)) {  //if the new process has less execution time remaining than the process at the head of the queue
            Process firstProcess = this.processes.get(0);
            this.platform.log("Preemptively removed: " + firstProcess.getName());
            this.platform.log("Scheduled: " + p.getName());
            this.preemptivelyRemoved = true;
            super.contextSwitches++;
        }
        else if (scheduleCount == 1) {
            this.platform.log("Scheduled: " + p.getName());
            super.contextSwitches++;
            scheduleCount++;
        }
        else if (p.isBurstComplete()) {
            if (this.processes.size() > 1) {
                Process nextProcess = this.processes.get(1);
                this.platform.log("Scheduled: " + nextProcess.getName());
                super.contextSwitches++;
            }
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
            if (this.preemptivelyRemoved) {
                if (this.processes.size() > 1) {
                    nextProcess = this.processes.get(1);  //should be next process in queue
                } else {
                    nextProcess = null;
                }

                this.preemptivelyRemoved = false;
                processComplete = true;
            }
            if (cpu.isBurstComplete()) {
                this.platform.log("Process " + cpu.getName() + " burst complete");

                sortProcesses();

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

    /**
     * @param cpu - the process currently running on the cpu
     * @return true if current process's remaining burst time is less than the head process's remaining burst time, false otherwise
     */
    private boolean isRemainingTimeShorter(Process cpu) {
        if (this.processes.size() > 1) {
            Process firstProcess = this.processes.get(0);

            if (!cpu.getName().equals(firstProcess.getName())) {    //if the current process is not the first process in the queue
                return cpu.getRemainingBurst() < firstProcess.getRemainingBurst();
            }
        }
        return false;
    }

    /**
     * Sorts the processes by process with shortest time remaining in the list first
     */
    private void sortProcesses() {
        int n = this.processes.size();

        for (int i = 0; i < n-1; i++) {
            int shortestRemainingTime = i;
            for (int j = shortestRemainingTime + 1; j < n; j++)
                if (this.processes.get(j).getRemainingBurst() < this.processes.get(shortestRemainingTime).getRemainingBurst()) {
                    shortestRemainingTime = j;
                }
            Process temp = this.processes.get(shortestRemainingTime);
            this.processes.set(shortestRemainingTime, this.processes.get(i));
            this.processes.set(i, temp);
        }
    }
}