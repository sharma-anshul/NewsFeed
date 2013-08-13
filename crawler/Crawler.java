import java.util.*;
import java.net.*;
import java.io.*;

public class Crawler {
		public static void main(String[] args) {
				try {
						String page = getPage("http://google.com");
						System.out.println(page);
				} catch(IOException e) {
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
}
