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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText inemail, inpassword;

    TextView signup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        checkuser();

        inemail = (EditText)findViewById(R.id.input_email);
        inpassword = (EditText) findViewById(R.id.input_password);

        signup = (TextView) findViewById(R.id.link_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

    }

    public void checkuser(){
        FirebaseUser mUser = mAuth.getCurrentUser();

        if(mUser != null){
            Intent intent = new Intent(MainActivity.this,Home.class);
            startActivity(intent);
            finish();
        }
    }

    public void login(View v){

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,R.style.MyAlertDialogStyle);

        String email = inemail.getText().toString().trim();
        String password = inpassword.getText().toString().trim();

        if(email.isEmpty()){
            inemail.setError("Please Enter an email");
            inemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inemail.setError("Email not correct");
            inemail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            inpassword.setError("Enter a Password");
            inpassword.requestFocus();
            return;
        }


        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                           Intent intent = new Intent(MainActivity.this,Home.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                            clear();
                            finish();
                        }

                        else{
                            Toast toast = Toast.makeText(MainActivity.this,"Error,please try again",Toast.LENGTH_SHORT);
                            View toastview = toast.getView();
                            toastview.setBackgroundResource(R.drawable.toastdraw);
                            toast.show();
                            clear();
                        }
                    }
                });
    }

    public void clear(){
        inemail.setText(null);
        inemail.requestFocus();
        inpassword.setText(null);
    }


}
