package com.example.scrummaster.controller;


import java.util.ArrayList;

public class Participant {

    ArrayList<String> list = new ArrayList<>();
    String name;


    //Methode geht die Liste durch und gibt den Participant aus
    public String goThrouhList(ArrayList<String> l){
        for ( String s : l) {
            name = s;
        }
        return name;
    }

}
