package com.example.nirav.smartairport;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    Button btn_map,btn_profile,btn_ticket_scan,btn_ticket_details;

    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_map = (Button)getView().findViewById(R.id.btn_map);
        btn_profile = (Button)getView().findViewById(R.id.btn_profile);
        btn_ticket_scan=(Button)getView().findViewById(R.id.btn_ticket_scan);
        btn_ticket_details=(Button)getView().findViewById(R.id.btn_ticket_details);

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment pf =new ProfileFragment();
                getFragmentManager().beginTransaction().replace(R.id.main_container,pf).commit();
            }
        });

        btn_ticket_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ticket_scan tc=new ticket_scan();
                getFragmentManager().beginTransaction().replace(R.id.main_container,tc).commit();
            }
        });

        btn_ticket_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO:error in send fragment .. send data to fragment
                Ticket_details td = new Ticket_details();
                getFragmentManager().beginTransaction().replace(R.id.main_container,td).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}
