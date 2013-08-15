/* Usage:
 *
 * ?> java Crawler URL LIMIT
 *
 */

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import de.l3s.boilerpipe.extractors.DefaultExtractor;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class Crawler {
		
		//Given a link, get n more links from the same domain
		public static HashSet<String> getLinks(String url, int limit) throws Exception {
				HashSet<String> links = new HashSet<String>();
				HashSet<String> temp = new HashSet<String>();
				String page, text, link = url;
				
				while (links.size() < limit) {
						page = getPage(link);
						HashSet<String> internalLinks = getInternalLinks(page);
						links.addAll(internalLinks);

						link = (String) links.toArray()[links.size() - 1];
				}

				return links;
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
		public static HashSet<String> getInternalLinks(String page) {
				HashSet<String> internalLinks = new HashSet<String>();
				Pattern pattern = Pattern.compile("<(a|A) (href|HREF)=\"(.*?)\".*?>.*?</(\\1)>");
				Matcher matcher = pattern.matcher(page);

				while(matcher.find()) {
						internalLinks.add(matcher.group(3));
				}

				return internalLinks;
		}

		//Select links with a length greater than the average link length
		public static ArrayList<String> getLongLinks(ArrayList<String> links) {
				ArrayList<String> longLinks = new ArrayList<String>();
				double avgLen = 0.0;
				for (String link: links) {
						avgLen += link.length();
				}
				avgLen /= links.size();

				for (String link: links) {
						if (link.length() > avgLen) {
								longLinks.add(link);
						}
				}

				return longLinks;
		}
}
