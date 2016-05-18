package com.example.piotrdros.dartapp.ranking;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.piotrdros.dartapp.R;
import com.example.piotrdros.dartapp.Util;
import com.example.piotrdros.dartapp.game.Game;

import java.util.List;

public class RankingAdapter extends ArrayAdapter<Game> {

    private final Context context;
    List<Ranking> rankings;

    public RankingAdapter(Context context, List<Ranking> rankings) {
        super(context, R.layout.ranking_list_row);
        this.rankings = rankings;
        this.context = context;

    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RankingHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.ranking_list_row, null);
            holder = new RankingHolder();
            holder.name = (TextView) row.findViewById(R.id.name);
            holder.rank = (TextView) row.findViewById(R.id.rank);
            holder.lastChange = (TextView) row.findViewById(R.id.last_change);

            row.setTag(holder);
        } else {
            holder = (RankingHolder) row.getTag();
        }

        Ranking ranking = rankings.get(position);
        holder.name.setText(ranking.name);
        holder.rank.setText(ranking.rank);
        holder.lastChange.setText(ranking.lastChange);

        row.setBackgroundResource(Util.getRowBackgroundColor(position));

        return row;
    }

    @Override
    public int getCount() {
        return rankings.size();
    }

    static class RankingHolder {
        TextView name;
        TextView rank;
        TextView lastChange;
    }
}
