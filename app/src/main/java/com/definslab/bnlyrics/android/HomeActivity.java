package com.definslab.bnlyrics.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: Md Imran Hasan Hira (imranhasanhira@gmail.com)
 * Date: 1/22/2015.
 */
public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateSplashScreen();
    }

    private void populateSplashScreen() {
        setContentView(R.layout.splashscreen);

        final long timeout = System.currentTimeMillis() + 2 * 1000;

        AsyncTask<Integer, Void, Exception> dataLoadingTask = new AsyncTask<Integer, Void, Exception>() {
            @Override
            protected Exception doInBackground(Integer... params) {
                try {
                    Database.initialize(getApplicationContext());
                    while (System.currentTimeMillis() < timeout) {
                        Thread.sleep(200);
                    }
                    return null;
                } catch (Exception e) {
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Exception e) {
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Data loading error", Toast.LENGTH_SHORT);
                } else {
                    populateHome();
                }
            }
        };
        dataLoadingTask.execute();
    }


    private void populateHome() {
        setContentView(R.layout.activity_home);

        final HashMap<String, String> categoryMap = Database.getCategoryMap();
        final ArrayList<String> categories = new ArrayList<>(categoryMap.keySet());

        for (int i = 0, n = categories.size(); i < n; i++) {
            if (categoryMap.get(categories.get(i)).equalsIgnoreCase("islami")) {
                categories.remove(i);
                break;
            }
        }

        ListView lvAllCat = (ListView) findViewById(R.id.lv_all_cat);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
        lvAllCat.setAdapter(adapter);
        lvAllCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = categories.get(position);
                String categoryId = categoryMap.get(categoryName);
               // Log.w("PPPP", "CAT: " + categoryId);


                Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
                intent.putExtra(Constants.CATEGORY, categoryId);
                startActivity(intent);
            }
        });

        findViewById(R.id.b_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch(false);
            }
        });

        findViewById(R.id.b_search).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startSearch(true);
                return true;
            }
        });
    }

    private void startSearch(boolean deepSearch) {
        String searchString = ((EditText) findViewById(R.id.et_search)).getText().toString();

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constants.SEARCH_STRING, searchString);
        intent.putExtra(Constants.SEARCH_DEEP, deepSearch);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
