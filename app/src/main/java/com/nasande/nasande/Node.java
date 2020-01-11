package com.nasande.nasande;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Node {
    private Link _links;
    private ArrayList<Title> title;
    private ArrayList<Fichier> field_fichier_audio;


    public Link get_links() {
        return _links;
    }

    public void set_links(Link _links) {
        this._links = _links;
    }

    public Node(ArrayList<Title> title, ArrayList<Fichier> field_fichier_audio) {

        Type type = new Type("http://nasande.cg/rest/type/node/audio");

        Link link = new Link(type);

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
