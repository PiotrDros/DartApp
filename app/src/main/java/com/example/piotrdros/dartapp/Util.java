package com.example.piotrdros.dartapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String getAppUrl (Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();


        String appUrl = prefs.getString(
                resources.getString(R.string.pref_app_url),
                resources.getString(R.string.pref_app_url_default_value));

        return  appUrl;
    }


    public static class Rule {
        String header;
        String description;

        public Rule(String header, String description) {
            this.header = header;
            this.description = description;
        }
    }

    public static List<Rule> rules = new ArrayList<>();
    static {
        rules.add(new Rule("Register Properly", "Register with your name or nickname so that everyone knows who you are. Keep it short and simple attempts to be a smartbottom will most likely earn you appropriate nickname."));
        rules.add(new Rule("Type of games you may play", "Generally best option is to play a \"pro\" game which is 2 player 501 double out. But you may play whatever you want as long as there are 2-8 participants and there are no draws alowed. You can also report a game where not everyone agreed to play ranked. In that case simply report those players that played ranked and omit those that did not."));
        rules.add(new Rule("Throwing order", "Players should throw in same order as they appear on ranking page at the time game starts."));
        rules.add(new Rule("General rules", "a. Winner reports game using \"Report Game\" link.\n" +
                "b. Game gets accepted and ranking gets calculated after all participants confirm their position.\n" +
                "c. Machine is always right. Only alowed interference with board is if dart stays on board but for some reason the board did not accept that thow. In that case player should push the dart to score.\n" +
                "d. If someone forgets to switch player and next player throws a dart that stays on board, then that player can choose whether to remove dart and then switch player to repeat throw or switch player and push the dart to score (according to above rule c).\n" +
                "e. Players need to play at least one game to be ranked."));
    }


}
