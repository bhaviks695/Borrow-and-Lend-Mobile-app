package com.example.bl;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bl.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    TextView username, userphoneno;

    Button update;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profiletoolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        Displayuserinfo();

        username = (TextView) findViewById(R.id.profileusername);
        userphoneno = (TextView) findViewById(R.id.profilephonenumber);

        update = (Button) findViewById(R.id.btnupdate);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateprofile();
            }
        });
    }

    public void Displayuserinfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

               username.setText(user.getName());
               userphoneno.setText(user.getPhoneno());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void updateprofile(){
        //update profile for user, opn update dialog with edit text
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater =this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.profiledialog, null);
        dialogBuilder.setView(dialogView);

         final EditText name = (EditText) dialogView.findViewById(R.id.dialogname);
         final EditText number = (EditText) dialogView.findViewById(R.id.dialognumber);

         name.setText(username.getText());
         number.setText(userphoneno.getText());

        dialogBuilder.setTitle("Update");
        dialogBuilder.setMessage("Update name or phone no.");
        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
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
                final String updname = name.getText().toString();
                final String updphone = number.getText().toString();

                if(updname.isEmpty()){
                    name.setError("Enter name please");
                    name.requestFocus();
                    return;
                }

                if(updphone.isEmpty()){
                    number.setError("Enter phone no");
                    number.requestFocus();
                    return;
                }

                if (!updphone.startsWith("07")){
                    number.setError("Enter valid kenyan number");
                    number.requestFocus();
                    return;
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
                reference.child("name").setValue(updname);
                reference.child("phoneno").setValue(updphone);
                b.cancel();
                Toast.makeText(getApplicationContext(),"Updated successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
