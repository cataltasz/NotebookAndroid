package com.cataltas.notebookum.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static String getCurrentTimeStamp(){
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
            String currentDate = dateFormat.format(new Date());
            String[] parts = currentDate.split("-");
            return getMonthFromNumber(parts[0]) + " " + parts[1];
        }catch (Exception e){
            return "Jan 2019";
        }
    }

    public static String getMonthFromNumber(String monthNumber){
        switch (monthNumber){
            case "01":
                return "JAN";
            case "02":
                return "FEB";
            case "03":
                return "MAR";
            case "04":
                return "APR";
            case "05":
                return "MAY";
            case "06":
                return "JUN";
            case "07":
                return "JUL";
            case "08":
                return "AUG";
            case "09":
                return "SEP";
            case "10":
                return "OCT";
            case "11":
                return "NOV";
            case "12":
                return "DEC";
            default:
                return "NON";
        }
    }
}
