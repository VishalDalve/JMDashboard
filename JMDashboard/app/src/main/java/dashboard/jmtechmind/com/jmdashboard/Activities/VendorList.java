package dashboard.jmtechmind.com.jmdashboard.Activities;

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
import java.util.List;

import dashboard.jmtechmind.com.jmdashboard.Adapters.OffersRootAdapter;
import dashboard.jmtechmind.com.jmdashboard.R;
import dashboard.jmtechmind.com.jmdashboard.Utils.FeedItem;
import dashboard.jmtechmind.com.jmdashboard.Utils.GetReaponse;
import dashboard.jmtechmind.com.jmdashboard.Utils.Webservices;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by jmtec on 1/6/2018.
 */

public class VendorList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Context mcontext;
    SearchView inputSearch;
    List<FeedItem> filteredModelList;
    OffersRootAdapter adapter;
    RecyclerView mRecyclerView;
    View emptyView;
    boolean isfilter = false;
    SwipeRefreshLayout swipeContainer;
    private List<FeedItem> feedsList;
    GetReaponse gr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_main);
        mcontext = this;
        gr = new GetReaponse();

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Vendor List");
        }

        emptyView = findViewById(R.id.null_layout);
        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
        inputSearch = findViewById(R.id.search_bar);
        inputSearch.setQueryHint("Search Vendor Name");

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

                // making request to server & parsing the data ------------------------
                parseResult(gr.GetResponses(Webservices.WEBSERVICE_VENDOR_LIST));

            } catch (IOException e) {
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

                item.setProprietor_name1(post.optString("Name"));
                item.setDate(post.optString("CreatedDate"));
                item.setAdmin_name(post.optString("Address"));
                item.setTotal(post.optString("Mobile"));
                item.setIs_viewed(post.optString("GSTIN"));
                item.setPaid_un_paid(post.optString("PaidAmount"));

                feedsList.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
