package com.example.nirav.smartairport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedBack extends AppCompatActivity {

    EditText email_id;
    EditText desc;
    Button submit;
    Firebase feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        email_id = (EditText) findViewById(R.id.txt_email_id);
        desc = (EditText) findViewById(R.id.desc);
        submit = (Button) findViewById(R.id.btn_submit);

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        email_id.setText(fuser.getEmail().toString());
        Firebase.setAndroidContext(this);
        feedback = new Firebase("https://smart-airpot.firebaseio.com/feedback");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_id.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String desc_str = desc.getText().toString();
                if(TextUtils.isEmpty(email_id.getText().toString()) || !email.matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter valid email ID",Toast.LENGTH_SHORT).show();
                    email_id.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(desc.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Description",Toast.LENGTH_SHORT).show();
                    desc.requestFocus();
                    return;
                }
                if(desc.getText().toString().length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Enter at least 10 character to Description",Toast.LENGTH_SHORT).show();
                    desc.requestFocus();
                    return;
                }

//                    Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(),desc_str,Toast.LENGTH_SHORT).show();

                Firebase feedback_child_msg = feedback.push();
                Firebase feedback_email = feedback_child_msg.child("Email");
                Firebase feedback_msg = feedback_child_msg.child("Message");
                feedback_email.setValue(email);
                feedback_msg.setValue(desc_str);

                Toast.makeText(getApplicationContext(), "FeedBack Submitted Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finishAffinity();

            }
        });
    }
}
