package com.ihhira.projects.bnlyrics.downloader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import au.com.bytecode.opencsv.CSVReader;

public class Merger {
	public static void main(String[] args) throws IOException {
		try {
			HashMap categoryVsSongMap = new HashMap();

			File csvFilesdir = new File("").getAbsoluteFile().getParentFile();
			File[] allFiles = csvFilesdir.listFiles();
			if (allFiles != null) {
				for (int i = 0; i < allFiles.length; i++) {
					File csvFile = allFiles[i];
					String category = FilenameUtils.getBaseName(csvFile.getName());
					String extention = FilenameUtils.getExtension(csvFile.getName());
					if (!extention.equals("csv"))
						continue;
					
					categoryVsSongMap.put(category, new ArrayList());
					CSVReader csvReader = new CSVReader(new FileReader(csvFile));
					List allRecord = csvReader.readAll();
					csvReader.close();
					for (int j = 0; j < allRecord.size(); j++) {
						List record = (List) allRecord.get(j);
						Song song = new Song(record);
						((List) categoryVsSongMap.get(category)).add(song);
					}
				}
			}

			String[] categories = new String[categoryVsSongMap.size()];
			categoryVsSongMap.keySet().toArray(categories);
			for (int i = 0; i < categories.length; i++) {
				List songList = (List) categoryVsSongMap.get(categories[i]);
				for (int j = 0; j < songList.size(); j++) {
					Song song = (Song) songList.get(i);
					System.out.println(categories[i] + ":" + song);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
