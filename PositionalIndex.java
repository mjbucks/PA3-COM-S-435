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
        dictionary = new Hashtable<String, Integer>();
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
        int termDocFreq = 0;

        for (int i = 0; i < docs.size(); i++) {
            if (termDocFreq == dictionary.get(t)) {
                break;
            }




        }



        ArrayList<String> postingsForTerm = dictionary.get(t);
        StringBuilder result = new StringBuilder("[");

        String docName = "";
        int i, j, index, docIndex;
        System.out.println(postingsForTerm.size());
        System.out.println(docs.size());

        for (i = 0; i < postingsForTerm.size(); i++){
            if (docs.get(getIndexOfDoc(postingsForTerm.get(i))).contains(t)){
                docIndex = getIndexOfDoc(postingsForTerm.get(i));
                docName = unprocessedDocs[docIndex];
                ArrayList<Integer> indeces = new ArrayList<Integer>();
                result.append("<");
                result.append(docName).append(":");

                for(j = 0; j < docs.get(getIndexOfDoc(postingsForTerm.get(i))).size(); j++){
                    if (docs.get(getIndexOfDoc(postingsForTerm.get(i))).get(j).equals(t)){
                        indeces.add(j);
                    }
                }
                for(index = 0; index < indeces.size(); index++){
                    if (index != indeces.size()-1){
                        result.append(indeces.get(index).toString()).append(",");
                    }
                    else {
                        result.append(indeces.get(index).toString());
                    }
                }
                if(i != postingsForTerm.size()-1){
                    result.append(">").append(",");
                } else{
                    result.append(">");
                }
            }
        }
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
            if (unprocessedDocs[i] == doc) {
                return i;
            }
        }
        return -1;
    }

    public void fillDictionary() throws FileNotFoundException {
        Integer frequency;
        Set<String> currTerms;

        ArrayList<String> termDocuments;
        for (ArrayList<String> doc : docs) {
            currTerms = new HashSet<>(doc);
            for (String currTerm : currTerms) {
                frequency = dictionary.get(currTerm);
                if (frequency == null) {
                    dictionary.put(currTerm, 1);
                }
                else {
                    dictionary.put(currTerm, frequency + 1);
                }
            }
        }
    }

}
