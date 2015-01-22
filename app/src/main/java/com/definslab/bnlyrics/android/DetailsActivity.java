package com.definslab.bnlyrics.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.definslab.bnlyrics.common.Song;

import java.util.ArrayList;

/**
 * Author: Md Imran Hasan Hira (imranhasanhira@gmail.com)
 * Date: 1/22/2015.
 */
public class DetailsActivity extends Activity {

    String categoryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);


        categoryString = getIntent().getStringExtra(Constants.CATEGORY);
        if (categoryString != null) {
            //findViewById(R.id.ll_search_bar).setVisibility(View.GONE);
            setTitle(categoryString);
            showSongByCategory(categoryString);

        } else {
            String searchString = getIntent().getStringExtra(Constants.SEARCH_STRING);
            if (searchString != null) {
                ((EditText) findViewById(R.id.et_search)).setText(searchString);
            }

            boolean deep = getIntent().getBooleanExtra(Constants.SEARCH_DEEP, false);
            showSearchedResult(searchString, deep);
        }

        findViewById(R.id.b_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = ((EditText) findViewById(R.id.et_search)).getText().toString();
                showSearchedResult(searchString, false);
            }
        });

        findViewById(R.id.b_search).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String searchString = ((EditText) findViewById(R.id.et_search)).getText().toString();
                showSearchedResult(searchString, true);
                return true;
            }
        });
    }

    private void showSearchedResult(String searchString, boolean deep) {
        ArrayList<Song> songs = Database.getSong(categoryString, searchString, deep);
        showSongs(songs);
    }

    private void showSongByCategory(String categoryString) {
        ArrayList<Song> songs = Database.getSongByCategory(categoryString);
        showSongs(songs);
    }

    private void showSongs(final ArrayList<Song> songs) {
        ListView lvSearchResult = (ListView) findViewById(R.id.lv_search_results);

        ArrayAdapter<Song> adapter = new ArrayAdapter<Song>(this, android.R.layout.simple_list_item_1, songs);
        lvSearchResult.setAdapter(adapter);
        lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = songs.get(position);
                SongActivity.song = song;
                Intent intent = new Intent(DetailsActivity.this, SongActivity.class);
                startActivity(intent);
            }
        });
    }
}
