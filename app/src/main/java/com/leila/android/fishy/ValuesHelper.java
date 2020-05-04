package com.leila.android.fishy;

public class ValuesHelper {

    private static ValuesHelper INSTANCE = new ValuesHelper();

    public Integer mPreviousPosition=0;

    private ValuesHelper(){

    }

    public static ValuesHelper get(){
        return INSTANCE;
    }


    public String getIntegerQuantityByLei(Double val){

        String[] parts = String.valueOf(val).split(".");
        if(Integer.valueOf(parts[1]) == 0){
            return parts[0];
        }else{
            return String.valueOf(val);
        }

    }

    public String getIntegerQuantity(Double val){
        String[] arr= String.valueOf(val).split("\\.");
        int[] intArr=new int[2];
        intArr[0]= Integer.parseInt(arr[0]);
        intArr[1]= Integer.parseInt(arr[1]);

        if(intArr[1] == 0){
            return String.valueOf(intArr[0]);
        }else{
            return String.valueOf(val);
        }
    }

    public Double roundTwoDecimals(double d)
    {
        double roundOff = (double) Math.round(d * 100) / 100;

        // DecimalFormat twoDForm = new DecimalFormat("#.##");
        //return Double.valueOf(twoDForm.format(d));
        return roundOff;
    }



    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        System.out.println(String.valueOf((double) tmp / factor));
        return (double) tmp / factor;
    }
}
