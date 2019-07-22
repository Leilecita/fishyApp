package com.leila.android.fishy;

public class CurrentValuesHelper {


    private static CurrentValuesHelper INSTANCE = new CurrentValuesHelper();

    //OrdersFragment
    private String mLastDate;
    private String mLastTimeZone;
    private String mLastZone;
    private boolean mOnlyAddress;
    //SummaryDay
    private String mSummaryDate;

    private CurrentValuesHelper(){
        this.mLastDate=null;
        this.mLastZone="Zona";
        this.mLastTimeZone="Horario";
        this.mOnlyAddress=false;
        this.mSummaryDate=null;

    }
    public void setmOnlyAddress(boolean address){this.mOnlyAddress=address;}
    public boolean getOnlyAddress(){return this.mOnlyAddress;}
    public void setLastDate(String date){
        this.mLastDate=date;
    }
    public void setLastTimeZone(String date){
        this.mLastTimeZone=date;
    }
    public void setLastZone(String date){
        this.mLastZone=date;
    }
    public void setSummaryDate(String date){
        this.mSummaryDate=date;
    }
    public String getLastDate(){
        return this.mLastDate;
    }
    public String getSummaryDate(){
        return this.mSummaryDate;
    }
    public String getLastTimeZone(){return this.mLastTimeZone;}
    public String getLastZone(){return this.mLastZone;}

    public static CurrentValuesHelper get(){
        return INSTANCE;
    }
}
