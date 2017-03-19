package com.example.nirav.smartairport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class Main_register extends AppCompatActivity {

    private Button btn_reg;
    private EditText ed_email,ed_passport_no,ed_password,ed_re_password;

    private FirebaseAuth firebaseAuth;
    private Firebase reg;
    private Firebase fpass;
    //private boolean flag_valid_passport_no =false;
    private boolean flag_status_reg = false;
    private boolean flag2 = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        firebaseAuth= FirebaseAuth.getInstance();

        Firebase.setAndroidContext(this);
        reg = new Firebase("https://smart-airpot.firebaseio.com/user");

        fpass=new Firebase("https://smart-airpot.firebaseio.com/passport");

        btn_reg=(Button)findViewById(R.id.btn_register);
        ed_email=(EditText)findViewById(R.id.txt_email_id);
        ed_passport_no=(EditText)findViewById(R.id.txt_passport_no);
        ed_password=(EditText)findViewById(R.id.txt_password);
        ed_re_password=(EditText)findViewById(R.id.txt_re_type_password);

        if(firebaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag_status_reg=false;
           //     flag2=false;

                register();
                /*Intent mIntent = new Intent(getApplicationContext(),Main_login.class);
                startActivity(mIntent);*/
            }
        });
    }


    private void register() {

        String email = ed_email.getText().toString();
        String password = ed_password.getText().toString();
        final String passport_no = ed_passport_no.getText().toString();
        String re_password=ed_re_password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Main_register.this, "Please Enter email ID", Toast.LENGTH_SHORT).show();
            ed_email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Main_register.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            ed_password.requestFocus();
            return;
        }

        String passport_no_validation = "^[A-PR-WY][1-9]\\d{5}[1-9]$";
        if (!passport_no.matches(passport_no_validation)) {
            Toast.makeText(Main_register.this, "Please Enter correct Passport No.", Toast.LENGTH_LONG).show();
            ed_passport_no.requestFocus();
            return;
        }

        String password_validation="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        if(!password.matches(password_validation))
        {
            Toast.makeText(Main_register.this, "Password must be Minimum 8 characters at least 1 Alphabet & 1 Number", Toast.LENGTH_LONG).show();
            ed_password.requestFocus();
            return;
        }

        if(!password.equals(re_password))
        {
            Toast.makeText(Main_register.this, "Password does not match", Toast.LENGTH_LONG).show();
            ed_re_password.requestFocus();
            return;
        }

        valid_passport_no();
    }

    private void valid_passport_no()
    {
        final String passport_no=ed_passport_no.getText().toString();

        synchronized (this)
        {
            fpass.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot pass1 : dataSnapshot.getChildren()) {
                        Map<String, String> map = pass1.getValue(Map.class);
                        if (map.get("Passport_no").equals(passport_no)) {
                            //Toast.makeText(Main_register.this,map.get("F_name"),Toast.LENGTH_SHORT).show();
                            //flag_valid_passport_no=true;
                            check_status();
                            //registertodatabase();
                            return;
                            //break;
                        }
                    }
                    Log.v("pass Value", String.valueOf(flag_status_reg));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
    }
    private void check_status()
    {
        final String passport_no=ed_passport_no.getText().toString();
        i=0;
        synchronized (this)
        {
            Log.v("Flag2 Value", String.valueOf(flag_status_reg));
            reg.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot user2 : dataSnapshot.getChildren()) {
                        Map<String, String> map = user2.getValue(Map.class);
                        if (map.get("Passport_no").equals(passport_no)) {

                            //Toast.makeText(Main_register.this, "In Check Status : True", Toast.LENGTH_SHORT).show();
                            //registertodatabase(passport_no);
                            flag_status_reg = true;
                            //Log.v("Flage Value", String.valueOf(flag_status_reg));
                            display();
                            //break;
                            //Toast.makeText(Main_register.this, "Passport No is already registered", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Log.v("F Value", String.valueOf(flag_status_reg));

                    registertodatabase(flag_status_reg);
                    i=1;
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

    }
    int i=0;
    private void display()
    {
        //Log.v("Display value", String.valueOf(i));
        if(i<1)
        {
            Toast.makeText(Main_register.this, "Passport No is already registered", Toast.LENGTH_SHORT).show();
            i++;
        }
    }

    private void registertodatabase(boolean flag_status_reg) {
        //Log.v("Reg F value", String.valueOf(flag_status_reg));
        if (!flag_status_reg) {

            Thread thread = new Thread() {
                public void run() {
                    synchronized (this) {
                        final String email = ed_email.getText().toString();
                        final String password = ed_password.getText().toString();
                        final String passport_no = ed_passport_no.getText().toString();
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Main_register.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            //user.getUid();
                                            //Log.v("E_VALUE", "hello");
                                            FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                                            //Toast.makeText(Main_register.this, fuser.getUid().toString(), Toast.LENGTH_SHORT).show();
                                            String u_id = fuser.getUid().toString();
                                            user u = new user();
                                            u.setU_id(u_id);
                                            u.setPassport_no(passport_no);
                                            //u.setName();
                                            Firebase reg_child = reg.child(u.getU_id()).child("Passport_no");
                                            reg_child.setValue(u.getPassport_no());
                                            Toast.makeText(Main_register.this, "Register successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                            finishAffinity();
                                        }
                                    }
                                });
                    }
                 }
            };
            thread.start();

            thread.interrupt();
        }
        else
        {
            Toast.makeText(Main_register.this, "Passport No is already registered", Toast.LENGTH_SHORT).show();
        }
    }

}
