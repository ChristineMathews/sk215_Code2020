package com.sih.hawkeye.ocr;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sih.hawkeye.Issue;
import com.sih.hawkeye.R;
import com.sih.hawkeye.RecyclerViewClickListener;
import com.sih.hawkeye.RecyclerViewHolder;

import java.util.List;

/**
 * Created by JithinJude on 15-03-2018.
 */

public class VehicleAdapter extends RecyclerView.Adapter<VehicleRecyclerViewHolder> {

    Context context;
    private List<Vehicle> vehicleList;

    private LayoutInflater mInflater;

    // data is passed into the constructor
    VehicleAdapter(Context context, List<Vehicle> Vehicle) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.vehicleList = Vehicle;
    }

    // inflates the row layout from xml when needed
    @Override
    public VehicleRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item_vehicle, parent, false);
        return new VehicleRecyclerViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(VehicleRecyclerViewHolder holder, int pos) {
        holder.title.setText(vehicleList.get(pos).vehicleRegNo);
        holder.date.setText(vehicleList.get(pos).vehicleReportDate);
        holder.blockAndRoom.setText(vehicleList.get(pos).vehicleOffence);

        Glide.with(context)
                .load(vehicleList.get(pos).vehicleImageUrl)
                .centerCrop()
                .placeholder(R.drawable.loading_image)
                .into( holder.imageView);

/*        try {
            Bitmap imageBitmap = decodeFromFirebaseBase64(issueList.get(pos).imageEncoded);
            holder.imageView.setImageBitmap(imageBitmap);
        }catch (Exception e){
            e.printStackTrace();
        }*/

/*        holder.setItemClickListener(new RecyclerViewClickListener() {
            @Override
            public void onItemClick(int pos) {}
        });*/
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return vehicleList.size();
        //return mCategoryRecyclerviewData.size();
    }
/*    public static Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }*/
}
