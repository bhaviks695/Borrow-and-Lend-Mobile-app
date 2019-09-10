package com.example.bl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com.example.bl.Models.User;


public class Register extends AppCompatActivity {


    TextView login;

    EditText inname,inemail, inphone,inpassword,rinpassword;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        inname = (EditText) findViewById(R.id.input_name);
        inemail = (EditText) findViewById(R.id.input_email);
        inphone = (EditText) findViewById(R.id.input_phoneno);
        inpassword = (EditText) findViewById(R.id.input_password);
        rinpassword = (EditText) findViewById(R.id.input_rpassword);


        login = (TextView) findViewById(R.id.link_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public void register(View v) {

        final ProgressDialog progressDialog = new ProgressDialog(Register.this, R.style.MyAlertDialogStyle);


        final String name = inname.getText().toString().trim();
        String email = inemail.getText().toString().trim();
        final String phoneno = inphone.getText().toString().trim();
        String password = inpassword.getText().toString().trim();
        String repassword = rinpassword.getText().toString().trim();
        final String rating = "5.0";

        if (name.isEmpty()) {
            inname.setError("Enter name");
            inname.requestFocus();
        }
        if (email.isEmpty()) {
            inemail.setError("Please Enter an email");
            inemail.requestFocus();
            return;
        }

        if(phoneno.isEmpty()){
            inphone.setError("Enter phone no");
            inphone.requestFocus();
            return;
        }

        if (!phoneno.startsWith("07")){
            inphone.setError("Enter valid kenyan number");
            inphone.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inemail.setError("Email not correct");
            inemail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            inpassword.setError("Enter a Password");
            inpassword.requestFocus();
            return;
        }

        //check is passwords match
        if (!password.equals(repassword)) {
            rinpassword.setError("Passwords dont Match");
            rinpassword.requestFocus();
            return;
        }

            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Creating User...");
            progressDialog.show();


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                //sign up sucessful
                                User user = new User(
                                        name, phoneno, rating
                                );

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()){
                                           Toast toast = Toast.makeText(getApplicationContext(), "Signed up", Toast.LENGTH_SHORT);
                                            View toastview = toast.getView();
                                            toastview.setBackgroundResource(R.drawable.toastdraw);
                                            toast.show();
                                            clear();
                                            mAuth.signOut();

                                        }else{
                                            Toast toast =  Toast.makeText(getApplicationContext(), "An error occured, Please try again", Toast.LENGTH_SHORT);
                                            View toastview = toast.getView();
                                            toastview.setBackgroundResource(R.drawable.toastdraw);
                                            toast.show();
                                        }
                                    }
                                });



                            } else {
                              //error mesage
                            }
                        }

                    });

    }


    public void clear(){
        inemail.setText(null);
        inpassword.setText(null);
        inphone.setText(null);
        inname.setText(null);
        inname.requestFocus();
        rinpassword.setText(null);
    }
}
