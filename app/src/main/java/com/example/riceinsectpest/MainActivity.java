package com.example.riceinsectpest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.riceinsectpest.Adapters.Fields_RcViewAdapter;

public class MainActivity extends AppCompatActivity {


    private Fields_RcViewAdapter objectRVAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


    }

    private void expand(final View view) {
        view.setVisibility(View.VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, widthSpec);
        final int targetWidth = view.getMeasuredWidth();

        view.getLayoutParams().width = 1;
        view.requestLayout();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().width = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
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