package com.example.bl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class LendedHolder  extends RecyclerView.ViewHolder {
    View mView;
    public LendedHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setinfo(Context ctx, String receiptid, String borrowername, String itemname, String itemhours, String itemprice){

        TextView mreceiptid = mView.findViewById(R.id.lendedreceiptid);
        TextView musername = mView.findViewById(R.id.lendedborrowername);
        TextView mitemname = mView.findViewById(R.id.lendeditemname);
        TextView mitemhours = mView.findViewById(R.id.lendeditemhours);
        TextView mitemprice = mView.findViewById(R.id.lendeditemprice);
        //set data to views
        mreceiptid.setText("Receipt no: " + receiptid);
        musername.setText("Borrower Name: " + borrowername);
        mitemname.setText("Item Name: " + itemname);
        mitemhours.setText("Hours: " + itemhours +"hour(s)");
        mitemprice.setText("Total Price: " + itemprice + "Ksh");
    }
}
