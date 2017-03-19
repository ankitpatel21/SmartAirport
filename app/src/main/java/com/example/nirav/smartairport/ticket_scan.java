package com.example.nirav.smartairport;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ticket_scan extends Fragment {

    SurfaceView cameraPreview;
    SparseArray<Barcode> barcodes;
    TextView result;


    public ticket_scan() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraPreview = (SurfaceView) getView().findViewById(R.id.camera);
        result=(TextView)getView().findViewById(R.id.result);

        createcameraSource();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_ticket_scan, container, false);
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

                }
                catch (IOException e)
                {
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

                barcodes= detections.getDetectedItems();
                if(barcodes.size()>0) {
                    final Barcode barcode = barcodes.valueAt(0);
                    //result.setText(barcode.displayValue);
                    String t_barcode = "";
                    t_barcode = barcode.displayValue.toString();

                    Ticket_details td = new Ticket_details();
                    Bundle arg = new Bundle();
                    arg.putString("t_barcode_no", t_barcode);
                    td.setArguments(arg);
                    getFragmentManager().beginTransaction().replace(R.id.main_container, td).commit();

                }

            }
        });
    }

}
