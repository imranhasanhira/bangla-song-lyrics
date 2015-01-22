package com.definslab.bnlyrics.android;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.definslab.bnlyrics.common.Song;


/**
 * Author: Md Imran Hasan Hira (imranhasanhira@gmail.com)
 * Date: 1/22/2015.
 */
public class SongActivity extends Activity {

    public static Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_song);

//        int songId = getIntent().getIntExtra(Constants.SONG_ID, -1);
        if (song != null) {
            setTitle(song.title);
            TextView songDetails = (TextView) findViewById(R.id.tv_song_details);
            songDetails.setText(song.formatted());
            songDetails.setMovementMethod(new ScrollingMovementMethod());
        }
    }
}
