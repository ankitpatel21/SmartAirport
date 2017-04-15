package com.example.nirav.smartairport;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.zzi;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class BagVerificationFragment extends Fragment {

    SparseArray<Barcode> barcodes;
    SurfaceView cameraPreview;
    String t_barcode = "";
    String url="http://192.168.1.106/airport/bag_notification.php";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraPreview = (SurfaceView) getView().findViewById(R.id.camera);
        createcameraSource();
    }

    public BagVerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bag_verification, container, false);
    }

    public void createcameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getContext()).build();
        final CameraSource cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(cameraPreview.getLayoutParams().width, cameraPreview.getLayoutParams().height)
                .build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraPreview.getHolder());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    final Barcode barcode = barcodes.valueAt(0);
                    //result.setText(barcode.displayValue);

                    t_barcode = barcode.displayValue.toString();

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equalsIgnoreCase("success"))
                                    {
                                        //Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                                        getdialog();
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

                            params.put("bag_barcode", t_barcode);

                            return params;

                        }};
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
                    requestQueue.add(stringRequest);
                    getdialog();
                 }
            }
        });
    }
    public void getdialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext().getApplicationContext());
        builder.setMessage("Your Bag is verified")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HomeFragment hf = new HomeFragment();
                        getFragmentManager().beginTransaction().replace(R.id.main_container, hf).commit();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Verified");
        alertDialog.show();

    }
}
