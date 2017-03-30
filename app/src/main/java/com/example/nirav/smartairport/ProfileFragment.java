package com.example.nirav.smartairport;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this.getContext());
        ticketDetails =new Firebase("https://smart-airpot.firebaseio.com/ticket_details");

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

}
