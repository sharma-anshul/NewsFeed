import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Summarizer {

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

		//Gets top n Topic Words
		public static ArrayList<String> getTopNTopicWords(HashMap<String, Double> topicWords, int n) {
				ArrayList as = new ArrayList(topicWords.entrySet());  
				Collections.sort(as , new Comparator() {  
				        public int compare(Object o1, Object o2) {  
				        		Map.Entry e1 = (Map.Entry)o1 ;
				        		Map.Entry e2 = (Map.Entry)o2 ;  
				        		
				        		Double first = (Double)e1.getValue();  
				        		Double second = (Double)e2.getValue();  
				        		
				        		return first.compareTo(second);  
				        }  
				});
				
				int limit = (as.size() - n) > 0 ? (as.size() - n) : 0;
				ArrayList<String> topNTopicWords = new ArrayList<String>();
				for (int i = as.size() - 1; i >= limit; i--) {
						Map.Entry e = (Map.Entry)as.get(i);
						topNTopicWords.add((String)e.getKey());
				}

				return topNTopicWords;
		}
}
