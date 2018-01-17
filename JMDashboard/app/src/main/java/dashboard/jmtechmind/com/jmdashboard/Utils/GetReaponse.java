package dashboard.jmtechmind.com.jmdashboard.Utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jmtec on 1/17/2018.
 */

public class GetReaponse {

    public String GetResponses(Context mcontext, String Addrurl) throws MalformedURLException, IOException {

        URL url = new URL(Addrurl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String urlParameters = "";

        //  Log.d("BOOKING_params", urlParameters);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
        connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
        connection.setDoOutput(true);
        DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
        dStream.writeBytes(urlParameters);
        dStream.flush();
        dStream.close();
        // int responseCode = connection.getResponseCode();
        final StringBuilder output = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        StringBuilder responseOutput = new StringBuilder();
        while ((line = br.readLine()) != null) {
            responseOutput.append(line);
        }
        br.close();

        output.append(responseOutput.toString());


        String s = output.toString();


        return s;


    }


}
