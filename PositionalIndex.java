import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PositionalIndex {

    public String folder;
    String[] unprocessedDocs;
    ArrayList<ArrayList<String>> docs;
    Dictionary<String, Integer> dictionary;
    int N;
    Hashtable<String, Hashtable<String, ArrayList<Integer>>> postings;


    public PositionalIndex(String folder) throws FileNotFoundException {
        this.folder = folder;
        this.unprocessedDocs = allDocs();
        this.N = unprocessedDocs.length;
        File file;
        docs = new ArrayList<ArrayList<String>>();
        for (int doc = 0; doc < unprocessedDocs.length; doc++) {
            file = new File(folder + "\\" + unprocessedDocs[doc]);
            docs.add(preProcess(file));
        }
        postings = new Hashtable<String, Hashtable<String, ArrayList<Integer>>>();
        createPostings();

        dictionary = new Hashtable<String, Integer>();
        fillDictionary();

    }

    public void createPostings() {
        int docIndex, termIndex;
        String term, docName;

        for (docIndex = 0; docIndex < docs.size(); docIndex++) {
            docName = unprocessedDocs[docIndex];

            for (termIndex = 0; termIndex < docs.get(docIndex).size(); termIndex++){
                term = docs.get(docIndex).get(termIndex);
                if (postings.get(term) == null){
                    postings.put(term, new Hashtable<>());
                    postings.get(term).put(docName, new ArrayList<Integer>());
                }
                else if(postings.get(term).get(docName) == null) {
                    postings.get(term).put(docName, new ArrayList<Integer>());
                }
                postings.get(term).get(docName).add(termIndex);
            }
        }
    }

    public int termFrequency(String term, String doc) {
        doc = doc.toLowerCase();
        ArrayList<String> words = new ArrayList<>(Arrays.asList(doc.split(" ")));
        return Collections.frequency(words, term);
    }

    public String postingsList(String t) {
        StringBuilder result = new StringBuilder("[");
        String docName;
        Enumeration<String> e = postings.get(t).keys();
        int termIndex;

        while (e.hasMoreElements()) {
            docName = e.nextElement();
            result.append("<").append(docName).append(":");
            for (termIndex = 0; termIndex < postings.get(t).get(docName).size(); termIndex++) {
                result.append(postings.get(t).get(docName).get(termIndex)).append(",");
            }
            result.deleteCharAt(result.lastIndexOf(","));
            result.append(">,");
        }
        result.deleteCharAt(result.lastIndexOf(","));
        result.append("]");

        return result.toString();
    }

    public double weight(String t, String d) {
        if (postings.get(t).get(d) == null) {
            return 0;
        }
        return Math.sqrt(postings.get(t).get(d).size()) * Math.log((double) N /dictionary.get(t));
    }

    public double TPScore(String query, String doc) {
        String[] qWords = query.split(" ");

        if(qWords.length == 0){
            return 0.0;
        }
        
        double sum = 0;
        
        for(int l = 0; l < qWords.length - 1; l++){
            sum = sum + distd(qWords[l], qWords[l+1], doc);
        }

        return ((double) qWords.length)/sum;
    }

    public double VSScore(String query, String doc) {
        ArrayList<Double> queryVector = new ArrayList<>();
        ArrayList<Double> docVector = new ArrayList<>();
        Set<String> termsSet = postings.keySet();
        ArrayList<String> terms = new ArrayList<>(termsSet);
        double dotProduct = 0;
        double magnitudeA = 0;
        double magnitudeB = 0;

        for (String term : terms) {
            queryVector.add((double) termFrequency(term, query));
            docVector.add(weight(term, doc));
        }

        for (int i = 0; i < terms.size(); i++) {
            dotProduct += queryVector.get(i) + docVector.get(i);
            magnitudeA += Math.pow(queryVector.get(i), 2);
            magnitudeB += Math.pow(docVector.get(i), 2);
        }

        magnitudeA = Math.sqrt(magnitudeA);
        magnitudeB = Math.sqrt(magnitudeB);

        return dotProduct / (magnitudeA * magnitudeB);
    }

    public double Relevance(String query, String doc) {
        return 0.6 * TPScore(query, doc) + 0.4 * VSScore(query, doc);
    }

    ArrayList<String> topkDocs(String query, int k) {
        String doc;
        HashMap<String, Double> docs = new HashMap<>();

        for (int docIndex = 0; docIndex < unprocessedDocs.length; docIndex++) {
            doc = unprocessedDocs[docIndex];
            docs.put(doc, Relevance(query, doc));
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

    public ArrayList<String> preProcess(File file) throws FileNotFoundException {
        ArrayList<String> document = new ArrayList<String>();

        Scanner scan = new Scanner(new BufferedReader(new FileReader(file)));

        while (scan.hasNext()){
            String word = scan.next();
            word = word.toLowerCase();
            word = word.replaceAll("[.,\\[\\]\"\'{}:;()\\?]", "");
            if (!word.equals("are") && !word.equals("the") && !word.equals("is")){
                document.add(word);
            }
        }
        return document;
    }

    public String[] allDocs() {
        try {
            Path folderPath = Paths.get(folder);
            long count = Files.list(folderPath).count();
            String[] allDocs = new String[(int) count];
            int i = 0;
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
                for (Path path : directoryStream) {
                    if (Files.isRegularFile(path)) {
                        allDocs[i] = String.valueOf(path.getFileName());
                        i++;
                    }
                }
            }
            return allDocs;
        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
        }
        return null;
    }

    public int getIndexOfDoc(String doc) {
        for (int i = 0; i < unprocessedDocs.length; i++) {
            if (unprocessedDocs[i].equals(doc)) {
                return i;
            }
        }
        return -1;
    }

    public void fillDictionary() throws FileNotFoundException {
        String key;
        Enumeration<String> e = postings.keys();
        while (e.hasMoreElements()) {
            key = e.nextElement();
            dictionary.put(key, postings.get(key).size());
        }
    }




    // This method returns the distance between two terms in a document[^1^][1]
    public double distd(String term1, String term2, String doc) {
        int index = getIndexOfDoc(doc);
        ArrayList<String> document = docs.get(index);
        int pos1 = -1, pos2 = -1;
        double minDist = Double.MAX_VALUE;

        for (int i = 0; i < document.size(); i++) {
            if (document.get(i).equals(term1)) {
                pos1 = i;
            }
            if (document.get(i).equals(term2)) {
                pos2 = i;
            }
            if (pos1 != -1 && pos2 != -1 && pos1 <= pos2) {
                minDist = Math.min(minDist, pos2 - pos1);
            }
        }
        if (pos1 == -1 || pos2 == -1) {
            return 17;
        }
        return Math.min(minDist, 17);
    }


    // Please note that this is a simple implementation and may not cover all edge cases.
    // For example, it does not handle cases where the terms are not found in the document, or where the document is empty.
    // You may need to add additional error checking depending on your specific requirements.
    // Also, this method assumes that the document is a single string where words are separated by spaces.
    // If your document structure is different, you may need to adjust the method accordingly.


}
