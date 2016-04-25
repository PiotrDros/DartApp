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

public class GameAdapter extends ArrayAdapter<Game> {

    private final Context context;
    List<Game> games;

    public GameAdapter(Context context, List<Game> games) {
        super(context, R.layout.game_list_row);
        this.games = games;
        this.context = context;

    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GameHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.game_list_row, null);
            holder = new GameHolder();
            holder.id = (TextView) row.findViewById(R.id.id);
            holder.added = (TextView) row.findViewById(R.id.added);
            holder.done = (TextView) row.findViewById(R.id.done);
            holder.players = (TextView) row.findViewById(R.id.players);
            holder.reporter = (TextView) row.findViewById(R.id.reporter);
            holder.status = (TextView) row.findViewById(R.id.status);

            row.setTag(holder);
        } else {
            holder = (GameHolder) row.getTag();
        }

        Game game = games.get(position);
        if (holder.id != null) {
            holder.id.setText(game.id);
        }
        holder.added .setText(game.added);
        holder.done.setText(game.done);
        if(holder.players != null) {
            holder.players .setText(game.players);
        }
        holder.reporter.setText(game.reporter);
        holder.status  .setText(game.status);


        return row;
    }

    @Override
    public int getCount() {
        return games.size();
    }

    static class GameHolder {
        TextView id;
        TextView added;
        TextView done;
        TextView players;
        TextView reporter;
        TextView status;
    }
}
