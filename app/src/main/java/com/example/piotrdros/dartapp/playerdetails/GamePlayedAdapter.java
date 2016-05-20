package com.example.piotrdros.dartapp.playerdetails;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.piotrdros.dartapp.R;
import com.example.piotrdros.dartapp.Util;

import java.util.List;

public class GamePlayedAdapter extends ArrayAdapter<GamePlayed> {

    private final Context context;
    List<GamePlayed> gamesPlayed;

    public GamePlayedAdapter(Context context, List<GamePlayed> gamesPlayed) {
        super(context, R.layout.games_played_row);
        this.gamesPlayed = gamesPlayed;
        this.context = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GamePlayedHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.games_played_row, null);
            holder = new GamePlayedHolder();
            holder.id = (TextView) row.findViewById(R.id.player_id);
            holder.when = (TextView) row.findViewById(R.id.when);
            holder.againstWho = (TextView) row.findViewById(R.id.against_who);
            holder.rank = (TextView) row.findViewById(R.id.rank);
            holder.delta = (TextView) row.findViewById(R.id.delta);

            row.setTag(holder);
        } else {
            holder = (GamePlayedHolder) row.getTag();
        }

        GamePlayed gamePlayed = gamesPlayed.get(position);
        holder.id.setText(gamePlayed.id);
        holder.when.setText(gamePlayed.when);
        holder.againstWho.setText(gamePlayed.againstWho);
        holder.rank.setText(gamePlayed.rank);
        holder.delta.setText(gamePlayed.delta);

        row.setBackgroundResource(Util.getRowBackgroundColor(position));

        return row;
    }

    @Override
    public int getCount() {
        return gamesPlayed.size();
    }

    static class GamePlayedHolder {
        TextView id;
        TextView when;
        TextView againstWho;
        TextView rank;
        TextView delta;
    }
}
