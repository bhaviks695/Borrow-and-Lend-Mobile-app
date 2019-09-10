package com.example.bl;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bl.Models.Brwlend;
import com.example.bl.Models.User;
import com.example.bl.util.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class viewpost extends AppCompatActivity {

    TextView mitemname, mitemprice, mitemdescription, mitemlocation;
    ImageView mImageview;
    FloatingActionButton floatingActionButton, floatingActionButtoncall, floatingActionButtonbook, floatingActionButtondelete;

    FirebaseUser fuser;

    Button btn_submit;

    RatingBar ratingBar;

    Intent intent;

    String userid, phoneno, postid, Borrowername, postname, itemprice;

    double currentrating,userrating;

    private FirebaseAuth mAuth;

    FirebaseDatabase mdatabase;
    DatabaseReference mreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpost);

        Toolbar toolbar = (Toolbar) findViewById(R.id.postviewtoolabar);
        toolbar.setTitle("Item");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mdatabase = FirebaseDatabase.getInstance();
        mreference = mdatabase.getReference();

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        phoneno = intent.getStringExtra("phoneno");
        postid = intent.getStringExtra("postid");

        mitemname = (TextView) findViewById(R.id.postviewname);
        mImageview = (ImageView) findViewById(R.id.postviewimage);
        mitemprice = (TextView) findViewById(R.id.postviewitemprice);
        mitemdescription = (TextView) findViewById(R.id.postviewitemdesc);
        mitemlocation = (TextView) findViewById(R.id.postviewitemlocation);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.btnmessage);
        floatingActionButtoncall = (FloatingActionButton) findViewById(R.id.btncall);
        floatingActionButtonbook = (FloatingActionButton) findViewById(R.id.btnbook);
        floatingActionButtondelete = (FloatingActionButton) findViewById(R.id.btndelete);
        btn_submit = (Button) findViewById(R.id.submitrate);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Borrowername = dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       postname = intent.getStringExtra("itemname");
        mitemname.setText(postname);

        String imageview = intent.getStringExtra("itemimage");
        UniversalImageLoader.setImage(imageview, mImageview);

        itemprice = intent.getStringExtra("itemprice");
        mitemprice.setText(itemprice + "Ksh perhour");

        String itemdescription = intent.getStringExtra("itemdescription");
        mitemdescription.setText(itemdescription);

        String itemlocation = intent.getStringExtra("itemlocation");
        mitemlocation.setText(itemlocation);

        getuserrating();

        if (!fuser.getUid().equals(userid)) {
            floatingActionButtondelete.hide();

            floatingActionButton.show();
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent smsintent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneno, null));
                    smsintent.putExtra("sms_body", "Hello, Iam interested in borrowing this " + postname + ", Please send me more info about it");
                    startActivity(smsintent);
                }
            });

            floatingActionButtoncall.show();
            floatingActionButtoncall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneno));
                    startActivity(intent);
                }
            });

            floatingActionButtonbook.show();
            floatingActionButtonbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //open the dialog form
                   openrequestdialog();
                }
            });



        btn_submit.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  currentrating = ratingBar.getRating();
                   updaterating();
                   Toast.makeText(viewpost.this,"Rated",Toast.LENGTH_SHORT).show();
                   ratingBar.setRating(0);
                  }
            });

        }

        if (fuser.getUid().equals(userid)) {
            floatingActionButtondelete.show();
            floatingActionButtondelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);
                    reference.removeValue();
                    onBackPressed();
                    Toast.makeText(getApplicationContext(),"Deleted Post",Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    public void openrequestdialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater =this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.borrowdialog, null);
        dialogBuilder.setView(dialogView);

        final EditText hours = (EditText) dialogView.findViewById(R.id.hours);
        dialogBuilder.setTitle("Borrow Item");
        dialogBuilder.setMessage("Please click on request button if the user is who is willing to Lend the item is next to you.");
        dialogBuilder.setPositiveButton("Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.show();
        b.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creatae request node in firebase
                final String posttid =  postid;
                final String borrowerid = fuser.getUid();
                final String lenderid = userid;
                final String borrowername = Borrowername;
                final String itemid = postid;
                final String itemname = postname;
                final String Accept ="";
                final String gethours = String.valueOf(hours.getText());
                final String price = itemprice;
                final String requestid = FirebaseDatabase.getInstance().getReference().push().getKey();
                if(gethours.isEmpty()){
                    hours.setError("Enter hours please");
                    hours.requestFocus();
                    return;
                }
                if(gethours.equals("0")){
                    hours.setError("Enter correct hour");
                    hours.requestFocus();
                    return;
                }
                if(Integer.parseInt(gethours) > 24){
                    hours.setError("24hours is Maximum");
                    hours.requestFocus();
                    return;
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Brwlend brwlend = new Brwlend();
                brwlend.setHours(gethours);
                brwlend.setAccept(Accept);
                brwlend.setBorrowername(borrowername);
                brwlend.setBorrowerid(borrowerid);
                brwlend.setRequestid(requestid);
                brwlend.setItemname(itemname);
                brwlend.setItemid(itemid);
                brwlend.setLenderid(lenderid);
                brwlend.setPrice(price);

                reference.child("Request").child(requestid)
                        .setValue(brwlend);
                b.cancel();
            }
        });
    }


    //getting currentuser rating from firebase
    public void getuserrating(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                userrating = Double.parseDouble(user.getRating());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //update rating is user rates
    public void updaterating(){
       double updatedrating = (currentrating + userrating) / 2;
       DatabaseReference updaterating = FirebaseDatabase.getInstance().getReference("Users").child(userid);
       String rating = String.valueOf(updatedrating);
       updaterating.child("rating").setValue(rating);
    }

}