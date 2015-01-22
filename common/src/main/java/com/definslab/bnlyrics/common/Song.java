package com.definslab.bnlyrics.common;

import java.util.List;
/**
 * Author: Md Imran Hasan Hira (imranhasanhira@gmail.com)
 * Date: 1/22/2015.
 */
public class Song implements Comparable {

    public String title;
    public String lyrics;
    public String vocal;
    public String album;
    public int category;

    Song() {

    }

    public Song(String title, String lyrics) {
        this.title = title;
        this.lyrics = lyrics;
    }

    public Song(String title, String lyrics, String vocal, String album) {
        this.title = title;
        this.lyrics = lyrics;
        this.vocal = vocal;
        this.album = album;
    }

    public Song(List object) {
        this.title = (String) object.get(0);
        this.lyrics = (String) object.get(1);
        this.vocal = (String) object.get(2);
        this.album = (String) object.get(3);
        this.category = Integer.parseInt((String) object.get(4));
    }

    public Song(String... data) {
        this.title = data[0];
        this.lyrics = data[1];
        this.vocal = data[2];
        this.album = data[3];
        this.category = Integer.parseInt(data[4]);
    }

    public int compareTo(Object arg0) {
        return title.compareTo(((Song) arg0).title);
    }

    public String toString() {
        return title + " : " + vocal + " : " + album;
    }

    public String formatted() {
        return lyrics + "\n\n-" + vocal + "\n-" + album;
    }
}
