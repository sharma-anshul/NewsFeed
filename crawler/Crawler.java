/* Usage:
 *
 * ?> java Crawler URL 
 *
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import de.l3s.boilerpipe.extractors.DefaultExtractor;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class Crawler {
		public static void main(String[] args) {
				try {
						String url = args[0].startsWith("http://") ? args[0] : "http://" + args[0];
						String page = getPage(url);
						String text = getText(page);
						ArrayList<String> internalLinks = getInternalLinks(page);
						//System.out.println(internalLinks);
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
		
		//Uses regex to extract all links on a page
		public static ArrayList<String> getInternalLinks(String page) {
				ArrayList<String> internalLinks = new ArrayList<String>();
				Pattern pattern = Pattern.compile("<(a|A) (href|HREF)=\"(.*?)\".*?>.*?</(\\1)>");
				Matcher matcher = pattern.matcher(page);

				while(matcher.find()) {
						internalLinks.add(matcher.group(3));
				}

				return internalLinks;
		}
}
