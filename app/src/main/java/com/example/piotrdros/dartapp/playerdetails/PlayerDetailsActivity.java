package com.example.piotrdros.dartapp.playerdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.piotrdros.dartapp.DartApplication;
import com.example.piotrdros.dartapp.R;
import com.example.piotrdros.dartapp.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class PlayerDetailsActivity extends AppCompatActivity {


    public static final String ID_PLAYER = "idPlayer";

    private ListView listView;
    private TextView playerId;
    private TextView playerName;
    private TextView playerRank;

    public static void startPlayerDetailsActivity(Activity parentActivity, String playerId) {
        Intent intent = new Intent(parentActivity, PlayerDetailsActivity.class);
        intent.putExtra(ID_PLAYER, playerId);
        parentActivity.startActivity(intent);
    }

    private class GetGamesPlayedTask extends AsyncTask<String, Void, Details> {


        @Override
        protected Details doInBackground(String... params) {
            String idPlayer = params[0];

            Details details = new Details();


            DartApplication dartApplication = (DartApplication) PlayerDetailsActivity.this.getApplication();


            String appUrl = Util.getAppUrl(getApplicationContext());

            String url = "http://" + appUrl + "/PlayerRanking/Details/" + idPlayer;
            String html = Util.getStringFromHttpGet(url, dartApplication);
            Document doc = Jsoup.parse(html);
            Element tablePlayer = doc.getElementsByTag("table").get(0);


            Element tbodyDetails = tablePlayer.getElementsByTag("tbody").get(0);
            Element playerRow = tbodyDetails.getElementsByTag("tr").get(1);
            Elements dataElements = playerRow.children();


            details.id = dataElements.get(0).text();
            details.name = dataElements.get(1).text();
            details.rank = dataElements.get(2).text();


            Element tableGames = doc.getElementsByTag("table").get(1);
            Element tbody = tableGames.getElementsByTag("tbody").get(0);
            Elements rows = tbody.getElementsByTag("tr");
            for (Element row : rows) {
                Elements dataElementsGames = row.children();
                if (dataElementsGames.get(0).text().startsWith("ID")) {
                    // skip first row with header
                    continue;
                }

                GamePlayed gamePlayed = new GamePlayed();
                details.gamesPlayed.add(gamePlayed);


                gamePlayed.id = dataElementsGames.get(0).text();
                gamePlayed.when = dataElementsGames.get(1).text();
                gamePlayed.againstWho = dataElementsGames.get(2).text();
                gamePlayed.rank = dataElementsGames.get(3).text();
                gamePlayed.delta = dataElementsGames.get(4).text();

            }


            return details;
        }

        @Override
        protected void onPostExecute(Details details) {

            playerId.setText(details.id);
            playerName.setText(details.name);
            playerRank.setText(details.rank);


            GamePlayedAdapter gamePlayedAdapter = new GamePlayedAdapter(PlayerDetailsActivity.this, details.gamesPlayed);
            listView.setAdapter(gamePlayedAdapter);
        }
    }


    private static class Details {
        String id;
        String name;
        String rank;

        List<GamePlayed> gamesPlayed = new ArrayList<>();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        listView = (ListView) findViewById(R.id.games_played_list);


        playerId = (TextView) findViewById(R.id.player_id);
        playerName = (TextView) findViewById(R.id.player_name);
        playerRank = (TextView) findViewById(R.id.player_rank);


        playerId.setText("");
        playerName.setText("");
        playerRank.setText("");



        String idPlayer = getIntent().getStringExtra(ID_PLAYER);
        if (idPlayer != null) {
            new GetGamesPlayedTask().execute(idPlayer);
        } else {
            Log.e(DartApplication.TAG, "PlayerDetailsActivity: idPlayer is NULL!");
        }


    }
}
