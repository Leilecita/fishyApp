package com.leila.android.fishy.network;

/**
 * Created by leila on 17/7/18.
 */

public class ApiUtils {

        private ApiUtils() {}

       //public static fin al String BASE_URL = "http://192.168.0.87/fishyserver/";
       public static final String BASE_URL = "http://192.168.88.9/fishyserver/";
       // public static final String BASE_URL = "http://192.168.0.10/fishyserver/";

    //FISHY MARDEL
        // public static final String BASE_URL = "http://fishy.abarbieri.com.ar/";

    //FISHY ROSARIO
        // public static final String BASE_URL = "http://fishydev.abarbieri.com.ar/";

    public static APIService getAPIService() {
            return RetrofitClient.getClient(BASE_URL).create(APIService.class);
        }

        public static final String getImageUrl(String imagePath){
            if(imagePath.startsWith("/")){
                imagePath = imagePath.replaceFirst("/","");
            }
            return BASE_URL + imagePath;
        }
}

