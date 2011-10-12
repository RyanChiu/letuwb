package com.zrd.zr.pnj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PNJ {
	
	/*
	 * methods zone
	 */
	
	/*
	 * get all the response contents from PHP server with the way "get".
	 * and the sParams will be the formated string for PHP such as
	 * "count=10&page=3" kind of thing. the sPHPURL will be like
	 * "http://www.xxx.com/getme.php" kind of stuff.
	 */
	public static String getResponseByGet(String sPHPURL, String sParams) {
		URL url;
		try {
			url = new URL(sPHPURL + "?" + sParams);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line, content = "";
			while ((line = reader.readLine()) != null) {
				content += line;
			}
			return content;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}
	
	/*
	 * return the parameters for PHP from an array values 
	 * into the format such as "count=10&page=1", and if
	 * the pairs are given with wrongs with params, it'll
	 * just return an empty string.
	 */
	public static String getParamsAsStr(String... params) {
		String sParams = "";
		if (params.length >= 2 && (params.length % 2 == 0)) {
			for (int i = 0; i < params.length; i += 2) {
				sParams += (params[i] + "=" + params[i + 1]);
				if (i != params.length - 2) {
					sParams += "&";
				}
			}
		}
		return sParams;
	}
}
