package com.example.piotrdros.dartapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RuleAdapter extends ArrayAdapter<Util.Rule> {

    private final Context context;
    List<Util.Rule> rules;
    private TypedArray orderedListImages;

    public RuleAdapter(Context context, List<Util.Rule> rules) {
        super(context, R.layout.rule_list_row);
        this.rules = rules;
        this.context = context;

        orderedListImages = context.getResources().obtainTypedArray(R.array.ordered_list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RuleHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.rule_list_row, null);
            holder = new RuleHolder();
            holder.icon = (ImageView) row.findViewById(R.id.icon);
            holder.header = (TextView) row.findViewById(R.id.header);
            holder.description = (TextView) row.findViewById(R.id.description);

            row.setTag(holder);
        } else {
            holder = (RuleHolder) row.getTag();
        }

        Util.Rule rule = rules.get(position);
        holder.icon.setImageResource(orderedListImages.getResourceId(position, R.drawable.ordered_list_0));
        holder.header.setText(rule.header);
        holder.description.setText(rule.description);


        return row;
    }

    @Override
    public int getCount() {
        return rules.size();
    }

    static class RuleHolder {
        ImageView icon;
        TextView header;
        TextView description;
    }
}
