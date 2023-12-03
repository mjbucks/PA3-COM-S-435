import java.io.*;
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
//        int index = getIndexOfDoc(doc);
        ArrayList<String> words = new ArrayList<>(Arrays.asList(doc.split(" ")));

        System.out.println(words);
        return Collections.frequency(words, term);
    }

    public int docFrequency(String term) {
        int numTimes = 0;

        for (ArrayList<String> doc : docs) {
            if (doc.contains(term)) {
                numTimes++;
            }
        }
        return numTimes;
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
        return Math.pow(termFrequency(t, d), 0.5) * Math.log((double) N /dictionary.get(t));
    }

    public double TPScore(String query, String doc) {

        return 0.0;
    }

    public double VSScore(String query, String doc) {

        return 0.0;
    }

    public double Relevance(String query, String doc) {

        return 0.0;
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

}
