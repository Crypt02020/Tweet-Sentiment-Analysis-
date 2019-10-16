import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
//Assigns numeric value to each comma separated value in the CSV, numbers correspond to Topic, Sentiment, TweetID, TweetDate, and TweetText.
//Creates Mytweet[i] where i is a tweet containing the fields Topic, Sentiment, TweetID, TweetDate, and finally text contained in tweet
public class myTweets {
    public static Tweet[] csvParser() throws Exception{
        java.io.File file = new java.io.File("full-corpus.csv");
        Scanner input = new Scanner(file, "UTF-8");

        Tweet[] myTweets = new Tweet[5113];
              for(int i = 0; i<5113; ++i) {
                  String line = input.nextLine();
                  String[] fields = line.split("\",\"");
                  String Topic = fields[0].substring(1);
                  String Sentiment = fields[1];
                  String TweetID = fields[2];
                  String TweetDate = fields[3];
                  String TweetText = fields[4].substring(0, (fields[4].length()) - 1);

                  myTweets[i] = (new Tweet(Sentiment, TweetID, Topic, TweetText));
          }
        return(myTweets);
    }
}
