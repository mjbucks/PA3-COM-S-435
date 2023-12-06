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

    // This method returns the distance between two terms in a document[^1^][1]
    public double distd(String term1, String term2, String doc) {
        double minDist = Double.MAX_VALUE;
        int dist;

        /*
         If neither of the terms appear on the document or only one term appear in the document
         or if the terms don't exist in any docs
        */
        if (postings.get(term1) == null || postings.get(term2) == null ||
                postings.get(term1).get(doc) == null || postings.get(term2).get(doc) == null) {
            return 17;
        }

        ArrayList<Integer> p = postings.get(term1).get(doc);
        ArrayList<Integer> r = postings.get(term2).get(doc);

        int l = p.size();
        int k = r.size();
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < l; j++) {
                if (r.get(i) > p.get(j)) {
                    dist = r.get(i) - p.get(j);
                }
                else {
                    dist = 17;
                }

                if (dist < minDist) {
                    minDist = dist;
                }
            }
        }

//        if (pos1 == -1 || pos2 == -1) {
//            return 17;
//        }
        return Math.min(minDist, 17);
    }

    public double TPScore(String query, String doc) {

        ArrayList<String> qWords = queryPreProcess(query);
        int querySize = qWords.size();

        double sum = 0;

        if (querySize <= 1) {
            return 0.0;
        }

        for (int l = 0; l < querySize - 1; l++){
            sum = sum + distd(qWords.get(l), qWords.get(l + 1), doc);
        }

        return ((double) querySize)/sum;
    }

    public double weight(String t, String d) {
        if (postings.get(t).get(d) == null) {
            return 0;
        }
        return Math.sqrt(postings.get(t).get(d).size()) * Math.log((double) N /dictionary.get(t));
    }


    public double VSScore(String query, String doc) {
        ArrayList<Double> queryVector = new ArrayList<>();
        ArrayList<Double> docVector = new ArrayList<>();
        Set<String> termsSet = postings.keySet();
        ArrayList<String> terms = new ArrayList<>(termsSet);
        double dotProduct = 0;
        double magnitudeA = 0;
        double magnitudeB = 0;

        ArrayList<String> termsInQuery = queryPreProcess(query);

        for (String term : terms) {
            queryVector.add((double) Collections.frequency(termsInQuery, term));
            docVector.add(weight(term, doc));
        }

        for (int i = 0; i < terms.size(); i++) {
            dotProduct += queryVector.get(i) * docVector.get(i);
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

    public void fillDictionary() throws FileNotFoundException {
        String key;
        Enumeration<String> e = postings.keys();
        while (e.hasMoreElements()) {
            key = e.nextElement();
            dictionary.put(key, postings.get(key).size());
        }
    }

    public ArrayList<String> queryPreProcess (String query) {
        query = query.toLowerCase();
        return new ArrayList<>(Arrays.asList(query.split(" ")));
    }


    // Please note that this is a simple implementation and may not cover all edge cases.
    // For example, it does not handle cases where the terms are not found in the document, or where the document is empty.
    // You may need to add additional error checking depending on your specific requirements.
    // Also, this method assumes that the document is a single string where words are separated by spaces.
    // If your document structure is different, you may need to adjust the method accordingly.


}
