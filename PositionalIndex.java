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
    Dictionary<String, ArrayList<String>> dictionary;
    int N;


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
        dictionary = new Dictionary<String, ArrayList<String>>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Enumeration<String> keys() {
                return null;
            }

            @Override
            public Enumeration<ArrayList<String>> elements() {
                return null;
            }

            @Override
            public ArrayList<String> get(Object key) {
                return null;
            }

            @Override
            public ArrayList<String> put(String key, ArrayList<String> value) {
                return null;
            }

            @Override
            public ArrayList<String> remove(Object key) {
                return null;
            }
        };
        fillDictionary();

    }

    public int termFrequency(String term, String doc) {
        int index = getIndexOfDoc(doc);

        return Collections.frequency(docs.get(index), term);
    }

    public int docFrequency(String term) {
        int numTimes = 0;

        for (int i = 0; i < docs.size(); i++){
            if(docs.get(i).contains(term)) {
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

                for(j = 0; j < docs.get(getIndexOfDoc(postingsForTerm.get(i))).size(); j++){
                    if (docs.get(getIndexOfDoc(postingsForTerm.get(i))).get(j).equals(t)){
                        indeces.add(j);
                    }
                }
                for(index = 0; index < indeces.size(); index++){
                    if (index != indeces.size()-1){
                        result = result + indeces.get(index).toString() + ",";
                    }
                    else {
                        result = result + indeces.get(index).toString();
                    }
                }
                if(i != postingsForTerm.size()-1){
                    result = result + ">" + ",";
                } else{
                    result = result + ">";
                }
            }
        }
        result = result + "]";

        return result;
    }

    public double weight(String t, String d) {
        return Math.pow(termFrequency(t, d), 0.5) * Math.log((double) N /dictionary.get(t).size());
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
            if (unprocessedDocs[i] == doc) {
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
