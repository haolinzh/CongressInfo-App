package com.example.nova.congressinfo;

import java.io.Serializable;

/**
 * Created by NOVA on 16/11/24.
 */

public class Comm implements Serializable {
    String commid;
    String name;
    String chamber;
    String phone;
    String pid;
    String office;


    Comm(String commid,String name,String chamber,String phone,String pid,String office){
        this.commid=commid;
        this.name=name;
        this.chamber=chamber;
        this.phone=phone;
        this.pid=pid;
        this.office=office;
    }

    public String getCommId(){
        return commid;
    }

    public String getCommName(){
        return name;
    }

    public String getCommChamber(){
        return chamber;
    }

    public String getCommPhone(){
        return phone;
    }

    public String getCommPid(){
        return pid;
    }

    public String getCommOffice(){
        return office;
    }


}
