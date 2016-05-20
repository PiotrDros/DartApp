package com.example.piotrdros.dartapp.game;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.piotrdros.dartapp.DartApplication;
import com.example.piotrdros.dartapp.R;
import com.example.piotrdros.dartapp.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GameDetailsActivity extends AppCompatActivity {
    ListView listView;

    public static final String GAME_ID = "gameId";
    private TextView reportedOnTextView;
    private TextView winnerTextView;
    private TextView playerCountTextView;
    private TextView playerPlacementTextView;
    private TextView reporterTextView;
    private TextView statusTextView;




    public static void startGameDetailsActivity(Activity parentActivity, String gameId) {
        Intent intent = new Intent(parentActivity, GameDetailsActivity.class);
        intent.putExtra(GAME_ID, gameId);
        parentActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        String gameId = "Empty Id";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gameId = bundle.getString(GAME_ID, "Empty Id");
        }

        listView = (ListView) findViewById(R.id.game_history_content);


        TextView gameIdTextView = (TextView) findViewById(R.id.game_id);
        gameIdTextView.setText(String.format("Game id: %s", gameId));


        reportedOnTextView = (TextView) findViewById(R.id.reported_on);
        winnerTextView = (TextView) findViewById(R.id.winner);
        playerCountTextView = (TextView) findViewById(R.id.player_count);
        playerPlacementTextView = (TextView) findViewById(R.id.player_placement);
        reporterTextView = (TextView) findViewById(R.id.reporter);
        statusTextView = (TextView) findViewById(R.id.status);


        GetGameHistoryTask getGameHistoryTask = new GetGameHistoryTask(gameId);
        getGameHistoryTask.execute();

    }


    private class GetGameHistoryTask extends AsyncTask<Void, Void, GameDetails> {

        private final String gameId;

        public GetGameHistoryTask(String gameId) {
            this.gameId = gameId;
        }

        @Override
        protected GameDetails doInBackground(Void... params) {
            GameDetails gameDetails = new GameDetails();

            DartApplication dartApplication = (DartApplication) GameDetailsActivity.this.getApplication();

            String appUrl = Util.getAppUrl(getApplicationContext());

            String url = "http://" + appUrl + "/Game/Details/" + gameId;
            String html = Util.getStringFromHttpGet(url, dartApplication);
            Document doc = Jsoup.parse(html);
            Element tableDetails = doc.getElementsByTag("table").get(0);


            Element tbodyDetails = tableDetails.getElementsByTag("tbody").get(0);
            Element detailsRow = tbodyDetails.getElementsByTag("tr").get(1);
            Elements dataElements = detailsRow.children();

            gameDetails.reportedOn = dataElements.get(0).text();
            gameDetails.winner = dataElements.get(1).text();
            gameDetails.playerCount = dataElements.get(2).text();
            gameDetails.playerPlacement = dataElements.get(3).text();
            gameDetails.reporter = dataElements.get(4).text();
            gameDetails.status = dataElements.get(5).text();


            Element tableHistory = doc.getElementsByTag("table").get(1);
            Element tbody = tableHistory.getElementsByTag("tbody").get(0);
            Elements rows = tbody.getElementsByTag("tr");
            for (Element row : rows) {
                Elements dataElementsHistory = row.children();
              GameHistory gameHistory = new GameHistory();
                if (dataElementsHistory.get(0).text().startsWith("When")) {
                    // skip first row with header
                    continue;
                }

                gameDetails.gameHistory.add(gameHistory);

                gameHistory.when = dataElementsHistory.get(0).text();
                gameHistory.what = dataElementsHistory.get(1).text();
                gameHistory.who= dataElementsHistory.get(2).text();
            }


            return gameDetails;
        }

        @Override
        protected void onPostExecute(GameDetails details) {


            boolean landscape = Util.isLandscape(getResources());
            if (landscape) {
                winnerTextView.setText(details.winner);
                playerCountTextView.setText(details.playerCount);
                reporterTextView.setText(details.reporter);
            }

            reportedOnTextView.setText(details.reportedOn);


            playerPlacementTextView.setText(details.playerPlacement);

            statusTextView.setText(details.status);


            GameHistoryAdapter gameAdapter = new GameHistoryAdapter(GameDetailsActivity.this, details.gameHistory);
            listView.setAdapter(gameAdapter);
        }
    }


}
