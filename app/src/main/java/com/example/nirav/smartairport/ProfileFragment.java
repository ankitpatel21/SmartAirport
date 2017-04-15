package com.example.nirav.smartairport;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Firebase profile;
    private Firebase profile_data;
    private FirebaseAuth firebaseAuth;
    //private Button btn_edit,btn_save;
    private EditText edName,edNumner,edPassport,edAddress;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edName=(EditText)getView().findViewById(R.id.Pname);
        edNumner=(EditText)getView().findViewById(R.id.Pnumber);
        edPassport=(EditText)getView().findViewById(R.id.Ppassport);
        edAddress=(EditText)getView().findViewById(R.id.Paddress);


        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        //email_id.setText(fuser.getEmail().toString());
        String uid = fuser.getUid().toString();
        Firebase.setAndroidContext(getContext().getApplicationContext());
        profile = new Firebase("https://smart-airpot.firebaseio.com/user/"+uid);
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot pass : dataSnapshot.getChildren())
//                {
//                    Map<String, String> map=pass.getValue(Map.class);
//                    edPassport.setText(map.get("Passport_no").toString());
//                    Toast.makeText(getContext().getApplicationContext(),"Passport No. "+edPassport.getText().toString(),Toast.LENGTH_LONG).show();
//                }
                Map<String, String> map=dataSnapshot.getValue(Map.class);
                edPassport.setText(map.get("Passport_no").toString());
                //Toast.makeText(getContext().getApplicationContext(),"Passport No. "+edPassport.getText().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        edPassport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Toast.makeText(getContext().getApplicationContext(),"Passport No. "+edPassport.getText().toString(),Toast.LENGTH_LONG).show();
                fetch_profile_data();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private void fetch_profile_data() {
        //Toast.makeText(getContext().getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();
        profile_data = new Firebase("https://smart-airpot.firebaseio.com/passport");

        profile_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot pass : dataSnapshot.getChildren()) {
                    Map<String, String> map = pass.getValue(Map.class);
                    //Toast.makeText(getContext().getApplicationContext(),map.get("Passport_no").toString(),Toast.LENGTH_SHORT).show();
                    if (map.get("Passport_no").equals(edPassport.getText().toString())) {
                        edName.setText(map.get("F_name").toString()+" "+map.get("M_name").toString()+" "+map.get("L_name").toString());
                        edAddress.setText(map.get("Address").toString());

                        edNumner.setText(String.valueOf(map.get("Mobile_no")));

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
