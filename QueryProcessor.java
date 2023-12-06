import java.io.FileNotFoundException;
import java.util.*;

public class QueryProcessor {

    PositionalIndex pos;
    public QueryProcessor(String folder) throws FileNotFoundException {
        pos = new PositionalIndex(folder);
    }

    ArrayList<String> topkDocs(String query, int k) {
        String doc;
        HashMap<String, Double> docs = new HashMap<>();

        for (int docIndex = 0; docIndex < pos.unprocessedDocs.length; docIndex++) {
            doc = pos.unprocessedDocs[docIndex];
            docs.put(doc, pos.Relevance(query, doc));
        }

        PriorityQueue<Map.Entry<String, Double>> queue = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );

        for (Map.Entry<String, Double> entry : docs.entrySet()) {
            queue.offer(entry);
            if (queue.size() > k) {
                queue.poll();  // remove the document with the smallest relevance score
            }
        }

        ArrayList<String> topKDocs = new ArrayList<>();
        while (!queue.isEmpty()) {
            topKDocs.add(queue.poll().getKey());
        }
        Collections.reverse(topKDocs);

        return topKDocs;
    }

}
