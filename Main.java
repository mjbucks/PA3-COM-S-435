import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String folder = "C:\\Users\\hedgr_v6euno5\\OneDrive\\ISU Fall23\\COM S 435\\PA3\\testFiles";
//        String folder = "C:\\Users\\Maxwe\\Downloads\\testFiles\\testFiles";
        PositionalIndex pos = new PositionalIndex(folder);
//        System.out.println(pos.dictionary);

//        Enumeration<String> keys = pos.dictionary.keys();
// iterate over the keys
//        while (keys.hasMoreElements()) {
//            // get the next key
//            String key = keys.nextElement();
//            // print the key
//            System.out.println("Eric noob");
//            System.out.println(key + ": " + pos.dictionary.get(key).toString());
//        }
//        String query = "come had major league tracks";
//        ArrayList<String> docs = pos.topkDocs(query, 5);
//        System.out.println(docs);
//        String doc;
////        System.out.println(pos.dictionary);
//        for (int i = 0; i < pos.unprocessedDocs.length; i++) {
//            doc = pos.unprocessedDocs[i];
//            if (docs.contains(doc)) {
//                System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!  ");
//            }
//            System.out.println(doc + ": " + pos.Relevance(query, doc));
//        }



        /*
        QUERIES FOR THE REPORT
        run this and put the output into the report
         */
        String[] queries = {"had", "had come", "missed injury play", "argued decided in number", "had play bridgegrooms injury of"};
        int k = 10;
        ArrayList<String> topK;
        String doc;

        for (String q : queries) {
            System.out.println("Query: " + q);

            topK = pos.topkDocs(q, k);
            for (int d = 0; d < topK.size(); d++) {
                doc = topK.get(d);
                System.out.println((d + 1) + ") " + doc);
                System.out.println("TPScore: " + pos.TPScore(q, doc) + ", VSScore: " + pos.VSScore(q, doc) +
                        ", Relevance Score: " + pos.Relevance(q, doc));
            }
            System.out.println("-----------------------------------------------------------------------------\n");
        }
    }
}
