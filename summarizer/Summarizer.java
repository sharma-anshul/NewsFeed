/* Usage:
 *
 * ?> java Summarizer
 *
 */

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Summarizer {
		public static void main(String[] args) {
				try {
						getTopicWords();
				} catch(IOException e) {
						e.printStackTrace();
				} catch(Exception e) {
						e.printStackTrace();
				}
		}

		//Performs Topic Analysis on article to get Topic words
		public static HashMap<String, Double> getTopicWords() throws Exception {
				Process p = Runtime.getRuntime().exec("./Topic.sh");
				p.waitFor();

				File ts = new File("../TopicWordTool/TopicWords-v2/test.ts");
				BufferedReader in = new BufferedReader(new FileReader(ts));
				HashMap topicWords = new HashMap<String, Double>();
				String line;
				while((line = in.readLine()) != null) {
						String word = line.split("\\s")[0];
						double score = Double.parseDouble(line.split("\\s")[1]);

						topicWords.put(word, score);
				}

				return topicWords;
		}
}
