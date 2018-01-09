package dashboard.jmtechmind.com.jmdashboard;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * Created by vishal on 11/7/2017.
 */


public class ViewPaidUnPaidActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Intent i;
    Context mcontext;
    SearchView inputSearch;
    List<FeedItem> filteredModelList;


    OffersRootAdapter adapter;
    boolean lang = false;
    RecyclerView mRecyclerView;
    View emptyView;
    View view;
    boolean isfilter = false;
    SwipeRefreshLayout swipeContainer;
    TextView llcandi;
    private List<FeedItem> feedsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_main);
        mcontext = this;

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Tenant List");
        }

        emptyView = findViewById(R.id.null_layout);
        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
        inputSearch = findViewById(R.id.search_bar);
        inputSearch.setQueryHint("Search Tenant Name");

        new OfferAsyncHttpTask().execute();

        inputSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enabling touch anywhrer fuction of searchbar -------------
                // inputSearch.onActionViewExpanded();
                inputSearch.setIconified(false);
            }
        });

        inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                isfilter = true;
                filteredModelList = filter(feedsList, newText);
                adapter.setFilter(filteredModelList);
                return true;

            }

        });

        swipeContainer.setOnRefreshListener(this);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

//        swipeContainer.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeContainer.setRefreshing(true);
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
//            Intent i = new Intent(AboutUsActivity.this, MainActivity.class);
//            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private List<FeedItem> filter(List<FeedItem> models, String query) {
        query = query.toLowerCase();

        final List<FeedItem> filteredModelList = new ArrayList<>();
        for (FeedItem model : models) {
            final String text = model.getProprietor_name1().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);

            }
        }
        return filteredModelList;
    }

    @Override
    public void onRefresh() {
        new OfferAsyncHttpTask().execute();
    }


    //OLDDDD fething offers Task--------------------------------------------------------------------------------------------
    public class OfferAsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            swipeContainer.setRefreshing(true);


        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Integer doInBackground(String... params) {
            try {
//                http://10.10.10.53/tenantname.phphttp://localhost/tenantname.php?status=unpaid
                URL url = new URL("http://10.10.10.53/tenantname.php?status=unpaid");

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

                Log.d("     Data", s);


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
            swipeContainer.setRefreshing(false);


            adapter = new OffersRootAdapter(mcontext, feedsList);
            mRecyclerView.setAdapter(adapter);


            if (adapter.getItemCount() == 0) {
                mRecyclerView.setVisibility(GONE);
                emptyView.setVisibility(VISIBLE);
            } else {
                mRecyclerView.setVisibility(VISIBLE);
                emptyView.setVisibility(GONE);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void parseResult(String result) {

        try {

            feedsList = new ArrayList<>();


            JSONArray jsonarray = new JSONArray(result);
            for (int i = 0; i < jsonarray.length(); i++) {

                FeedItem item = new FeedItem();

                JSONObject post = jsonarray.getJSONObject(i);

                item.setProprietor_name1(post.optString("proprietor_name1"));
                item.setDate(post.optString("date"));
                item.setAdmin_name(post.optString("admin_name"));
                item.setTotal(post.optString("total"));
                item.setIs_viewed(post.optString("is_viewed"));
                item.setPaid_un_paid(post.optString("paid_un_paid"));

                feedsList.add(item);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

