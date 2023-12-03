import java.io.FileNotFoundException;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String folder = "C:\\Users\\hedgr_v6euno5\\OneDrive\\ISU Fall23\\COM S 435\\PA3\\testFiles";
//        String folder = "C:\\Users\\Maxwe\\Downloads\\testFiles\\testFiles";
        PositionalIndex pos = new PositionalIndex(folder);

//        Enumeration<String> keys = pos.dictionary.keys();
// iterate over the keys
//        while (keys.hasMoreElements()) {
//            // get the next key
//            String key = keys.nextElement();
//            // print the key
//            System.out.println("Eric noob");
//            System.out.println(key + ": " + pos.dictionary.get(key).toString());
//        }

        System.out.println(pos.postingsList("had"));



    }


    //
}
