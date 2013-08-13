/* Usage:
 *
 * ?> java Crawler URL 
 *
 */

import java.util.*;
import java.net.*;
import java.io.*;
import de.l3s.boilerpipe.extractors.DefaultExtractor;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class Crawler {
		public static void main(String[] args) {
				try {
						String url = args[0].startsWith("http://") ? args[0] : "http://" + args[0];
						String page = getPage(url);
						String text = getText(page);
						System.out.println(text);
				} catch(IOException e) {
						e.printStackTrace();
				} catch(Exception e) {
						e.printStackTrace();
				}
		}

		//Downloads page and returns String representation
		public static String getPage(String url) throws IOException {
				URL link = new URL(url);
		    	URLConnection linkCon = link.openConnection();
		    	BufferedReader in = new BufferedReader(new InputStreamReader(linkCon.getInputStream()));
		    	
		    	String line, page = "";
		    	while ((line = in.readLine()) != null) 
		    			page += line;
		    	in.close();

		    	return page;
		}
		
		//Removes boilerplate(HTML tags, ads etc) and returns main text
		public static String getText(String page) throws Exception {
				DefaultExtractor extractor = DefaultExtractor.getInstance();
				String text = extractor.getText(page);

				return text;
		}
}
