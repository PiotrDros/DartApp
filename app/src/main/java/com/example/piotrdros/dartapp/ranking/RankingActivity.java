package com.example.piotrdros.dartapp.ranking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class RankingActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);


        listView = (ListView) findViewById(R.id.ranking_list_view);


        new GetGamesTask().execute();

    }


    private class GetGamesTask extends AsyncTask<Void, Void, List<Ranking>> {

        @Override
        protected List<Ranking> doInBackground(Void... params) {
            List<Ranking> rankings = new ArrayList<>();

            DartApplication dartApplication = (DartApplication) RankingActivity.this.getApplication();

            String appUrl = Util.getAppUrl(getApplicationContext());

            //http://78.9.79.62/PlayerRanking
            String url = "http://" + appUrl + "/PlayerRanking";
            String html = Util.getStringFromHttpGet(url, dartApplication);
            Document doc = Jsoup.parse(html);
            Element table = doc.getElementsByTag("table").get(0);
            Element tbody = table.getElementsByTag("tbody").get(0);
            Elements rows = tbody.getElementsByTag("tr");
            for (Element row : rows) {
                Elements dataElements = row.children();
                Ranking ranking = new Ranking();
                rankings.add(ranking);

                ranking.name = dataElements.get(0).text();
                ranking.rank = dataElements.get(1).text();
                ranking.lastChange = dataElements.get(2).text();


            }

            return  rankings;
        }

        @Override
        protected void onPostExecute(List<Ranking> rankings) {
            RankingAdapter gameAdapter = new RankingAdapter(RankingActivity.this, rankings);
            listView.setAdapter(gameAdapter);
        }
    }

}
