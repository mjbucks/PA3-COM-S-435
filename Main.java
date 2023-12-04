import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;

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

        System.out.println(pos.VSScore("Major League Baseball", "3,000_hit_club.txt"));
//        System.out.println(pos.dictionary);

    }


    //
}
