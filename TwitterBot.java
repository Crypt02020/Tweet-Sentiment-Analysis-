import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.Scanner;

//Twitterbots define by topic and sentiment that will dictate how they respond to tweets.
// Public class Twitterbot defines the fields, followed by method that sets fields for twitterbot

public class TwitterBot {
	public String topic;
	public String sentiment;

	public TwitterBot(String sentiment1, String topic1) {
		this.topic = topic1;
		this.sentiment = sentiment1;
	}

	//Method that takes int n and returns array[n] of TwitterBots.
   //Creates lists of sentiments and topics, randomly picks one of those, picks tweet from mytweets based on randomly selected sentiment/topic
	public static TwitterBot[] TwitterBotGen(int n) throws Exception {
		TwitterBot[] Twitterbaby = new TwitterBot[n];
		List<String> Sentiments = Arrays.asList("positive", "neutral", "negative");
		List<String> Topics = Arrays.asList("apple", "microsoft", "google", "twitter");
		for (int i = 0; i < n; ++i) {
			Random rand = new Random();
			String randomElement = Sentiments.get(rand.nextInt(Sentiments.size()));
			Random rand2 = new Random();
			String randomElement2 = Topics.get(rand2.nextInt(Topics.size()));
			Twitterbaby[i] = new TwitterBot(randomElement, randomElement2);
		}
		return(Twitterbaby);
	}

	//Takes a tweet and then returns a tweet that reflects the TwitterBot's topic and sentiment (their topic but that are sentimentally irrelevant -- to confuse the moderator).
	//Generates tweet based on parameters set in Tweet.java. It allows twitterbot to pull tweets from mytweets that match the topic/sentiment of the twitter-
	//bot creating the tweet. Randomly iterates through tweets in order to avoid repeating results over multiple iterations.
	public Tweet TweetGen(Tweet baby) throws Exception{
		Tweet[] Tweets = myTweets.csvParser();
		Tweet randomTweet = new Tweet("","","","");
		if (baby.getTweetTopic().equals(this.topic)) {
			while((!randomTweet.getTweetTopic().equals(this.topic)) || !(randomTweet.getSentiment().equals(this.sentiment)) || randomTweet.getSentiment().equals("Irrelevant")) {
				Random rand = new Random();
				randomTweet = Tweets[rand.nextInt(Tweets.length)];
			}
			return randomTweet;
		}
		else {
			while((!randomTweet.getTweetTopic().equals(baby.getTweetTopic()))) {
				Random rand = new Random();
				randomTweet = Tweets[rand.nextInt(Tweets.length)];
			}
			return randomTweet;
		}
	}

	//Assess method compares the product of moderator's tweet product with th stat of the actual tweet

	public static double Assess(String[] GuessArray, TwitterBot[] TwitterBots){
		double sumCorrect = 0.0;
		for (int j=0; j<GuessArray.length; ++j) {
			String[] fields = GuessArray[j].split(",");
			if (fields[0].equals(TwitterBots[j].topic) && fields[1].equals(TwitterBots[j].sentiment)) {  //If the sentiment/topic is correct, tally correct is incremented
				++sumCorrect;
			}
		}
		return sumCorrect/TwitterBots.length;
	}
}
