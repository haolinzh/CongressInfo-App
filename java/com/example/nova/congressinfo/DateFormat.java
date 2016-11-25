package com.example.nova.congressinfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NOVA on 16/11/23.
 */

public class DateFormat {
    public static String dateFormat(String src){
        String dst;
        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd,yyyy");

        try {
            date=sdf.parse(src);
            dst = sdf2.format(date);

            return dst;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }


}
