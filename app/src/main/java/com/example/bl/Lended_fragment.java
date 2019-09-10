package com.example.bl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bl.Models.Brwlend;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Lended_fragment extends Fragment {

    private RecyclerView mRecyclerview;

    private DatabaseReference mDatabase;
    private FirebaseUser fuser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lended, container, false);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        mRecyclerview = (RecyclerView) view.findViewById(R.id.lendedrecyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase = FirebaseDatabase.getInstance().getReference("Request");
        mDatabase.keepSynced(true);

        readrequest();

        return view;
    }

    public void readrequest(){
        FirebaseRecyclerOptions<Brwlend> options = new FirebaseRecyclerOptions.Builder<Brwlend>().setQuery(mDatabase,Brwlend.class).build();

        FirebaseRecyclerAdapter<Brwlend, LendedHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Brwlend, LendedHolder>(options) {

            @NonNull
            @Override
            public LendedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lendedreceipt,viewGroup,false);
                return new LendedHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull LendedHolder holder, int position, @NonNull Brwlend model) {

                final String borrowername = model.getBorrowername();
                final String itemname = model.getItemname();
                final String accept = model.getAccept();
                final String lenderid = model.getLenderid();
                final String requestid = model.getRequestid();
                final String itemhours = model.getHours();
                final String itemprice = model.getPrice();

                int hours = Integer.parseInt(itemhours);
                int price = Integer.parseInt(itemprice);

                int totprice = hours * price;

                final String totalprice = String.valueOf(totprice);

                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 40));

                if(lenderid.equals(fuser.getUid()) && accept.contentEquals("Lended")){
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    holder.setinfo(getContext(),requestid,borrowername,itemname,itemhours,totalprice);
                }

            }
         };

        firebaseRecyclerAdapter.startListening();
        mRecyclerview.setAdapter(firebaseRecyclerAdapter);
    }
}
