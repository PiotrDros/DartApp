package com.example.piotrdros.dartapp.game;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.piotrdros.dartapp.R;

import java.util.List;

public class GameHistoryAdapter extends ArrayAdapter<GameHistory> {

    private final Context context;
    List<GameHistory> gameHistroy;

    public GameHistoryAdapter(Context context, List<GameHistory> gameHistory) {
        super(context, R.layout.game_history_list_row);
        this.gameHistroy = gameHistory;
        this.context = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GameHistoryHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.game_history_list_row, null);
            holder = new GameHistoryHolder();
            holder.when = (TextView) row.findViewById(R.id.when);
            holder.what = (TextView) row.findViewById(R.id.what);
            holder.who = (TextView) row.findViewById(R.id.who);

            row.setTag(holder);
        } else {
            holder = (GameHistoryHolder) row.getTag();
        }

        GameHistory gameHistory = gameHistroy.get(position);
        holder.when.setText(gameHistory.when);
        holder.what.setText(gameHistory.what);
        holder.who.setText(gameHistory.who);

        return row;
    }

    @Override
    public int getCount() {
        return gameHistroy.size();
    }

    static class GameHistoryHolder {
        TextView when;
        TextView what;
        TextView who;
    }
}
