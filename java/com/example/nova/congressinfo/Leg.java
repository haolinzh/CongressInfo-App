package com.example.nova.congressinfo;

/**
 * Created by NOVA on 16/11/20.
 */

public class Leg {
//    private String picUrl;
    private String name;
    private String party;
    private String state;
    private String district;

    Leg(String name,String party,String state,String district){
//        this.picUrl=picUrl;
        this.name=name;
        this.party=party;
        this.state=state;
        this.district=district;
    }

//    public String getPicUrl() {
//        return picUrl;
//    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

}
