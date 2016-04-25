package com.example.piotrdros.dartapp.game;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.piotrdros.dartapp.DartApplication;
import com.example.piotrdros.dartapp.R;
import com.example.piotrdros.dartapp.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class AllGamesActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_games);


        listView = (ListView) findViewById(R.id.games_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameAdapter gmeAdapter = (GameAdapter) listView.getAdapter();
                if (gmeAdapter != null) {
                    Game game = gmeAdapter.games.get(position);
                    GameDetailsActivity.startGameDetailsActivity(AllGamesActivity.this, game.id);
                }
            }
        });


        new GetGamesTask().execute();

    }


    private class GetGamesTask extends AsyncTask<Void, Void, List<Game>> {

        @Override
        protected List<Game> doInBackground(Void... params) {
            List<Game> games = new ArrayList<>();

            DartApplication dartApplication = (DartApplication) AllGamesActivity.this.getApplication();

            String appUrl = Util.getAppUrl(getApplicationContext());

            //http://78.9.79.62/Game/Index/0
            String url = "http://" + appUrl + "/Game";
            String html = Util.getStringFromHttpGet(url, dartApplication);
            Log.e("GetGamesTask", url);
            Log.e("GetGamesTask", html);
            Document doc = Jsoup.parse(html);
            Element table = doc.getElementsByTag("table").get(0);
            Element tbody = table.getElementsByTag("tbody").get(0);
            Elements rows = tbody.getElementsByTag("tr");
            for (Element row : rows) {
                Elements dataElements = row.children();
                Game game = new Game();
                games.add(game);

                game.id = dataElements.get(0).text();
                game.added = dataElements.get(1).text();
                game.done = dataElements.get(2).text();
                game.players = dataElements.get(3).text();
                game.reporter = dataElements.get(4).text();
                game.status = dataElements.get(5).text();


            }


            return games;
        }

        @Override
        protected void onPostExecute(List<Game> games) {

            GameAdapter gameAdapter = new GameAdapter(AllGamesActivity.this, games);
            listView.setAdapter(gameAdapter);
        }
    }

}
