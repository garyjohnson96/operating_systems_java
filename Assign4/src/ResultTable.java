import java.util.ArrayList;
import java.util.HashMap;

public class ResultTable {

    private HashMap<Integer, Integer> results;

    public ResultTable() {
        results = new HashMap<>();
    }

    public synchronized void storeResult(int digit, int result) {
        results.put(digit, result);
    }

    public void printResults() {
        ArrayList<Integer> sortedResults = new ArrayList<>();
        if (results.isEmpty()) {
            System.out.println("No results.");
        } else {
            for (int key = 1; key <= results.size(); key++) {
                if (results.get(key) != null) {
                    sortedResults.add(results.get(key));
                }
            }
            System.out.print(".\n\n3.");
            for (Integer sortedResult : sortedResults) {
                System.out.print(sortedResult);
            }
        }
        results.clear();
    }
}
