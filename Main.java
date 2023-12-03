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

        System.out.println(pos.VSScore("In Major League Baseball (MLB), the 3,000 hit club is the group of batters \n" +
                "who have collected 3,000 or more regular-season hits in their careers. \n" +
                "Cap Anson was the first to join the club on July 18, 1897, although his \n" +
                "precise career hit total is unclear.[a] Two players—Nap Lajoie and Honus \n" +
                "Wagner—reached 3,000 hits during the 1914 season. Ty Cobb became the club's \n" +
                "fourth member in 1921 and became the first player in MLB history to reach \n" +
                "4,000 hits in 1927, ultimately finishing his career with more than 4,100.[7][b] \n" +
                "Cobb, also MLB's all-time career batting average leader, remained the MLB \n" +
                "hit leader until September 11, 1985, when Pete Rose collected his 4,192nd \n" +
                "hit.[11] Rose, the current record holder, finished his career with 4,256 \n" +
                "hits, an achievement that on its own would have qualified him for the Hall \n" +
                "of Fame had Major League Baseball not banned him for life due to Rose's \n" +
                "having gambled on games as a manager. Roberto Clemente's career ended with \n" +
                "precisely 3,000 hits, reaching the mark in the last at bat of his career.[12][13][c] \n" +
                "Ichiro Suzuki is the most recent player to reach the milestone, achieving \n" +
                "the feat on August 7, 2016.[16] In total, 30 players have reached the 3,000 \n" +
                "hit mark in MLB history. Of these, 15 were right-handed batters, 13 were \n" +
                "left-handed, and two were switch hitters, meaning they could bat from either \n" +
                "side of the plate. Ten of these players have played for only one major \n" +
                "league team. Five players—Hank Aaron, Willie Mays, Eddie Murray, Rafael \n" +
                "Palmeiro, and Alex Rodriguez—are also members of the 500 home run club. \n" +
                "At .367, Cobb holds the highest career batting average among club members, \n" +
                "while Cal Ripken Jr. holds the lowest at .276. Alex Rodriguez, Derek Jeter, \n" +
                "and Wade Boggs are the only players to hit a home run for their 3,000th \n" +
                "hit and Paul Molitor and Ichiro Suzuki are the only players to hit a triple \n" +
                "for their 3,000th; all others hit a single or double. Craig Biggio was \n" +
                "thrown out at second base attempting to stretch his 3,000th hit, a single, \n" +
                "into a double.[17] Biggio and Jeter are the only players to join the club \n" +
                "in a game where they had five hits; Jeter reached base safely in all of \n" +
                "his at bats.[18] Baseball writer Josh Pahigian wrote that membership in \n" +
                "the club has been \"long considered the greatest measure of superior bat \n" +
                "handling.\"[19] Reaching 3,000 hits is often described as a guarantee of \n" +
                "eventual entry into the Baseball Hall of Fame.[20][21][22] All eligible \n" +
                "club members, with the exception of Palmeiro, have been elected to the \n" +
                "Hall, and since 1962 all club members who have been inducted were elected \n" +
                "on the first ballot, except for Biggio. Rose is ineligible for the Hall \n" +
                "of Fame because he was permanently banned from baseball in 1989.[23][24] \n" +
                "After 4 years on the ballot, Palmeiro failed to be named on 5% of ballots \n" +
                "in 2014 and his name will be off the ballot for future elections.[25] The \n" +
                "only active player on this list is Suzuki. Italics denotes active player \n", "3,000_hit_club.txt"));



    }


    //
}
