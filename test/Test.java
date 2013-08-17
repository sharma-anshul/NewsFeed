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
						String text = Crawler.getText(Crawler.getPage(url));

						Summarizer.summarize(text);
						
				} catch(IOException e) {
						e.printStackTrace();
				} catch(Exception e) {
						e.printStackTrace();
				}
		}
}
