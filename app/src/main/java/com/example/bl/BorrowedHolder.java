package com.example.bl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class BorrowedHolder  extends RecyclerView.ViewHolder {
    View mView;
    public BorrowedHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setinfo(Context ctx, String receiptid, String borrowername, String itemname, String itemhours, String itemprice){

        TextView mreceiptid = mView.findViewById(R.id.borrowedreceiptid);
        TextView musername = mView.findViewById(R.id.borrowedborrowername);
        TextView mitemname = mView.findViewById(R.id.borroweditemname);
        TextView mitemhours = mView.findViewById(R.id.borroweditemhours);
        TextView mitemprice = mView.findViewById(R.id.borroweditemprice);
        //set data to views
        mreceiptid.setText("Receipt no: " + receiptid);
        musername.setText("Borrower: " + borrowername);
        mitemname.setText("Item Name: " + itemname);
        mitemhours.setText("Hours: " + itemhours +"hour(s)");
        mitemprice.setText("Total Price: " + itemprice + "Ksh");

    }
}
