package dashboard.jmtechmind.com.jmdashboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import dashboard.jmtechmind.com.jmdashboard.Activities.CustPaymentList;
import dashboard.jmtechmind.com.jmdashboard.Activities.PurchaseGST;
import dashboard.jmtechmind.com.jmdashboard.Activities.SalesCustList;
import dashboard.jmtechmind.com.jmdashboard.Activities.SalesGST;
import dashboard.jmtechmind.com.jmdashboard.Activities.VendorList;
import dashboard.jmtechmind.com.jmdashboard.Activities.ViewPaidUnPaidActivity;
import dashboard.jmtechmind.com.jmdashboard.Utils.FeedItem;

public class MainActivity extends AppCompatActivity {

    PieChart pieChart;
    int ipaid = 0, iunpaid = 0;
    TextView vendorlist,polist,customerpynt,salesreport,salescutlist,salesgst,purchasegast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vendorlist = findViewById(R.id.vendor_report);
        polist = findViewById(R.id.propo_list);
        customerpynt = findViewById(R.id.tv_cust_list);
        salesreport = findViewById(R.id.tv_sales_list);
        salescutlist = findViewById(R.id.tv_cust_sales_list);
        salesgst = findViewById(R.id.tv_sales_gst);
        purchasegast = findViewById(R.id.tv_purchase_gst);


        TextView teneantmore = findViewById(R.id.tv_viewmore);
        pieChart = findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
//        pieChart.setClickable(true);
        teneantmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ViewPaidUnPaidActivity.class);
                startActivity(i);
            }
        });

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.


        new OfferAsyncHttpTask().execute();

        vendorlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, VendorList.class);
                startActivity(i);
            }
        });

        customerpynt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CustPaymentList.class);
                startActivity(i);
            }
        });


        salesreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Sales Report in progress", Toast.LENGTH_SHORT).show();

            }
        });

        salescutlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, SalesCustList.class);
                startActivity(i);

            }
        });

        salesgst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SalesGST.class);
                startActivity(i);
            }
        });

        purchasegast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PurchaseGST.class);
                startActivity(i);
            }
        });


    }

    //OLDDDD fething offers Task--------------------------------------------------------------------------------------------
    public class OfferAsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            // UtilMethod.ShowProgressDialogue(getContext(), "Fetching Offers");
//            swipeContainer.setRefreshing(true);


        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Integer doInBackground(String... params) {
            try {
//                http://10.10.10.53/tenantname.php
                URL url = new URL("http://10.10.10.53/tenant_paid_unpaid.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

//                ApiCrypter apicry = new ApiCrypter();
//                String data_web = "";

                String encryptedRequest = "";
//                try {
//                    encryptedRequest = apicry.encrypt(data_web);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                String urlParameters = encryptedRequest;

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


//                try {
//                    apicry = new ApiCrypter();
//                    String res = ApiCrypter.decrypt(s);
//                    s = URLDecoder.decode(res, "UTF-8");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                Log.d("crytoData", s);


                parseResult(s);


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            //swipeContainer.setRefreshing(false);

            ArrayList<Entry> yvalues = new ArrayList<Entry>();
            yvalues.add(new Entry(ipaid, 0));
            yvalues.add(new Entry(iunpaid, 1));

            PieDataSet dataSet = new PieDataSet(yvalues, "Tenant Payment Status");

            ArrayList<String> xVals = new ArrayList<String>();

            xVals.add("Paid");
            xVals.add("Unpaid");


            PieData data = new PieData(xVals, dataSet);

            // In percentage Term
            data.setValueFormatter(new PercentFormatter());
            // Default value
            //data.setValueFormatter(new DefaultValueFormatter(0));

            Legend l = pieChart.getLegend();
            l.setEnabled(false);
//            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
//            l.setXEntrySpace(7f);
//            l.setYEntrySpace(5f);
//            l.setYOffset(0f);

//            pieChart .getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//            pieChart .getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
//            pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
//            pieChart .getLegend().setFormSize(0f);

            pieChart.setDescription(null);
            pieChart.setData(data);

            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void parseResult(String result) {

        try {

            //  feedsList = new ArrayList<>();


            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {

                FeedItem item = new FeedItem();

                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String paid = jsonobject.getString("Paid");
                String unpaid = jsonobject.getString("Unpaid");

                ipaid = Integer.parseInt(paid);
                iunpaid = Integer.parseInt(unpaid);

                System.out.println("Paid : " + ipaid + " " + iunpaid);
            }

//            JSONArray jsonarray = response.getJSONArray("result");
//
//            for (int i = 0; i < jsonarray.length(); i++) {
//                JSONObject post = jsonarray.getJSONObject(i);

            // FeedItemnw item = new FeedItemnw();

//                if(lang) {
//                    item.setApp_name(post.optString("titlehin"));
//                    item.setApp_desc(post.optString("deschin"));
//
//
//                }else {
//                item.setApp_name(post.optString("MarketName"));
//                item.setApp_desc(post.optString("MarketCurrencyLong"));
//                item.setApp_image(post.optString("LogoUrl"));
//                item.setApp_24h(post.optString("24h_volume_usd"));
//                item.setApp_price(post.optString("Last"));
////                item.setApp_down_link(post.optString("afterapplink"));
////                }
//                feedsList.add(item);


//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
