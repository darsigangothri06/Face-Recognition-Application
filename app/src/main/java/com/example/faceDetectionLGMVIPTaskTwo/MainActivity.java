package com.example.faceDetectionLGMVIPTaskTwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.faceDetectionLGMVIPTaskTwo.Helper.GraphicOverlay;
import com.example.faceDetectionLGMVIPTaskTwo.Helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    private Button detectButtonForFace;
    private GraphicOverlay graphOverlay;
    private CameraView cameraVisibility;
    AlertDialog alertMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detectButtonForFace =findViewById(R.id.detect_face_btn);
        graphOverlay =findViewById(R.id.graphic_overlay);
        cameraVisibility =findViewById(R.id.camera_view);


        alertMessage =new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please Wait...")
                .setCancelable(false)
                .build();

        cameraVisibility.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
            }

            @Override
            public void onError(CameraKitError cameraKitError) {
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                alertMessage.show();
                Bitmap bitMapForCam = cameraKitImage.getBitmap();
                bitMapForCam = Bitmap.createScaledBitmap(bitMapForCam, cameraVisibility.getWidth(), cameraVisibility.getHeight(),false);
                cameraVisibility.stop();

                // face detection
                FaceDetection(bitMapForCam);
            }
            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {
            }
        });

        detectButtonForFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraVisibility.start();
                cameraVisibility.captureImage();
                graphOverlay.clear();
            }
        });
    }

    private void FaceDetection(Bitmap bitmap) {
        FirebaseVisionImage firebaseImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionFaceDetectorOptions firebaseVisionOptions = new FirebaseVisionFaceDetectorOptions.Builder().build();
        FirebaseVisionFaceDetector faceDetectorFirebase = FirebaseVision.getInstance()
                .getVisionFaceDetector(firebaseVisionOptions);

        faceDetectorFirebase.detectInImage(firebaseImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionFace> facesList) {
                        // On successfull detection of face
                             resultFuncFaces(facesList);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Displaying Error Message
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG);
            }
        });
        }

    private void resultFuncFaces(List<FirebaseVisionFace> firebaseFaces) {
        int count_increment = 0;
        for(FirebaseVisionFace face:firebaseFaces){
            Rect rectFun = face.getBoundingBox();
            RectOverlay rect_overlay_face = new RectOverlay(graphOverlay,rectFun);
            graphOverlay.add(rect_overlay_face);
            count_increment += 1;
        }
        alertMessage.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraVisibility.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraVisibility.start();
    }
}