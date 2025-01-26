package com.example.riceinsectpest.Features;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riceinsectpest.Address.LocationActivity;
import com.example.riceinsectpest.Dashboard.DashboardActivity;
import com.example.riceinsectpest.R;
import com.example.riceinsectpest.ml.InsectmodelIncept;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ResultViewer extends AppCompatActivity {
    private  String imageFILENAME ="IMG_",  currentPHOTOPATH;
    private static final int REQUEST_CODE = 102;
    public Uri imageUri;
    Bitmap imageToStore;
    String timeStamp,dateCapture,timeNow, image_name, Image_path, date_Taken, ID, Image_Name,filename,imgTreatment,area="no data";
    int imageSize = 224;
    Dialog dialog;

    TextView imageName, confidence;
    ImageView imgresultView;
    ArrayList<String> MyFields;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_viewer);
        init();

        if (DashboardActivity.identifier == 1) {
            accessCamera();
        }  else if(DashboardActivity.identifier == 2){
            accessGallery();
        }


        /*Spinner spinner = findViewById(R.id.spinerMyFields);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, MyFields);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/

    }
    //Methods
    public void init(){
        imgresultView = findViewById(R.id.imageView);
        imageName = findViewById(R.id.diseaseName);
        confidence = findViewById(R.id.percentage);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imgresultView.setVisibility(View.VISIBLE);
//        toolbar.setVisibility(View.VISIBLE);
        imageName.setVisibility(View.VISIBLE);
//        showBottom.setVisibility(View.VISIBLE);
//        saveData.setVisibility(View.VISIBLE);
        confidence.setVisibility(View.VISIBLE);



        try {
            if (resultCode == RESULT_OK){
                if (DashboardActivity.identifier == 2 && requestCode == 100)
                {
                    if (data != null){
                        imageUri = data.getData();
                        try {
                            imageToStore = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                            Matrix matrix = new Matrix();
                            matrix.postRotate(270);
                            Bitmap cachedImage = Bitmap.createBitmap(imageToStore, 0, 0, imageToStore.getWidth(), imageToStore.getHeight(), matrix, true);
                            imgresultView.setImageBitmap(cachedImage);
                            imageToStore = Bitmap.createScaledBitmap(imageToStore, imageSize, imageSize, false);

                            //---------------------------Image Classifier CNN ---------------------------
                            classifyImage(imageToStore);

                           // storeImage(imageToStore);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    if (DashboardActivity.identifier == 1 && requestCode == 101){
                        try{
                            imageUri = Uri.fromFile(new File(currentPHOTOPATH));
                            onImageResult(data);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }else {
                Intent intent = new Intent(ResultViewer.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void accessGallery(){

        // ----- Get Image from Gallery -----
        try{
            DashboardActivity.identifier = 2;
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 100);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void accessCamera(){

        // ----- Access Device Camera -----
        try {
            DashboardActivity.identifier = 1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                        File imageFILE = File.createTempFile(imageFILENAME, ".jpg", storageDirectory);
                        currentPHOTOPATH = imageFILE.getAbsolutePath();

                        Uri imageURI = FileProvider.getUriForFile(ResultViewer.this,
                                "com.example.riceinsectpest.fileprovider", imageFILE);

                        Log.d("IMAGE", "File path: " + currentPHOTOPATH);
                        Log.d("IMAGE", "File URI: " + imageURI);

                        if (imageURI != null) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                            startActivityForResult(intent, 101);
                        } else {
                            Log.e("ERROR", "imageURI is null");
                        }
                        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                        Log.d("IMAGE","Value"+imageURI);
                        startActivityForResult(intent, 101);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception c) {
            c.printStackTrace();
        }
    }


    //CHECK PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == 100 && DashboardActivity.identifier == 1) {
                    Intent intent = new Intent(ResultViewer.this, ResultViewer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 100);
                    DashboardActivity.identifier = 1;


                }else if (requestCode == 101 && DashboardActivity.identifier == 2) {
                    Intent intent = new Intent(ResultViewer.this, ResultViewer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 101);
                }
                    DashboardActivity.identifier = 2;
            }else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    public void classifyImage(Bitmap image) {

        try {
            InsectmodelIncept model = InsectmodelIncept.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            int pixel = 0;
            for (int i = 0; i < imageSize; ++i) {
                for (int j = 0; j < imageSize; ++j) {
                    final int val = intValues[pixel++];


                    int IMAGE_MEAN = 0;
                    float IMAGE_STD = 255.0f;
                    byteBuffer.putFloat((((val >> 16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                    byteBuffer.putFloat((((val >> 8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                    byteBuffer.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);

                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            InsectmodelIncept.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; ++i) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Brown Plant Hopper","Mole Cricket", "Rice Bug", "Rice Leaf Folders",  "Stem Borer", "Undefined"};

            imageName.setText(classes[maxPos]);
            Image_Name = imageName.getText().toString();

            String CONFIDENCE_LEVEL = String.format("%.2f", maxConfidence * 100);
            confidence.setText(CONFIDENCE_LEVEL+"%");


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }//---------------------------------------
    }
    private void onImageResult(Intent data){

        // ----- Function for Camera while after pressing the camera button -----
        try {
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = 2;
            Bitmap cachedImage = BitmapFactory.decodeFile(currentPHOTOPATH, o2);
            int rotate = getCameraPhotoOrientation(imgresultView, currentPHOTOPATH);
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            cachedImage = Bitmap.createBitmap(cachedImage , 0, 0, cachedImage.getWidth(), cachedImage.getHeight(), matrix, true);
            imgresultView.setImageBitmap(cachedImage);
            imageToStore = Bitmap.createScaledBitmap(cachedImage, imageSize, imageSize, false);

            //---------------------------Image Classifier CNN ---------------------------
            classifyImage(imageToStore);
            //storeImage(imageToStore);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getCameraPhotoOrientation(ImageView context, String imagePath) {

        // ----- Set Image Orientation -----
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultViewer.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


}