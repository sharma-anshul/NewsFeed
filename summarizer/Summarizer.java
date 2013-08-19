import java.io.*;
import java.util.Map;
import java.util.Locale;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.text.BreakIterator;

public class Summarizer {
		
		private static String topicWordRoot = "../TopicWordTool/TopicWords-v2/";

		//Summarize file based on Topic Words
		public static String summarize(String text, ArrayList<String> topicWords) throws Exception{
				ArrayList<String> sentences = new ArrayList<String>();
				BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
				iterator.setText(text);
				
				int start = iterator.first();
				for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
						String sentence = text.substring(start, end);
						String[] sentenceParts = sentence.split("\n");
						
						for (String part: sentenceParts) {
								if (part.endsWith(".")) {
										sentences.add(part);
								}
						}
				}
				
				ArrayList<String> topSentences = getTopNSentences(sentences, topicWords, 10);
				String summary = "";
				for (String sentence: topSentences) {
						summary += sentence + " ";
				}

				return summary;
		}

		//Gets top sentences based on the number of Topic Words it contains
		public static ArrayList<String> getTopNSentences(ArrayList<String> sentences, ArrayList<String> topicWords, int n) {
				HashMap<String, Double> sentencePosition = new HashMap<String, Double>();
				HashMap<String, Double> topicSentences = new HashMap<String, Double>();
				
				double position = 0.0;
				for (String sentence: sentences) {
						double score = 0.0;
						String[] words = sentence.split("\\s|,|'|;|:|\"");
						for (String word: words) {
								if (!word.trim().equals("")) {
										if (topicWords.contains(word.trim().toLowerCase())) {
												score += 1.0;
										}
								}
						}
						topicSentences.put(sentence, score);
						sentencePosition.put(sentence, position);
						position += 1.0;
				}
				
				ArrayList<String> topSentences = getTopN(topicSentences, n);
				HashMap<String, Double> temp = new HashMap<String, Double>();

				//Sorts top Topic sentences according to position in article to maintain coherence
				for (String sentence: topSentences) {
						double pos = sentencePosition.get(sentence);
						temp.put(sentence, position - pos);
				}
				
				ArrayList<String> sortedTopSentences = getTopN(temp, topSentences.size());

				return sortedTopSentences;
		}

		//Performs Topic Analysis on article to get Topic Words
		public static HashMap<String, Double> getTopicWords() throws Exception {
				Process p = Runtime.getRuntime().exec("./Topic.sh");
				p.waitFor();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream())); 
				StringBuffer sb = new StringBuffer();								 
				String line = reader.readLine();
				sb.append(line);
				while (line != null) {
						line = reader.readLine();
						sb.append(line);
				}

				File ts = new File(topicWordRoot + "test.ts");
				BufferedReader in = new BufferedReader(new FileReader(ts));
				HashMap topicWords = new HashMap<String, Double>();
				line = "";
				while((line = in.readLine()) != null) {
						String word = line.split("\\s")[0];
						double score = Double.parseDouble(line.split("\\s")[1]);
						
						topicWords.put(word, score);
				}

				return topicWords;
		}

		//Gets top n Topic Words
		public static ArrayList<String> getTopNTopicWords(String text, int n) throws Exception {
				
				File file = new File(topicWordRoot + "test");
                PrintWriter pw = new PrintWriter(file);
				pw.print(text); pw.flush(); pw.close();
				
				HashMap<String, Double> topicWords = getTopicWords();
				ArrayList<String> topNTopicWords = getTopN(topicWords, n);

				return topNTopicWords;
		}

		//Get top n "things"
		public static ArrayList<String> getTopN(HashMap<String, Double> things, int n) {
				ArrayList as = new ArrayList(things.entrySet());
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
				ArrayList<String> topNThings = new ArrayList<String>();
				for (int i = as.size() - 1; i >= limit; i--) {
						Map.Entry e = (Map.Entry)as.get(i);
						topNThings.add((String)e.getKey());
				}
				
				return topNThings;
		}
}
