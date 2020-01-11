package com.nasande.nasande;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Node {
    private Map _links;
    private ArrayList<Title> title;
    private ArrayList<Fichier> field_fichier_audio;


    public Node(ArrayList<Title> title, ArrayList<Fichier> field_fichier_audio) {
        Map m = new LinkedHashMap(1);
        JSONObject jo = new JSONObject();
        try {
            jo.put("href","http://nasande.cg/rest/type/node/audio");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        m.put("type",jo);

        this._links = m;
        this.title = title;
        this.field_fichier_audio = field_fichier_audio;
    }

    public Map get_links() {
        return _links;
    }

    public void set_links(Map _links) {
        this._links = _links;
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
