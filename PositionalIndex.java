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
    int N;

    Dictionary<String, ArrayList<String>> dictionary;


    public PositionalIndex(String folder) throws FileNotFoundException {
        this.folder = folder;
        this.unprocessedDocs = allDocs();
        this.N = unprocessedDocs.length;
        File file;
        for (String unprocessedDoc : unprocessedDocs) {
            file = new File(folder + unprocessedDoc);
            docs.add(preProcess(file));
        }
        fillDictionary();

    }

    public int termFrequency(String term, String doc) {
        int index = getIndexOfDoc(doc);

        return Collections.frequency(docs.get(index), term);
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
        if (dictionary.get(t) == null) {
            return "";
        }
        ArrayList<String> postingsForTerm = dictionary.get(t);
        String result = "[";

        String docName = "";
        int i, j, index;
        for (i = 0; i < postingsForTerm.size(); i++){
            if (docs.get(i).contains(t)){
                docName = unprocessedDocs[i];
                ArrayList<Integer> indeces = new ArrayList<Integer>();
                result = result + "<";
                result = result + docName + ":";

        return null;
    }

    public double weight(String t, String d) {
        return Math.pow(termFrequency(t, d), 0.5) * Math.log(N/);;
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
            word = word.replaceAll("[,\\[\\]\"\'{}:;()\\?]", "");
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
        ArrayList<String> termDocuments;
        for(int i = 0; i < docs.size(); i++){
            ArrayList<String> currTerms = docs.get(i);
            for (String currTerm : currTerms) {
                if ((dictionary.get(currTerm) == null)) {
                    termDocuments = new ArrayList<String>();
                    termDocuments.add(unprocessedDocs[i]);
                    dictionary.put(currTerm, termDocuments);
                } else {
                    termDocuments = dictionary.get(currTerm);
                    termDocuments.add(unprocessedDocs[i]);
                    dictionary.put(currTerm, termDocuments);
                }
            }
        }
    }

}
