package tig167.com.helpmusic;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLSender extends AsyncTask<String, Void, String> {

    public String doInBackground(String... params){
        String data = "";

        HttpURLConnection connection = null;
        try{

            connection = (HttpURLConnection) new URL(params[0]).openConnection();
            connection.setRequestMethod("POST");

            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            wr.writeBytes(params[1] + params[2]);
            wr.flush();
            wr.close();

            InputStream in = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            Log.d("DATA_Tag", data + " inputstremdata: " + inputStreamData);
            while(inputStreamData != -1) {
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                data += current;
            }
            System.out.println("TAG " + data);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if(connection != null){
                connection.disconnect();
            }
        }

        return data;
    }

    public void onPostExecute(String result){
        super.onPostExecute(result);
        Log.e("TAG", result);
    }
}
