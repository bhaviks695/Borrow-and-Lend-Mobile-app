package com.example.bl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewwHolder extends RecyclerView.ViewHolder{
    View mView;
    public ViewwHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setinfo(Context ctx, String borrowername, String itemname){


        TextView musername = mView.findViewById(R.id.requestnameview);
        TextView mitemname = mView.findViewById(R.id.requestitemview);

        //set data to views
        musername.setText(borrowername);
        mitemname.setText(itemname);

    }
}
