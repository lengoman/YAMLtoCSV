package com.tlscloud.plugin;

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
		String yaml = IOUtils.toString(new FileReader(fileName));
		return getCSV(yaml, separator);
	}

	public static String getCSV(String content, String separator) {
		String contentLines[] = content.split("\n");
		Map<String, String> map = new LinkedHashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		boolean comment = false;
		String keyTmp="";
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
						if(key.startsWith("-")||key.startsWith(" ")){
							key=keyTmp+"/"+key;
						}else{
							keyTmp=key;
						}
						String value = tmp.substring(tmp.indexOf(":") + 1)
								.trim();
						map.put(key, value);
					}
				}
			}

		}
		map.put("---", sb.toString().replace("\n", "\\n"));
		//System.out.println(map);
		ArrayList<String> keyList=new ArrayList<String>(map.keySet());
		StringBuffer sb2=new StringBuffer();
		sb2.append(Arrays.toString(keyList.toArray()).replaceAll(",",separator).replaceAll("\\[","").replaceAll("\\]","")).append("\n");
		ArrayList<String> valueList=new ArrayList<String>(map.values());
		for(int i=0;i<valueList.size();i++){
			sb2.append(valueList.get(i));
			if(i<valueList.size()-1){
				sb2.append(separator);
			}
		}
		return sb2.toString();
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getCSVFromFile("test/test2.yaml", ","));
	}
}
