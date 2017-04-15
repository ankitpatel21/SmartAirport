package com.example.nirav.smartairport;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Ticket_details extends Fragment {

    private Firebase ticketDetails;
    private FirebaseAuth firebaseAuth;
    TextView data1, timecal;
    String t_barcode_no;
    String[] date;
    String[] time;
    String[] currentDateTiem;

    public Ticket_details() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this.getContext());
        ticketDetails =new Firebase("https://smart-airpot.firebaseio.com/ticket_details");

        data1 =(TextView)getView().findViewById(R.id.data);
        timecal = (TextView)getView().findViewById(R.id.timecal);
        t_barcode_no = getArguments().getString("t_barcode_no");

        if (t_barcode_no.isEmpty())
        {
            ticket_scan ts=new ticket_scan();
            getFragmentManager().beginTransaction().replace(R.id.main_container,ts).commit();
        }
        //data1.setText(t_barcode_no);




        ticketDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot pass : dataSnapshot.getChildren())
                {
                    Map<String, String> map=pass.getValue(Map.class);
                    if(map.get("t_barcode_no").equals(t_barcode_no))
                    {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd:MM:yyyy:HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        data1.setText("\nTicket ID : "+ map.get("t_id")+ "\nFlight Number : " + map.get("flight_no")+ "\nFlight Type : " + map.get("flight_type")+ "\nJourney Date : " + map.get("j_date")+ "\nJourney Time : " + map.get("j_time")+ "\nJourney : " + map.get("SA_name") + " To " + map.get("DA_name"));
                        currentDateTiem = formattedDate.split(":");
                        time = map.get("j_time").split(":");
                        date = map.get("j_date").split("-");

                        chkTime(time,date);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void chkTime(String[] time, String[] date)
    {
        int[] d = new int[date.length];
        int[] t = new int[time.length];
        int[] currentDT = new int[currentDateTiem.length];
        for(int i=0;i<currentDateTiem.length;i++)
        {
            currentDT[i] = Integer.parseInt(currentDateTiem[i]);
            Log.d("date",String.valueOf(currentDT[i]));
        }
        for (int i=0;i<date.length;i++)
        {
            d[i] =Integer.parseInt(date[i]);
        }

        for (int i=0;i<time.length;i++)
        {
            t[i] =Integer.parseInt(time[i]);
            //Log.d("date",String.valueOf(t[i]));
        }

//        Toast.makeText(this.getContext(),String.valueOf(currentDT[2])+String.valueOf(d[2]),Toast.LENGTH_LONG).show();
        Log.d("cd",String.valueOf(d[0]));
        Log.d("day1", String.valueOf(currentDT[0]-d[0]));
        if(d[2] == currentDT[2])
        {
            if(d[1] == currentDT[1])
            {
                if(d[0] <= currentDT[0])
                {

                    Log.d("day", String.valueOf(currentDT[0]-d[0]));
                    timecal.setText(String.valueOf(currentDT[0]-d[0]));
                    //Toast.makeText(getActivity(),String.valueOf(currentDT[0]-d[0]),Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_ticket_details, container, false);
    }

}
