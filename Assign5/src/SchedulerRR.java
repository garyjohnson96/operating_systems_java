import java.util.LinkedList;

/**
 * Round Robin Scheduler
 */
public class SchedulerRR extends SchedulerBase implements Scheduler {
    private Platform platform;  //Reference to the platform this scheduler is executing on
    private LinkedList<Process> processes;  //A list of the processes that need to be scheduled
    private int scheduleCount;  //Count of the number of processes that have been scheduled
    private int timeQuantum;    //The amount of cpu time that each process gets to execute
    private boolean timeQuantumComplete;    //Boolean for checking if a process has reached the time quantum

    /**
     * Constructor for Round Robin Scheduler
     * @param platform reference to the platform that executes the scheduler
     */
    public SchedulerRR(Platform platform, int timeQuantum) {
        this.platform = platform;
        this.timeQuantum = timeQuantum;
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
        } else if (this.processes.size() > 1 && (this.timeQuantumComplete || p.isExecutionComplete())) {
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
     *         Process has completed when either the process has reached the time quantum or completed its execution
     */
    @Override
    public Process update(Process cpu) {
        Process nextProcess = cpu;  //tracks the next process in the queue that should be scheduled
        boolean processComplete = false;    //boolean to check if the current process has completed
        this.timeQuantumComplete = false;

        if (!this.processes.isEmpty()) {
            if (cpu == null) {
                cpu = this.processes.get(0);
                nextProcess = cpu;
            }
            if (cpu.isExecutionComplete()) {
                this.platform.log("Process " + cpu.getName() + " execution complete");

                if (this.processes.size() > 1) {
                    nextProcess = this.processes.get(1);  //should be next process in queue
                } else {
                    nextProcess = null;
                }
                processComplete = true;
            }
            else if (this.isTimeQuantumComplete(cpu)){
                this.platform.log("Time quantum complete for process " + cpu.getName());

                Process newProcess = new Process(cpu.getName(), cpu.getStartTime(), cpu.getBurstTime() - this.timeQuantum, cpu.getTotalTime() - this.timeQuantum);
                cpu = newProcess;

                if (this.processes.size() > 1) {
                    nextProcess = this.processes.get(1);  //should be next process in queue
                } else {
                    nextProcess = newProcess;
                }

                this.timeQuantumComplete = true;
                processComplete = true;
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
     * @return true if current burst equals the time quantum, false otherwise
     */
    private boolean isTimeQuantumComplete(Process cpu) {
        return cpu.getElapsedBurst() == this.timeQuantum;
    }
}