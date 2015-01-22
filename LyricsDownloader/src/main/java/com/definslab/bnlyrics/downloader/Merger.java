package com.definslab.bnlyrics.downloader;

import com.definslab.bnlyrics.common.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Merger {

    static File dir = new File("lyricsdownloader/data/txts").getAbsoluteFile();

    public static void main(String[] args) throws IOException {

        Gson gson = new Gson();
        try {
            HashMap<String, ArrayList<Song>> categoryVsSongMap = new HashMap();


            System.out.println(dir.getAbsolutePath());
            File[] allFiles = dir.listFiles();
            System.out.println("Parsing " + allFiles.length + " files");
            int count = 0;
            if (allFiles != null) {
                for (int i = 0; i < allFiles.length; i++) {
                    File dataFile = allFiles[i];
                    String category = FilenameUtils.getBaseName(dataFile.getName()).trim();

                    ArrayList<Song> songs = gson.fromJson(FileUtils.readFileToString(dataFile), new TypeToken<ArrayList<Song>>() {
                    }.getType());
                    Collections.sort(songs);
                    categoryVsSongMap.put(category, songs);
                    count += songs.size();
                }
            }

            System.out.println("Total " + count + " songs");


            String alldata = gson.toJson(categoryVsSongMap, new TypeToken<HashMap<String, ArrayList<Song>>>() {
            }.getType());
            FileUtils.write(new File(dir.getParent(), "all.dat"), alldata);

            ArrayList<String> catList = new ArrayList<>(categoryVsSongMap.keySet());
            Collections.sort(catList);
            String allCats = gson.toJson(catList, new TypeToken<ArrayList<String>>() {
            }.getType());
            FileUtils.write(new File(dir.getParent(), "cat.dat"), allCats);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
