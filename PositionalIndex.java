import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class PositionalIndex {

    public String folder;
    String[] unprocessedDocs;
    ArrayList<ArrayList<String>> docs;


    public PositionalIndex(String folder) throws FileNotFoundException {
        this.folder = folder;
        this.unprocessedDocs = allDocs();
        File file;
        for (int doc = 0; doc < unprocessedDocs.length; doc++) {
            file = new File(folder + unprocessedDocs[doc]);
            docs.add(preProcess(file));
        }

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

        return null;
    }

    public double weight(String t, String d) {

        return 0.0;
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

}
