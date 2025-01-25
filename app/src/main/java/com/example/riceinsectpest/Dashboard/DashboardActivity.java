package com.example.riceinsectpest.Dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.riceinsectpest.Adapters.Fields_RcViewAdapter;
import com.example.riceinsectpest.Address.LocationActivity;
import com.example.riceinsectpest.Camera.Capture;
import com.example.riceinsectpest.Features.ResultViewer;
import com.example.riceinsectpest.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class DashboardActivity extends AppCompatActivity {

    public static  int identifier = 0;
    private Fields_RcViewAdapter objectRVAdapter;
    private RecyclerView recyclerView;
    AppCompatButton CreatenewFieldbtn;
    FloatingActionsMenu FloatngMenu;
    FloatingActionButton Camera,Gallery,CreateField;
    EditText SearchEditText;
    ImageView SearchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //Initialize
        init();

        Camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(DashboardActivity.this, ResultViewer.class);
                    startActivity(intent);
                    identifier = 1;

                }else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
                }
            }
        });
        Gallery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(DashboardActivity.this, Capture.class);
                startActivity(gallery);
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(DashboardActivity.this, ResultViewer.class);

                    startActivity(intent);
                    identifier = 2;

                }else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);

                }
            }
        });

        CreatenewFieldbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, LocationActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        SearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SearchEditText.getVisibility() == View.GONE) {
                    int targetWidth = 800; // Set your desired width in pixels
                    expand(SearchEditText, targetWidth);
                } else {
                    collapse(SearchEditText);
                }
            }
        });

    }


    public void init(){
        Camera = findViewById(R.id.camera_Fbtn);
        Gallery = findViewById(R.id.gallery_Fbtn);
        CreateField= findViewById(R.id.createfield_Fbtn);
        FloatngMenu = findViewById(R.id.floatngActionMenu);
        CreatenewFieldbtn = findViewById(R.id.createfieldbtn);
        SearchIcon = findViewById(R.id.searchIcon);
        SearchEditText = findViewById(R.id.searchEditText);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == 100) {
                    Intent intent = new Intent(DashboardActivity.this, ResultViewer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 100);
                    identifier = 1;

                }else if (requestCode == 101) {

                    Intent intent = new Intent(DashboardActivity.this, ResultViewer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 101);
                    identifier = 2;
                }
            }else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void expand(final View view, final int targetWidth) {
        view.setVisibility(View.VISIBLE);

        view.getLayoutParams().width = 1;
        view.requestLayout();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().width = interpolatedTime == 1
                        ? targetWidth
                        : (int) (targetWidth * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration((int) (targetWidth / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }

    private void collapse(final View view) {
        final int initialWidth = view.getMeasuredWidth();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().width = initialWidth - (int) (initialWidth * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration((int) (initialWidth / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }

}