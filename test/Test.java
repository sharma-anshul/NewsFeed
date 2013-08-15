/* Usage:
 * 
 * ?> java Test URL LIMIT
 *
 */


import java.io.*;
import crawler.*;
import summarizer.*;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

public class Test {
		public static void main(String[] args) {
				try {
						String url = args[0].startsWith("http://") ? args[0] : "http://" + args[0];
						int limit = Integer.parseInt(args[1]);
						
						HashSet<String> links = Crawler.getLinks(url, limit);
						
						System.out.println("\nCrawled links:\n");
						for (String link: links) {
								System.out.println(link);
						}
						
						HashMap<String, Double> topicWords = Summarizer.getTopicWords();
						ArrayList<String> topNTopicWords = Summarizer.getTopNTopicWords(topicWords, 10);
						
						System.out.println("\nTop 10 topic words from the test file:\n");
						for (String word: topNTopicWords) {
						        System.out.println(word);
						}

				} catch(IOException e) {
						e.printStackTrace();
				} catch(Exception e) {
						e.printStackTrace();
				}
		}
}
