package com.leila.android.fishy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {

    private static DateHelper INSTANCE = new DateHelper();

    private DateHelper() {

    }

    public static DateHelper get() {
        return INSTANCE;
    }

    public String getActualDate() {
        return actualDate2();
    }

    public String getActualDateToShow() {
        return actualDateToShow();
    }

    public String getOnlyDate(String date) {
        return onlyDate(date);
    }

    public String getOnlyDateComplete(String date) {
        return onlyDateComplete(date);
    }

    public String getOnlyTime(String date) {
        return onlyTime(date);
    }

    public String getNextDay(String date) {
        try {

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date1 = format1.parse(date);

            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            c.add(Calendar.DATE, 1);
            date1 = c.getTime();

            return format1.format(date1);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "dd/MM/yyyy";
    }

    public Date parseDate(String date){
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format1.setTimeZone(TimeZone.getDefault());
            Date date1 = format1.parse(date);
            return date1;
        }catch (Exception e){
            return null;
        }
    }

    public Date parseDateMonth(String date2){
        try {

            String date=onlyMonth2(date2);

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format1.setTimeZone(TimeZone.getDefault());
            Date date1 = format1.parse(date);


            return date1;
        }catch (Exception e){
            return null;
        }
    }

    public String getNextMonth(String date) {
        try {

            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            format1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date1 = format1.parse(date);

            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            c.add(Calendar.MONTH, 1);
            date1 = c.getTime();

            return format1.format(date1);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "dd/MM/yyyy";
    }

    public String getSumHours(String date,Integer duration) {
        try {

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date1 = format1.parse(date);

            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            c.add(Calendar.HOUR, duration);
            date1 = c.getTime();

            return format1.format(date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "dd/MM/yyyy";
    }


    public boolean compareDate(String dateFirst,String date){
        String onlyDate= onlyDate(date);
        String onlyDateFirst= onlyDate(dateFirst);
        // String today=onlyDate(getActualDate());

        if(onlyDateFirst.equals(onlyDate)){
            return true;
        }else{
            return false;
        }

    }

    private String actualDate() {
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(currentDate);
    }

    private String actualDate2() {
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(currentDate);
    }

    private String actualDateToShow() {
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(currentDate);
    }

    public String serverToUser(String date) {

        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date1 = format1.parse(date);
            format1.setTimeZone(TimeZone.getDefault());
            return format1.format(date1);
        } catch (ParseException e) {

        }
        return "";
    }


    public String serverToUserFormatted(String date) {

        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date1 = format1.parse(date);
            format1.setTimeZone(TimeZone.getDefault());
            return changeFormatDate(format1.format(date1));
        } catch (ParseException e) {

        }
        return "";
    }

    public String changeFormatDate(String date) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = format1.parse(date);
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            String stringdate2 = format2.format(date1);
            return stringdate2;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "dd/MM/yyyy";
    }



    public String changeFormatDateUserToServer(String date) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date1 = format1.parse(date);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String stringdate2 = format2.format(date1);
            return stringdate2;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "dd/MM/yyyy";
    }

    public String userToServer(String date) {

        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format1.setTimeZone(TimeZone.getDefault());
            Date date1 = format1.parse(date);

            format1.setTimeZone(TimeZone.getTimeZone("UTC"));
            return format1.format(date1);
        } catch (ParseException e) {

        }
        return "";
    }

    public String onlyTimeFromDate(String date) {
        String[] parts = date.split(" ");

        if (parts.length > 1) {
            String part1 = parts[0]; // fecha
            return parts[1];

        }else{
            return "";
        }

    }

    private String onlyDate(String date) {
        String[] parts = date.split(" ");

        if (parts.length > 1) {
            String part1 = parts[0]; // fecha
            return part1;

        }else{
            return "";
        }

    }

    public String onlyDateComplete(String date) {
        String[] parts = date.split(" ");
        String part1 = parts[0]; // fecha
        return part1 + " 00:00:00";
    }




    public String getMonthEvent(String date){
        String[] parts = date.split(" ");
        String part1 = parts[0]; // fecha

        String part3= onlyMonth3(part1);

        return part3;
    }

    public String getDayEvent(String date){
        String[] parts = date.split(" ");
        String part1 = parts[0]; // fecha

        String part3= onlyDay2(part1);

        if(Integer.valueOf(part3) < 10){
            return part3.substring(1);
        }else{
            return part3;
        }
    }

    public String getYearEvent(String date){
        String[] parts = date.split(" ");
        String part1 = parts[0]; // fecha

        String[] part2=part1.split("-");

        return part2[0];
    }

    public String onlyMonth3(String date) {
        String[] parts = date.split("-");
        String part1 = parts[0]; // año
        String part2 = parts[1]; // mes
        String part = parts[2]; // dia
        return part2 ;
    }

    public String onlyDay2(String date) {
        String[] parts = date.split("-");
        String part1 = parts[0]; // año
        String part2 = parts[1]; // mes
        String part = parts[2]; // dia
        return part ;
    }

    public String onlyDayMonth(String date) {
        String[] parts = date.split("-");
        String part1 = parts[0]; // año
        String part2 = parts[1]; // mes
        String part = parts[2]; // dia
        return part + "-" + part2;
    }

    public String getOnlymonth(String date) {
        String[] parts = date.split("-");
        String part1 = parts[0]; // dia
        String part2 = parts[1]; // mes
        String part = parts[2]; // año
        return part + "-" + part2 + "-00 00:00:00";
    }

    public String onlyMonth2(String date) {
        String[] parts = date.split("-");
        String part1 = parts[0]; // dia
        String part2 = parts[1]; // mes
        String part = parts[2]; // año
        return part + "-" + part2 + "-00 00:00:00";
    }

    public String getOnlyYear(String date) {
        String[] parts = date.split("-");
        String part1 = parts[0]; // año
        String part2 = parts[1]; // mes
        String part3 = parts[2]; // dia
        return part1;
    }

    public String onlyMonth(String date) {
        String[] parts = date.split("/");
        String part1 = parts[0]; // dia
        String part2 = parts[1]; // mes
        String part3 = parts[2]; // año
        return part2;
    }

    public String onlyDay(String date) {
        String[] parts = date.split("-");
        String part1 = parts[0]; // dia
        String part2 = parts[1]; // mes
        String part3 = parts[2]; // año
        return part1;
    }

    public String onlyDayAndMonth(String date) {
        if(date.length() > 0 ) {
            String[] parts = date.split("-");
            String part1 = parts[0]; // dia
            String part2 = parts[1]; // mes
            String part3 = parts[2]; // año
            System.out.println(date);
            System.out.println(part1+ part2);
            return part1 + "-" + part2;
        }else{
            return "dd-mm";
        }
    }

    public String getTime(String date){
        if(date.length() > 0 ) {
            String[] parts = date.split(" ");
            return parts[1];
        }else{
            return date;
        }

    }

    private String onlyTime(String date) {
        if(date.length() > 0 ){
            String[] parts = date.split(" ");
            if (parts.length > 1) {

                String[] parts2 = parts[1].split(":");

                System.out.println("date "+date+" hour"+parts2[0]+":"+parts2[1]);

                String hour=parts2[0];

                if(Integer.valueOf(hour) < 10){
                    return hour.substring(1)+":"+parts2[1]; // time
                }else{
                    return hour+":"+parts2[1]; // time
                }

            } else {
                return "";
            }

        }else{
            return "";
        }
    }

    public String onlyTimeRounded(String date) {
        if(date.length() > 0 ){
            String[] parts = date.split(" ");
            if (parts.length > 1) {

                String[] parts2 = parts[1].split(":");

                if(Integer.valueOf(parts2[0]) < 10){

                    return parts2[0].substring(1)+":00"; // time
                }else{
                    return parts2[0]+":00"; // time
                }

                // return parts2[0]+":00"; // time

            } else {
                return "";
            }

        }else{
            return "";
        }
    }

    public String getDateWhith(String date,String hour){

        if(date.length() > 0 ){
            String[] parts = date.split(" ");
            String onlyDate=parts[0];
            return onlyDate+" "+hour+":00";

        } else {
            return "";
        }
    }

    public String getNameOfDay(int year, int dayOfYear) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);

        return days[dayIndex - 1];
    }

    public String getActualHour(){

        String dateUp= getSumHours(DateHelper.get().getActualDate(),1);

        return onlyTimeRounded(dateUp);
    }



    public String getNameMonth(String date) {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            String monthName = new SimpleDateFormat("MM").format(cal.getTime());

            String[] monthNames = {"","Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
            return monthNames[Integer.valueOf(monthName)];
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getNumberDay(String input_date){
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date dt1 = format1.parse(input_date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(dt1);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            return day;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 2;
    }

    public String getNameDay(String input_date){
        try {

            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
            Date dt1=format1.parse(input_date);

            DateFormat format2=new SimpleDateFormat("EEE");
            String finalDay=format2.format(dt1);

            System.out.println(finalDay.substring(0,3));
            return getNamSpanish(finalDay.substring(0,3));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }

    public String getNamSpanish(String dayEnglish) {

        if(dayEnglish.equals("Sun")|| dayEnglish.equals("dom")){
            return "Dom";
        }else if(dayEnglish.equals("Mon") || dayEnglish.equals("lun")){
            return "Lun";
        }else if(dayEnglish.equals("Tue")|| dayEnglish.equals("mar")){
            return "Mar";
        }else if(dayEnglish.equals("Wed")|| dayEnglish.equals("mié")){
            return "Mie";
        }else if(dayEnglish.equals("Thu")|| dayEnglish.equals("jue")){
            return "Jue";
        }else if(dayEnglish.equals("Fri")|| dayEnglish.equals("vie")){
            return "Vie";
        }else if(dayEnglish.equals("Sat")|| dayEnglish.equals("sáb")){
            return "Sab";
        }else {
            return "juernes";
        }

    }


    public String getStringByDate(String date){

        //yyyy-mm-dd


        if(date.length() >0){
            String[] parts = getOnlyDate(date).split("-");

            String nameDay= getNamSpanish(getNameOfDay(Integer.valueOf(parts[0]),Integer.valueOf(parts[2])).substring(0,3));
            System.out.println(nameDay);
            String nameMonth=getNameMonth(date);

            System.out.println(nameMonth);


            String numDay="";
            if(Integer.valueOf(parts[2]) < 10){
                numDay=parts[2].substring(1);
            }else{
                numDay=parts[2];
            }

            return nameDay+" "+numDay+" de "+nameMonth+" "+parts[0];

        }else{
            return "yyyy-mm-dd";
        }



    }


    public String getDayStringByDate(String date){

        //yyyy-mm-dd


        if(date.length() >0){
            String[] parts = getOnlyDate(date).split("-");

            String nameDay= getNamSpanish(getNameOfDay(Integer.valueOf(parts[0]),Integer.valueOf(parts[2])).substring(0,3));
            String nameMonth=getNameMonth(date);

            String numDay="";
            if(Integer.valueOf(parts[2]) < 10){
                numDay=parts[2].substring(1);
            }else{
                numDay=parts[2];
            }

            return nameDay+" "+numDay;

        }else{
            return "yyyy-mm-dd";
        }



    }

    public String getStringByDateWithoutYear(String date){

        //yyyy-mm-dd


        if(date.length() >0){
            String[] parts = getOnlyDate(date).split("-");

            String nameDay= getNamSpanish(getNameOfDay(Integer.valueOf(parts[0]),Integer.valueOf(parts[2])).substring(0,3));
            System.out.println(nameDay);
            String nameMonth=getNameMonth(date);

            System.out.println(nameMonth);


            String numDay="";
            if(Integer.valueOf(parts[2]) < 10){
                numDay=parts[2].substring(1);
            }else{
                numDay=parts[2];
            }

            return nameDay+" "+numDay+" de "+nameMonth;

        }else{
            return "yyyy-mm-dd";
        }



    }



}
