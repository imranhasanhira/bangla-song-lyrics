package com.ihhira.projects.bnlyrics.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVWriteProc;
import au.com.bytecode.opencsv.CSVWriter;

public class Main {
	String baseUrlString = "http://banglasonglyrics.com";
	CSVWriter csvWriter;
	CSV csv;
	private int songCount;

	Main() throws IOException {
		songCount = 0;

		//"ব্যান্ড", "রবীন্দ্র-সংগীত", "বিবিধ", "আধুনিক", "ছায়াছবি","জীবনমুখী-গান",
		String[] categories = {   "দেশাত্মবোধক-গান", "নাটক",
				"পপ-সঙ্গীত", "পল্লীগীতি", "বাউল", "ভক্তিমূলক-গান", "লালন",
				"হাসন-রাজার-গান" };
		for (int i = 0; i < categories.length; i++) {
//			downloadCategory(categories[0], 1);
			final String cat = categories[i];
			csv = CSV.separator(';').quote('\'').skipLines(1).charset("UTF-8")
					.create();
			csv.write(new File(cat+".csv"), new CSVWriteProc() {

				public void process(CSVWriter w) {
					csvWriter = w;
					try {
						downloadCategory(cat, 1);
						csvWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			});
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
						new Object[] { new Integer(songCount) })
						+ " > Category: "
						+ category
						+ ", page: "
						+ page
						+ ", Song: " + song);
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
		csvWriter.writeNext(new String[] { song.title, song.lyrics, song.vocal,
				song.album, "" + song.category });
		csvWriter.flush();
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
