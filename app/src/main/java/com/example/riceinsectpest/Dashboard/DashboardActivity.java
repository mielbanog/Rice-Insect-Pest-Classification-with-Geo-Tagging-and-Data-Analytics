package com.example.riceinsectpest.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.riceinsectpest.Adapters.Fields_RcViewAdapter;
import com.example.riceinsectpest.Adress.LocationActivity;
import com.example.riceinsectpest.MainActivity;
import com.example.riceinsectpest.R;

public class DashboardActivity extends AppCompatActivity {

    private Fields_RcViewAdapter objectRVAdapter;
    private RecyclerView recyclerView;
    AppCompatButton createnewFieldbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        createnewFieldbtn = findViewById(R.id.createfieldbtn);
        ImageView searchIcon = findViewById(R.id.searchIcon);
        EditText searchEditText = findViewById(R.id.searchEditText);

        createnewFieldbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEditText.getVisibility() == View.GONE) {
                    int targetWidth = 800; // Set your desired width in pixels
                    expand(searchEditText, targetWidth);
                } else {
                    collapse(searchEditText);
                }
            }
        });






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