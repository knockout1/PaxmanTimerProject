package org.knockout.paxmantimerproject;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.knockout.paxmantimerproject.R.id.dateTextView;

public class GetMethodSupport  extends AsyncTask <String, Void, String> {
    private String con = new String();

    protected String doInBackground(String... urls) {

        try{
            URL url = new URL("http://10.0.2.2:8080/PaxmanWebService_war_exploded/helloworld");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            //urlConnection.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (urlConnection.getInputStream())));

            String output;

            while ((output = br.readLine()) != null) {
               con = con+output;
            }
            urlConnection.disconnect();

        }catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    protected void onPostExecute(TextView textView) {
        textView.setText(con);

    }
}
