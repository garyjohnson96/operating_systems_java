import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import static java.util.Collections.shuffle;

public class TaskQueue {
    private int tasks;
    private int dequeueCount;
    private ArrayList<Integer> taskArray;
    private LinkedList<Integer> taskList;

    public TaskQueue(int t) {
        dequeueCount = 0;
        this.tasks = t;
        this.taskArray = new ArrayList<>();
        fillArray();
        shuffle(taskArray);
        this.taskList = new LinkedList<>(taskArray);
    }
    private void fillArray() {
        for (int i = 0; i < tasks; i++) {
            taskArray.add(i + 1);
        }
    }
    public synchronized Optional<Integer> dequeueTask() {
        if (taskList.isEmpty()) {
            return Optional.empty();
        }
        dequeueCount++;
        return Optional.of(taskList.remove());
    }
    public int getDequeueCount() {
        return dequeueCount;
    }
}
