/* Usage:
 * 
 * ?> java Test URL
 *
 */


import java.io.*;
import crawler.*;
import java.net.*;
import summarizer.*;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

public class Test {
		public static void main(String[] args) {
				try {
						String url = args[0].startsWith("http://") ? args[0] : "http://" + args[0];
						String text = Crawler.getText(Crawler.getPage(url));
						
						ArrayList<String> topicWords = Summarizer.getTopNTopicWords(text, 10);
						String summary = Summarizer.summarize(text, topicWords);
						
						if (url.startsWith("http://")) {
								url = url.substring(7, url.length());
						}

						URL apiURL = new URL("http://newsfeed.net84.net/api.php/addSummary?url=" + url + 
											"&topWords=" + encodeTopicWords(topicWords) +
											"&summary=" + summary);
						URLConnection connection = apiURL.openConnection();
						connection.connect();
						
						BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String inputLine;

						while((inputLine = in.readLine()) != null)
						{
							System.out.println(inputLine);
						}

						in.close();
							
						System.out.println(summary);
						
				} catch(IOException e) {
						e.printStackTrace();
				} catch(Exception e) {
						e.printStackTrace();
				}
		}

		public static String encodeTopicWords(ArrayList<String> topicWords) {
				String encodedString = "";
				for (String word: topicWords) {
						encodedString += word + "!!DELIMITER!!";
				}	
				
				return encodedString.substring(0, encodedString.length() - "!!DELIMITER!!".length());
		}
}
