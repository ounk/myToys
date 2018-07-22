package com.example.ounk.challenge;

import android.os.StrictMode;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


final class ServiceHandler {

    private static final String strUrl = "https://mytoysiostestcase1.herokuapp.com/api/navigation";

    static String LoadJson() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url;
            url = new URL(strUrl);
            HttpsURLConnection con;
            con = (HttpsURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setRequestProperty("x-api-key","hz7JPdKK069Ui1TRxxd1k8BQcocSVDkj219DVzzD");
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder response = new StringBuilder();

            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
//            Log.v("response: ", Integer.toString(responseCode));
//            Log.v("test: ",response.toString());
            return response.toString();
        }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

    }
