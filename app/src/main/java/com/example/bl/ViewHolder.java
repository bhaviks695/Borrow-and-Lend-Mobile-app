package com.example.bl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bl.util.UniversalImageLoader;


public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

    }

    public void setDetails(Context ctx, String name, String image){

        TextView mitemname = mView.findViewById(R.id.item_nameview);
        ImageView mitemimage = mView.findViewById(R.id.item_imageview);
        //set data to views
        mitemname.setText(name);
        UniversalImageLoader.setImage(image,mitemimage);
    }
}
