package com.example.nova.congressinfo;

/**
 * Created by NOVA on 16/11/20.
 */

public class Bill {
    private String id;
    private String title;
    private String date;


    Bill(String id,String title,String date){
        this.id=id;
        this.title=title;
        this.date=date;
    }

    public String getBillId(){
        return id;
    }
    public String getBillTitle(){
        return title;
    }
    public String getBillDate(){
        return date;
    }

}
