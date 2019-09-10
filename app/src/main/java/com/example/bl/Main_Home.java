package com.example.bl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bl.Models.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class Main_Home extends Fragment {

    private RecyclerView mRecylerview;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.home_fragment, container, false);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));

        getActivity().setTitle("Home");

        setHasOptionsMenu(true);


        mRecylerview = (RecyclerView)view.findViewById(R.id.recyclerView);
        mRecylerview.setHasFixedSize(true);
        mRecylerview.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mDatabase.keepSynced(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //displaying posts in recycler view
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(mDatabase, Post.class).build();

        FirebaseRecyclerAdapter<Post, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Post, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
               View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card,viewGroup,false);

               return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post model) {

                final String itemname = model.getItemname();
                final String itemimage = model.getImage();
                final String itemprice = model.getItemprice();
                final String itemdescription = model.getItemdesc();
                final String itemlocation = model.getItemlocation();
                final String userid = model.getUser_id();
                final String phoneno = model.getPhoneno();
                final String postid = model.getPost_id();

                holder.setDetails(getContext(),itemname,itemimage);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(),viewpost.class);
                        intent.putExtra("itemname",itemname);
                        intent.putExtra("itemimage",itemimage);
                        intent.putExtra("itemprice",itemprice);
                        intent.putExtra("itemdescription", itemdescription);
                        intent.putExtra("itemlocation", itemlocation);
                        intent.putExtra("userid",userid);
                        intent.putExtra("phoneno", phoneno);
                        intent.putExtra("postid", postid);
                        startActivity(intent);
                    }
                });
            }
        };

        firebaseRecyclerAdapter.startListening();
        mRecylerview.setAdapter(firebaseRecyclerAdapter);
    }
}

