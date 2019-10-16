import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

public class Moderator {
  public static void main(String[] args) throws Exception{
      Scanner scan = new Scanner(System.in);
      System.out.println("How many TwitterBots do you want to create? Enter an Integer: ");
      int numTwitterBots = scan.nextInt();

      TwitterBot[] TwitterBots = TwitterBot.TwitterBotGen(numTwitterBots);
      Tweet[] Tweets = myTweets.csvParser();
      Moderator.Report(TwitterBot.Assess(Moderator.Guess(numTwitterBots, TwitterBots, Tweets), TwitterBots));
  }

    public static String[] Guess(int numTwitterBots, TwitterBot[] TwitterBots, Tweet[] Tweets) throws Exception {
        Scanner scan1 = new Scanner(System.in);
        System.out.println("How many Tweets should we pass to the Bots, moderator? Enter an Integer: ");
        int numPassedTweets = scan1.nextInt();

        String[] GuessArray = new String[numTwitterBots];   // Created 2D array in order to parse over and assign values to sentiment/topic. Primary array
        for (int j=0; j<numTwitterBots; ++j) {              // accessed by moderator methods
            int[][] LikelihoodTracker =  new int[4][4];
                                                            //LikelihoodTracker[0][_] is AppleIntArray
                                                            //LikelihoodTracker[1][_] is GoogleIntArray
                                                            //LikelihoodTracker[2][_] is MicrosoftIntArray
                                                            //LikelihoodTracker[3][_] is TwitterIntArray
                                                            //LikelihoodTracker[_][0] is PositiveTracker
                                                            //LikelihoodTracker[_][1] is NeutralTracker
                                                            //LikelihoodTracker[_][2] is NegativeTracker
                                                            //LikelihoodTracker[_][3] is IrrelevantTracker

            String TopicGuess = "";
            String SentimentGuess = "";
            for(int i = 0; i<numPassedTweets; ++i) {
                Tweet randomTweet;
                Random rand = new Random();
                randomTweet = Tweets[rand.nextInt(Tweets.length)];
                Tweet GeneratedTweet = TwitterBots[j].TweetGen(randomTweet);
                switch (GeneratedTweet.getTweetTopic()) {         // For logical simplicity, we ended up using a switch method instead of a for-loop.
                    case "apple":                                 // Instead of loops, each combination of sentiment and topic were broken down
                        switch (GeneratedTweet.getSentiment()) {  // into cases
                            case "positive":
                                LikelihoodTracker[0][0] += 1;
                                break;
                            case "neutral":
                                LikelihoodTracker[0][1] += 1;
                                break;
                            case "negative":
                                LikelihoodTracker[0][2] += 1;
                                break;
                            case "irrelevant":
                                LikelihoodTracker[0][3] += 1;
                                break;
                        }
                        break;
                    case "google":
                        switch (GeneratedTweet.getSentiment()) {
                            case "positive":
                                LikelihoodTracker[1][0] += 1;
                                break;
                            case "neutral":
                                LikelihoodTracker[1][1] += 1;
                                break;
                            case "negative":
                                LikelihoodTracker[1][2] += 1;
                                break;
                            case "irrelevant":
                                LikelihoodTracker[1][3] += 1;
                                break;
                        }
                        break;
                    case "microsoft":
                        switch (GeneratedTweet.getSentiment()) {
                            case "positive":
                                LikelihoodTracker[2][0] += 1;
                                break;
                            case "neutral":
                                LikelihoodTracker[2][1] += 1;
                                break;
                            case "negative":
                                LikelihoodTracker[2][2] += 1;
                                break;
                            case "irrelevant":
                                LikelihoodTracker[2][3] += 1;
                                break;
                        }
                        break;
                    case "twitter":
                        switch (GeneratedTweet.getSentiment()) {
                            case "positive":
                                LikelihoodTracker[3][0] += 1;
                                break;
                            case "neutral":
                                LikelihoodTracker[3][1] += 1;
                                break;
                            case "negative":
                                LikelihoodTracker[3][2] += 1;
                                break;
                            case "irrelevant":
                                LikelihoodTracker[3][3] += 1;
                                break;
                        }
                }
            }

            ArrayList<Double>TopicProportions = new ArrayList<Double>(); //We sometimes used ArrayLists selectively (as opposed to arrays) for ease of use of methods such as ".indexof"
            for(int k = 0; k<4; ++k) {
                double maxForRowK;
                double sumForRowK;
                maxForRowK = Math.max(Math.max(Math.max(LikelihoodTracker[k][0], LikelihoodTracker[k][1]), LikelihoodTracker[k][2]), LikelihoodTracker[k][3]);
                // ^This finds the maximum value in the row, meaning the maximum Sentiment-count for this topic for said TwitterBot
                sumForRowK = (LikelihoodTracker[k][0] + LikelihoodTracker[k][1] + LikelihoodTracker[k][2] + LikelihoodTracker[k][3]);
                // ^This computes the sum of every Sentiment-count for this topic for said TwitterBot
                if (maxForRowK == 0 || sumForRowK == 0) {
                    TopicProportions.add(0.0);
                    // ^This prevents NaN values from arising due to zero-divisors
                }
                else {
                    TopicProportions.add(maxForRowK/sumForRowK);
                    // ^TopicProportions gives us a normalized value for the most common sentiment for comparison purposes
                }
            }
            double maxOfTopicProportions = Math.max(Math.max(Math.max(TopicProportions.get(0), TopicProportions.get(1)), TopicProportions.get(2)), TopicProportions.get(3));
            switch (TopicProportions.indexOf(maxOfTopicProportions)) {  //Finding the index of the maximum of the normalized values of most common sentiments gives the topic
                case 0:                                                 // that has the most consistent sentiment, proportionately, AKA the "TopicGuess" of Moderator for this Bot
                    TopicGuess = "apple";
                    break;
                case 1:
                    TopicGuess = "google";
                    break;
                case 2:
                    TopicGuess = "microsoft";
                    break;
                case 3:
                    TopicGuess = "twitter";
                    break;
            }
            ArrayList<Integer>GuessedTopicRow = new ArrayList<Integer>();  //Now that we have a GuessedTopic, we find the GuessedRow to find the
            for (int m = 0; m<4; ++m) {                                    // index of the found most proportionately common sentiment, found above.
                int GuessedTopicElement = LikelihoodTracker[TopicProportions.indexOf(maxOfTopicProportions)][m];
                GuessedTopicRow.add(GuessedTopicElement);
            }
            int count = 0;                              //This counter and for-loop are for locating the index of the most proportionately common sentiment, within the found TopicRow
            for (int n = 0; n<4; ++n) {
                if (GuessedTopicRow.get(n)==0) {
                    ++count;
                }
                else break;
            }
            switch (count) {                            //This switch on "count" is for turning the found index of the sentiment into the relevant string
                case 0:                                 // Although the Bots cannot have Sentiment.equals("irrelevant"), we left the possibility for the
                    SentimentGuess = "positive";        //  sake of future implementations that incorporate such.
                    break;
                case 1:
                    SentimentGuess = "neutral";
                    break;
                case 2:
                    SentimentGuess = "negative";
                    break;
                case 3:
                    SentimentGuess = "irrelevant";
                    break;
            }
            GuessArray[j] = (TopicGuess + "," + SentimentGuess);  //Adds the Guess for this TwitterBot to the Moderator's Guess array, to be loaded into the TritterBot.Assess method.
        }

        return GuessArray; //Array of Strings to be split and assessed by TwitterBot.Asses
    }

    public static void Report(double Assessment){
      System.out.println("Moderator correctly guessed %" + (Assessment*100) + " of the TwitterBots' single Topic and Sentiment pair.");
    }
}