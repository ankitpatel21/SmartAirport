package com.example.nirav.smartairport;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EstimateTimeFragment extends Fragment {

    SharedPreferences sharedpreferences;
    String url="http://192.168.1.114/airport/getTime.php";
    private FirebaseAuth firebaseAuth;
    TextView esTime, counterNo;

    public EstimateTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        esTime = (TextView)getView().findViewById(R.id.esTime);
        counterNo=(TextView)getView().findViewById(R.id.counterNo);

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        if (!sharedpreferences.contains("Ticket_ID"))
        {
            ticket_scan ts=new ticket_scan();
            getFragmentManager().beginTransaction().replace(R.id.main_container,ts).commit();
        }

        getEstimateTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estimate_time, container, false);
    }

    public void getEstimateTime()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        //Toast.makeText(getApplicationContext(),"Sample Toast", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("DATA");

                                if ((jsonArray.length()) != 0) {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject o = jsonArray.getJSONObject(i);
                                        esTime.setText(o.getString("Estime"));
                                        counterNo.setText(o.getString("CounterNo"));
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getContext().getApplicationContext(),"No data",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String , String>();

                params.put("u_id", user.getUid());

                return params;

            }};
        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
