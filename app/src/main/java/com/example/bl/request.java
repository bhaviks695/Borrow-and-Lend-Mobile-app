package com.example.bl;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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



public class request extends Fragment {
 private RecyclerView recyclerView;

private DatabaseReference mDatabase;
private FirebaseUser fuser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

       getActivity().setTitle("Request");

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.requestrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase = FirebaseDatabase.getInstance().getReference("Request");
        mDatabase.keepSynced(true);


        readrequest();


        return view;
    }


    public void readrequest(){

         FirebaseRecyclerOptions<Brwlend> options = new FirebaseRecyclerOptions.Builder<Brwlend>().setQuery(mDatabase,Brwlend.class).build();

         FirebaseRecyclerAdapter<Brwlend, ViewwHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Brwlend, ViewwHolder>(options) {

             @NonNull
            @Override
            public ViewwHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.requestcard,viewGroup,false);
                return new ViewwHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull ViewwHolder holder, int position, @NonNull Brwlend model) {
                final String borrowername = model.getBorrowername();
                final String itemname = model.getItemname();
                final String accept = model.getAccept();
                final String lenderid = model.getLenderid();
                final String requestid = model.getRequestid();
                final String hours = model.getHours();

                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 40));

                if(lenderid.equals(fuser.getUid()) && accept.contentEquals("")){

                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    holder.setinfo(getContext(), borrowername, itemname);
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Open dialog box and ask user to Accept or Reject
                            final android.support.v7.app.AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                            dialogBuilder.setTitle("Item to Lend");
                            dialogBuilder.setMessage("Do you want to Lend this Item to "+ borrowername + " for " + hours + " hours" +" ?");
                            dialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            dialogBuilder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Request").child(requestid);
                                    reference.removeValue();
                                    dialog.dismiss();
                                }
                            });
                            final AlertDialog b = dialogBuilder.create();
                            b.setCanceledOnTouchOutside(false);
                            b.show();

                            b.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Request").child(requestid);
                                    reference.child("accept").setValue("Lended");
                                    b.cancel();
                                }
                            });

                        }
                    });

                }
            }

        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}