import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        PositionalIndex pos = new PositionalIndex("C:\\Users\\Maxwe\\Downloads\\IR\\IR");

        System.out.println(pos.postingsList("team"));



    }


    //
}
