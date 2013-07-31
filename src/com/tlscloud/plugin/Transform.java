package com.tlscloud.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class Transform {

	public static String getCSVFromFile(String fileName, String separator)
			throws FileNotFoundException, IOException {
		String content = IOUtils.toString(new FileReader(fileName));
		Map<String, String> map = getMap(content);
		StringBuilder sb = new StringBuilder();
		sb.append(getHeader(map, separator)).append("\n");
		sb.append(getCSVFromMap(map, separator)).append("\n");
		return sb.toString();
	}

	public static String getCSVFromDirectory(String fileDirectory, String separator)
			throws FileNotFoundException, IOException {
		File files[] = new File(fileDirectory).listFiles();
		StringBuilder sb = new StringBuilder();
		Map<String, String> mapFinal =new LinkedHashMap<String,String>();
		for (int i = 0; i < files.length; i++) {
			String content = IOUtils.toString(new FileReader(files[i]));
			Map<String,String>map = getMap(content);
			mapFinal.putAll(map);
			//sb.append(getHeader(mapFinal, separator)).append("\n");
			
		}
		sb.append(getHeader(mapFinal, separator)).append("\n");
		for(int j=0;j<files.length;j++){
			String content = IOUtils.toString(new FileReader(files[j]));
			Map<String,String>map = getMap(content);
			sb.append(getCSVFromMapUsingHeader(mapFinal,map, separator)).append("\n");
		}
		return sb.toString();
	}

	public static String getCSVFromMapUsingHeader(Map<String, String> headerMap,Map<String, String> map, String separator) {

		StringBuffer sb2 = new StringBuffer();

		ArrayList<String> valueList = new ArrayList<String>(headerMap.keySet());
		for (int i = 0; i < valueList.size(); i++) {
			if(map.get(valueList.get(i))!=null){
				sb2.append(map.get(valueList.get(i)));
			}else{
				sb2.append("");
			}
			if (i < valueList.size() - 1) {
				sb2.append(separator);
			}
		}
		return sb2.toString();
	}
	
	public static String getCSVFromMap(Map<String, String> map, String separator) {

		StringBuffer sb2 = new StringBuffer();

		ArrayList<String> valueList = new ArrayList<String>(map.keySet());
		for (int i = 0; i < valueList.size(); i++) {
			sb2.append(map.get(valueList.get(i)));
			if (i < valueList.size() - 1) {
				sb2.append(separator);
			}
		}
		return sb2.toString();
	}

	public static Map<String, String> getMap(String content) {
		String contentLines[] = content.split("\n");
		Map<String, String> map = new LinkedHashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		boolean comment = false;
		String keyTmp = "";
		for (int i = 1; i < contentLines.length; i++) {
			String tmp = contentLines[i];
			if (comment) {
				sb.append(tmp).append("\n");
			} else {
				if (tmp.indexOf("---".trim()) > -1) {
					comment = true;
				} else {
					if (!tmp.equals("")) {
						String key = tmp.substring(0, tmp.indexOf(":"));
						if (key.startsWith("-") || key.startsWith(" ")) {
							key = keyTmp + "/" + key;
						} else {
							keyTmp = key;
						}
						String value = tmp.substring(tmp.indexOf(":") + 1)
								.trim();
						map.put(key, value);
					}
				}
			}

		}
		map.put("---", sb.toString().replace("\n", "\\n"));
		return map;
	}

	public static String getHeader(Map<String, String> map, String separator) {
		ArrayList<String> keyList = new ArrayList<String>(map.keySet());

		return Arrays.toString(keyList.toArray()).replaceAll(",", separator)
				.replaceAll("\\[", "").replaceAll("\\]", "");
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getCSVFromDirectory("test", ","));
	}
}
