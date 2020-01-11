package com.nasande.nasande;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Node {
    ArrayList<Type> type;
    private ArrayList<Title> title;
    private ArrayList<Fichier> field_fichier_audio;






    public Node(ArrayList<Title> title, ArrayList<Fichier> field_fichier_audio) {

        ArrayList<Type> type = new ArrayList<>();
        type.add(0,new Type("audio"));


        this.title = title;


        this.type = type;



        this.field_fichier_audio = field_fichier_audio;
    }



    public ArrayList<Title> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<Title> title) {
        this.title = title;
    }

    public ArrayList<Fichier> getField_fichier_audio() {
        return field_fichier_audio;
    }

    public void setField_fichier_audio(ArrayList<Fichier> field_fichier_audio) {
        this.field_fichier_audio = field_fichier_audio;
    }
}
