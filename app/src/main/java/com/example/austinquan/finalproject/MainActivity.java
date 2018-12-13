package com.example.austinquan.finalproject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}
//add dependencies to your class

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button search = findViewById(R.id.button);
        final EditText name = findViewById(R.id.editText);
        String edtext = name.getText().toString();
        int length = edtext.length();
        final String web = "https://od-api.oxforddictionaries.com:443/api/v1/entries/en/";
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = name.getText().toString();

                new CallbackTask().execute(dictionary(input));
            }
        });
    }


    private String dictionary(String input) {
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/en/" + input.toLowerCase();
    }


    //in android calling network requests on the main thread forbidden by default
    //create class to do async job
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            //TODO: replace with your own app id and app key
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id","10742428");
                urlConnection.setRequestProperty("app_key","ada344f3a7a7c7de0315fb78c5c9d6f9");
                String address = "https://od-api.oxforddictionaries.com:443/api/v1/entries/en/";

                // output
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = "\"https://od-api.oxforddictionaries.com:443/api/v1/entries/en/\"";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                return stringBuilder.toString();

            }
            catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }
        JsonArray results;
        JsonArray lexical;
        JsonArray entries;
        JsonArray senses;
        JsonArray definitions;
        JsonElement print;
        JsonElement lexicalpool ;
        JsonElement entriespool;
        JsonElement sensespool;
        JsonElement definitionspool;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            JsonParser jp = new JsonParser();

            JsonObject jo = jp.parse(result).getAsJsonObject();

            //String message = jo.get("result").getAsString();





            results = jo.get("results").getAsJsonArray();
            lexicalpool = results.get(0);
            lexical = lexicalpool.getAsJsonObject().get("lexicalEntries").getAsJsonArray();
            entriespool = lexical.get(0);
            entries =  entriespool.getAsJsonObject().get("entries").getAsJsonArray();
            sensespool = entries.get(0);
            senses =  sensespool.getAsJsonObject().get("etymologies").getAsJsonArray();
            print = senses.get(0);
            String define = print.getAsString();


            //get("lexicalEntries").getAsJsonObject().get("entries").getAsJsonObject().get("senses").getAsJsonObject().get("definitions").getAsString();








            //System.out.println(result);
            TextView output = findViewById(R.id.textView2);

            output.setText(define);

            //text.setText(result);

        }
    }
}