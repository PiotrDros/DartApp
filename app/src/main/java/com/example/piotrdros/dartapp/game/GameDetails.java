package com.example.piotrdros.dartapp.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotr.dros on 2016-04-25.
 */
public class GameDetails {

    String reportedOn;
    String winner;
    String playerCount;
    String playerPlacement;
    String reporter;
    String status;


    List<GameHistory> gameHistory;


    GameDetails() {
        gameHistory = new ArrayList<>();
    }



}
