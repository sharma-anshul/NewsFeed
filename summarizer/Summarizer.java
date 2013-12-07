import java.io.*;
import java.util.Map;
import java.lang.Math;
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
				Set<String> tempVocab = new Set<String>();
				vocabulary = new ArrayList<String>();

				double position = 0.0;
				for (String sentence: sentences) {
						double score = 0.0;
						String[] words = getWords(sentence);
						for (String word: words) {
								word = word.trim().toLowerCase();
								tempVocab.add(word);
								if (!word.trim().equals("")) {
										if (topicWords.contains(word)) {
												score += 1.0;
										}
								}
						}
						topicSentences.put(sentence, score);
						sentencePosition.put(sentence, position);
						position += 1.0;
				}
				
				vocabulary.addAll(tempVocab);
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
				
				//int limit = (as.size() - n) > 0 ? (as.size() - n) : 0;
				ArrayList<String> topNThings = new ArrayList<String>();
				for (int i = as.size() - 1; i >= 0; i--) {
						
						Map.Entry e = (Map.Entry)as.get(i);
						String sentence = (String)e.getKey();
						
						if (vocabulary.size() > 0) {
								boolean addSentence = true;
								ArrayList<String> vec1 = getVector(sentence);
								
								for (String other: topNThings) {
										ArrayList<String> vec2 = getVector(other);
										double similarity = getCosineSimilarity(vec1, vec2);
										if (similarity > 0.4) {
												addSentence = false;
												break;
										}	
								}

								if (addSentence) {
										topNThings.add(sentence);
								}

						} else {			
								topNThings.add(sentence);
						}

						if (topNThings.size() == n) {
								break;
						}
				}
				
				return topNThings;
		}
		
		//Calculate cosine similarity
		public static double getCosineSimilarity(ArrayList<Integer> vec1, ArrayList<Integer> vec2) {
				int numerator = 0, denominator1 = 0, denominator2 = 0;
				
				for (int i = 0; i < vec1.size(); i++) {
						numerator += vec1[i] * vec2[i];
						denominator1 += Math.pow(vec1[i], 2);
						denominator2 += Math.pow(vec2[i], 2);
				}
				
				if (denominator1 == 0 || denominator2 == 0) {
						return 1.0;
				} else {
						return (numerator / (Math.pow(denominator1, 0.5) + Math.pow(denominator2, 0.5)));
				}
		}

		//Get sentence vector
		public static ArrayList<Integer> getVector(String sentence) {
				String[] words = getWords(sentence);
				ArrayList<Integer> vector = new ArrayList<Integer>();

				for (String word: words) {
						int index = vocabulary.indexOf(word);
						vector[index] = 1;
				}

				return vector;
		}
		
		public static String[] getWords(String sentence) {
				return sentence.split("\\s|,|'|;|:|\"");
		}

		private static String topicWordRoot = "../TopicWordTool/TopicWords-v2/";
		private static ArrayList<String> vocabulary;

}
