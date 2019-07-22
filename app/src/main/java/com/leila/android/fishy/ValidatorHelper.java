package com.leila.android.fishy;

public class ValidatorHelper {

    private static ValidatorHelper INSTANCE = new ValidatorHelper();

    private ValidatorHelper(){

    }

    public static ValidatorHelper get(){
        return INSTANCE;
    }

    public boolean isTypeInteger(String testStr){
            try{
                Integer.parseInt(testStr);
                return true;
            } catch(Exception e) {
                return false;
            }
    }

    public Boolean isTypeDouble(String testStr) {

        try{
            Double.parseDouble(testStr);
            return true;
        } catch(Exception e) {
            return false;
        }

    }
}
