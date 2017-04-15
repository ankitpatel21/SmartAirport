package com.example.nirav.smartairport;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Firebase ticketDetails;
    private FirebaseAuth firebaseAuth;
    private Button btn_edit,btn_save;
    private EditText edName,edNumner,edPassport,edAddress;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_edit=(Button)getView().findViewById(R.id.btnEdit);
        btn_save=(Button)getView().findViewById(R.id.btnSave);
        edName=(EditText)getView().findViewById(R.id.Pname);
        edNumner=(EditText)getView().findViewById(R.id.Pnumber);
        edPassport=(EditText)getView().findViewById(R.id.Ppassport);
        edAddress=(EditText)getView().findViewById(R.id.Paddress);


        firebaseAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this.getContext());
        ticketDetails =new Firebase("https://smart-airpot.firebaseio.com/");

        ticketDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot pass : dataSnapshot.getChildren())
                {
                    Map<String, String> map=pass.getValue(Map.class);
                    /*if(map.get("t_barcode_no").equals(t_barcode_no))
                    {
                        data1.setText(map.values().toString());
                    }*/
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edName.setEnabled(true);
                edAddress.setEnabled(true);
                edNumner.setEnabled(true);
                btn_save.setEnabled(true);
                btn_edit.setEnabled(false);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

}
