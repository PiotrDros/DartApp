package com.example.piotrdros.dartapp.game;

/**
 * Created by piotr.dros on 2016-04-22.
 */
public class Game {
    String id;
    String added;
    String done;
    String players;
    String reporter;
    String status;

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", added='" + added + '\'' +
                ", done='" + done + '\'' +
                ", players='" + players + '\'' +
                ", reporter='" + reporter + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
