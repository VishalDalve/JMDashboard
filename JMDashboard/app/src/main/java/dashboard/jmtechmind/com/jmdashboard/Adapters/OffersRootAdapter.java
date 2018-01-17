package dashboard.jmtechmind.com.jmdashboard.Adapters;

/**
 * Created by Vishal on 2/1/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dashboard.jmtechmind.com.jmdashboard.R;
import dashboard.jmtechmind.com.jmdashboard.Utils.FeedItem;


public class OffersRootAdapter extends RecyclerView.Adapter<OffersRootAdapter.CustomViewHolder> {
    private List<FeedItem> feedItemList;
    private Context mContext;


    public OffersRootAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.root_row, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);


//        notifyItemRemoved(i);
//        notifyItemRangeChanged(i, feedItemList.size());


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {
        final FeedItem feedItem = feedItemList.get(i);

        //Download image using picasso library
//        try {
//            Picasso.with(mContext).load(feedItem.getApp_image())
//                    .error(R.drawable.ic_search)
//                    .placeholder(R.drawable.ic_search)
//    //                .fit()
//    //                .transform(transformation)
//                    .into(customViewHolder.app_image);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        String date = feedItem.getPickup_time();
//        String s2 = feedItem.getPickup_time().substring(0, 8);
        customViewHolder.app_name.setText(feedItem.getProprietor_name1());
        // customViewHolder.app_description.setText("Mobile No : "+feedItem.getApp_desc());
        customViewHolder.app_price.setText("Rent Ststus: " + feedItem.getPaid_un_paid());
        customViewHolder.app_duration.setText("Date : " + feedItem.getDate());
        customViewHolder.total.setText("Rent Amount : Rs " + feedItem.total);

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView app_name, app_price, app_duration,total;
        public TextView app_description;
        ImageView app_image;


        public CustomViewHolder(View view) {
            super(view);
            this.app_name = (TextView) view.findViewById(R.id.tv_title);

            //this.app_description = (TextView) view.findViewById(R.id.tv_subtitle);
            //this.app_image = (ImageView) view.findViewById(R.id.imageView);

            this.app_price = (TextView) view.findViewById(R.id.tv_price);
            this.total = (TextView) view.findViewById(R.id.tv_amount);
            this.app_duration = (TextView) view.findViewById(R.id.tv_duration);
        }
    }

    public void setFilter(List<FeedItem> countryModels) {
        feedItemList = new ArrayList<>();
        feedItemList.addAll(countryModels);
        notifyDataSetChanged();
    }

}
