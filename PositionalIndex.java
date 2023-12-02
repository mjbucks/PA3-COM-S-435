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
        StringBuilder result = new StringBuilder("[");
        String docName;
        Enumeration<String> e = postings.keys();
        int termIndex;

        while (e.hasMoreElements()) {
            docName = e.nextElement();
            result.append("<").append(docName).append(" : ");
            for (termIndex = 0; termIndex < postings.get(t).get(docName).size() - 1; termIndex++) {
                result.append(postings.get(t).get(docName).get(termIndex)).append(", ");
            }
            result.append(postings.get(t).get(docName).get(termIndex + 1)).append(">, ");
        }
        result.deleteCharAt(result.lastIndexOf(","));
        result.append("]");

        return result.toString();

//        if (dictionary.get(t) == null) {
//            return "";
//        }
//
//        int termDocFreq = 0;
//        int termIndex;
//        String docName;
//        StringBuilder result = new StringBuilder("[");
//
//        for (int docIndex = 0; docIndex < docs.size(); docIndex++) {
//            if (termDocFreq == dictionary.get(t)) {
//                break;
//            }
//
//            if (docs.get(docIndex).contains(t)) {
//                docName = unprocessedDocs[docIndex];
//
//                termDocFreq++;
//            }
//
//
//
//        }
//
//
//
////        ArrayList<String> postingsForTerm = dictionary.get(t);
//
//        String docName = "";
//        //int i, j, index, docIndex;
//        System.out.println(postingsForTerm.size());
//        System.out.println(docs.size());
//
//        for (i = 0; i < postingsForTerm.size(); i++){
//            if (docs.get(getIndexOfDoc(postingsForTerm.get(i))).contains(t)){
//                docIndex = getIndexOfDoc(postingsForTerm.get(i));
//                docName = unprocessedDocs[docIndex];
//                ArrayList<Integer> indeces = new ArrayList<Integer>();
//                result.append("<");
//                result.append(docName).append(":");
//
//                for(j = 0; j < docs.get(getIndexOfDoc(postingsForTerm.get(i))).size(); j++){
//                    if (docs.get(getIndexOfDoc(postingsForTerm.get(i))).get(j).equals(t)){
//                        indeces.add(j);
//                    }
//                }
//                for(index = 0; index < indeces.size(); index++){
//                    if (index != indeces.size()-1){
//                        result.append(indeces.get(index).toString()).append(",");
//                    }
//                    else {
//                        result.append(indeces.get(index).toString());
//                    }
//                }
//                if(i != postingsForTerm.size()-1){
//                    result.append(">").append(",");
//                } else{
//                    result.append(">");
//                }
//            }
//        }
//        result.append("]");
//
//        return result.toString();
    }

    Hashtable<String, ArrayList<String>> postings (String t) {
        Hashtable<String, ArrayList<String>> posting = new Hashtable<>();


        return posting;
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
        String key;
        Enumeration<String> e = postings.keys();
        while (e.hasMoreElements()) {
            key = e.nextElement();
            dictionary.put(key, postings.get(key).size());
        }
    }

}
