package com.definslab.bnlyrics.android;

import android.content.Context;
import android.content.res.AssetManager;

import com.definslab.bnlyrics.common.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: Md Imran Hasan Hira (imranhasanhira@gmail.com)
 * Date: 1/22/2015.
 */
public class Database {
    static HashMap<String, ArrayList<Song>> songmap;
    static HashMap<String, String> categoryMap;

    public static void initialize(Context context) throws IOException {

        Gson gson = new Gson();
        AssetManager assetManager = context.getAssets();

        InputStream allCatIs = assetManager.open("cat.dat");
        categoryMap = gson.fromJson(IOUtils.toString(allCatIs), new TypeToken<HashMap<String, String>>() {
        }.getType());

        InputStream allDataIs = assetManager.open("all.dat");
        songmap = gson.fromJson(IOUtils.toString(allDataIs), new TypeToken<HashMap<String, ArrayList<Song>>>() {
        }.getType());

    }

    /**
     * @return a map of category name vs category id
     */
    public static HashMap<String, String> getCategoryMap() {
        return categoryMap;
    }


    public static ArrayList<Song> getSong(String searchString, boolean deep) {
        return getSong(null, searchString, deep);
    }

    public static ArrayList<Song> getSong(String category, String searchString, boolean deep) {
        ArrayList<Song> result = new ArrayList<>();
        for (String cat : categoryMap.values()) {
            if (category != null && !cat.equals(category)) continue;
            ArrayList<Song> songs = songmap.get(cat);
            for (int i = 0; i < songs.size(); i++) {
                Song song = songs.get(i);
                if (song.title.contains(searchString)) {
                    result.add(song);
                } else if (deep) {
                    if (song.lyrics != null && song.lyrics.contains(searchString)) {
                        result.add(song);
                    } else if (song.vocal != null && song.vocal.contains(searchString)) {
                        result.add(song);
                    } else if (song.album != null && song.album.contains(searchString)) {
                        result.add(song);
                    }
                }
            }
        }

        return result;
    }

    public static ArrayList<Song> getSongByCategory(String categoryString) {
        return songmap.get(categoryString);
    }
}
