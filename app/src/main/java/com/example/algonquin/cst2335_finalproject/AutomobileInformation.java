package com.example.algonquin.cst2335_finalproject;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AutomobileInformation {
    public String time;
    public String gasPrice;
    public String gasVolume;

    public AutomobileInformation(){}

    public AutomobileInformation(String time, String gasPrice, String gasVolume){
        this.time = time;
        this.gasPrice = gasPrice;
        this.gasVolume = gasVolume;
    }

    public String [] parseDate(String time){
        String delim = "[.]";
        String [] tokens = time.split(delim);
        //String returnArray []  = new String [tokens.length];

        switch (tokens[1]){
            case "1": tokens[1] = "January";
                break;
            case "2": tokens[1] = "February";
                break;
            case "3": tokens[1] = "March";
                break;
            case "4": tokens[1] = "April";
                break;
            case "5": tokens[1] = "May";
                break;
            case "6": tokens[1] = "June";
                break;
            case "7": tokens[1] = "July";
                break;
            case "8": tokens[1] = "August";
                break;
            case "9": tokens[1] = "September";
                break;
            case "10": tokens[1] = "October";
                break;
            case "11": tokens[1] = "November";
                break;
            default: tokens[1] = "December";
                break;
        }


        return tokens;
    }

    public String [] parseDateInt(String time){
        String delim = "[.]";
        String [] tokens = time.split(delim);

        return tokens;
    }

    public Date stringToDate(String time){
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        try {
            Date date = format.parse(time);
            return date;
        }catch (Exception e){
            return null;

        }
    }

    public String parseYearMonth(String time){
        String delim = "[.]";
        String [] tokens = time.split(delim);
        String yearMonth = tokens[0] +"-"+ tokens[1]+"-"+tokens[2];


        return yearMonth;
    }

    public String parseYear (String time){
        String delim = "[.]";
        String [] tokens = time.split(delim);
        String yearMonth = tokens[0];

        return yearMonth;
    }

    public String parseMonth (String time){
        String delim = "[.]";
        String [] tokens = time.split(delim);
        String month = tokens[1];

        return month;
    }



}
