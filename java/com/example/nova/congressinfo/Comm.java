package com.example.nova.congressinfo;

/**
 * Created by NOVA on 16/11/24.
 */

public class Comm {
    String commid;
    String name;
    String chamber;


    Comm(String commid,String name,String chamber){
        this.commid=commid;
        this.name=name;
        this.chamber=chamber;
    }

    public String getCommid(){
        return commid;
    }

    public String getName(){
        return name;
    }

    public String getChamber(){
        return chamber;
    }
}
