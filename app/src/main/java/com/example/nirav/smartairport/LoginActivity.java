package com.example.nirav.smartairport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText ed_email_id,ed_password;
    private TextView f_pass,resend_email;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        btn_login=(Button)findViewById(R.id.btn_login);
        ed_email_id=(EditText)findViewById(R.id.txt_email_id);
        ed_password=(EditText)findViewById(R.id.txt_password);
        f_pass=(TextView)findViewById(R.id.f_pass);
        resend_email=(TextView)findViewById(R.id.resend_email);

        if(firebaseAuth.getCurrentUser() != null)
        {
            if(!firebaseAuth.getCurrentUser().isEmailVerified())
            {
                firebaseAuth.getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "verification email has been send", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();
                                }
                            }
                        });
            }
        }

        f_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),forgot_password.class));
            }
        });



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Login", "Please wait...", false, false);
                final String email=ed_email_id.getText().toString();
                String password=ed_password.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter email ID",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                /*Intent mIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mIntent);*/
                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                /*FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                if(user.isEmailVerified())
                                {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Main_login.this, "successful", Toast.LENGTH_SHORT).show();
                                        Intent mIntent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(mIntent);
                                    } else
                                    {
                                        Toast.makeText(Main_login.this,"not successful",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Main_login.this, "verification email has been send", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }*/

                                if (task.isSuccessful()) {
                                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                    if(user.isEmailVerified()) {
                                        Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_SHORT).show();
                                        Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(mIntent);
                                        //finish();
                                        finishAffinity();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Email ID is not verified." +
                                                "Please Verify Your Email ID to Login",Toast.LENGTH_SHORT).show();
                                        resend_email.setVisibility(View.VISIBLE);
                                        firebaseAuth.signOut();

                                    }
                                } else
                                {
                                    Toast.makeText(getApplicationContext(),"not successful",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
        resend_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "verification email has been send", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}
