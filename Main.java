import java.io.FileNotFoundException;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        PositionalIndex pos = new PositionalIndex("C:\\Users\\Maxwe\\Downloads\\testFiles\\testFiles");

//        Enumeration<String> keys = pos.dictionary.keys();
// iterate over the keys
//        while (keys.hasMoreElements()) {
//            // get the next key
//            String key = keys.nextElement();
//            // print the key
//            System.out.println("Eric noob");
//            System.out.println(key + ": " + pos.dictionary.get(key).toString());
//        }

        System.out.println(pos.postingsList("team"));



    }


    //
}
