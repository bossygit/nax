package com.nasande.nasande.model;

import java.util.ArrayList;

public class Notification {
    ArrayList<Type> type;
    private ArrayList<Title> title;
    private ArrayList<Amount> field_amount;
    private ArrayList<Telephone> field_telephone_number;
    private ArrayList<Body> body;

    public Notification(ArrayList<Title> title, ArrayList<Amount> field_amount,  ArrayList<Telephone> field_telephone_number) {
        ArrayList<Type> type = new ArrayList<>();
        type.add(0,new Type("notification"));
        this.title = title;
        this.field_amount = field_amount;
        this.field_telephone_number = field_telephone_number;
    }


}
