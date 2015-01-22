package com.definslab.bnlyrics.downloader;

import com.definslab.bnlyrics.common.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    String baseUrlString = "http://banglasonglyrics.com";
    //	CSVWriter csvWriter;
//	CSV csv;
    private int songCount;

    HashMap<String, ArrayList<Song>> songmap;
    String[] categories;

    File dir = new File("lyricsdownloader/data");

    Gson gson = new Gson();

    Main() throws IOException {
        songCount = 0;
        songmap = new HashMap<>();
        categories = new String[0];
//        categories = gson.fromJson(FileUtils.readFileToString(new File(dir, "cat.txt")), new TypeToken<String[]>(){}.getType());
        categories = getCategories();

        for (int i = 15; i < categories.length; i++) {
//			downloadCategory(categories[0], 1);
            final String cat = categories[i];
            songmap.put(cat, new ArrayList<Song>());
            downloadCategory(cat, 1);
            writeCategory(cat, songmap.get(cat));

        }


    }

    private String[] getCategories() throws IOException {
        Document doc = Jsoup.connect(baseUrlString).timeout(20 * 1000)
                .userAgent("Mozilla").get();

        Elements catItems = doc.select("div.widget li.cat-item a");
        HashMap<String, String> alLCats = new HashMap<>();

        for (Element catItem : catItems) {
            String caturl = catItem.attr("href");
            caturl = caturl.substring(37, caturl.length() - 1);
            caturl = URLDecoder.decode(caturl, "UTF-8");
            String catName = catItem.text().trim();
            alLCats.put(catName, caturl);
        }

        String allCatString = gson.toJson(alLCats, new TypeToken<HashMap<String, String>>(){}.getType());
        FileUtils.write(new File(dir, "cat.dat"), allCatString);
        System.out.println("Categories written");

        String[] cats = new String[alLCats.size()];
        alLCats.values().toArray(cats);
        return cats;
    }

    private void writeCategory(String cat, ArrayList<Song> songs) {
        Gson gson = new Gson();
        String str = gson.toJson(songs, new TypeToken<ArrayList<Song>>() {
        }.getType());
        File file = new File(dir, cat.trim() + ".txt");
        try {
            FileUtils.write(file, str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }

    private void downloadCategory(String category, int page) throws IOException {
        String urlString = baseUrlString + "/category/"
                + URLEncoder.encode(category, "UTF-8") + "/page/" + page;
        Document doc = Jsoup.connect(urlString).timeout(20 * 1000)
                .userAgent("Mozilla").get();

        Elements thumbnailElements = doc.select("ul.thumbnails div.inner>a");

        if (thumbnailElements != null) {
            for (int i = 0; i < thumbnailElements.size(); i++) {
                Element item = thumbnailElements.get(i);
                String songUrlString = item.attr("href");
                Song song = downloadSong(songUrlString);
                System.out.println(String.format("%5d",
                        new Object[]{new Integer(songCount)})
                        + " > Category: "
                        + category
                        + ", page: "
                        + page
                        + ", Song: " + song);
                songmap.get(category).add(song);
                writeSong(song);
            }

        }

        Elements paginationElment = doc.select("div.loop-pagination");
        Elements nextElement = paginationElment.select("a.next");
        if (nextElement != null && nextElement.size() > 0) {
            downloadCategory(category, page + 1);
        }
    }

    private void writeSong(Song song) throws IOException {
        songCount++;

//		csvWriter.writeNext(new String[] { song.title, song.lyrics, song.vocal,
//				song.album, "" + song.category });
//		csvWriter.flush();
    }

    private Song downloadSong(String songUrlString) throws IOException {
        Document doc = Jsoup.connect(songUrlString).timeout(20 * 1000)
                .userAgent("Mozilla").get();

        HtmlToPlainText htmlToPlainText = new HtmlToPlainText();
        String title = htmlToPlainText.getPlainText(
                doc.select("h2.entry-title").first()).trim();
        String lyrics = htmlToPlainText.getPlainText(
                doc.select("div.lyrics").first()).trim();

        Elements miscElements = doc.select("div.span6 ul.inner li a");
        String vocal = miscElements.first().text().trim();
        String album = miscElements.get(1).text().trim();

        return new Song(title, lyrics, vocal, album);
    }

}
