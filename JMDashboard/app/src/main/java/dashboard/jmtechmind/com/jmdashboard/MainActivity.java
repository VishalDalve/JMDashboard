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
import dashboard.jmtechmind.com.jmdashboard.Utils.GetReaponse;
import dashboard.jmtechmind.com.jmdashboard.Utils.Webservices;

public class MainActivity extends AppCompatActivity {

    PieChart pieChart;
    int ipaid = 0, iunpaid = 0;
    GetReaponse gr;
    TextView vendorlist, polist, customerpynt, salesreport, salescutlist, salesgst, purchasegast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gr = new GetReaponse();

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

        teneantmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ViewPaidUnPaidActivity.class);
                startActivity(i);
            }
        });


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
                // making request to server & parsing the data ------------------------
                parseResult(gr.GetResponses(Webservices.WEBSERVICE_TENANT_PAID_UNPAIS));

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

            Legend l = pieChart.getLegend();
            l.setEnabled(false);

            pieChart.setDescription(null);
            pieChart.setData(data);

            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void parseResult(String result) {

        try {
            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {

                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String paid = jsonobject.getString("Paid");
                String unpaid = jsonobject.getString("Unpaid");

                ipaid = Integer.parseInt(paid);
                iunpaid = Integer.parseInt(unpaid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
