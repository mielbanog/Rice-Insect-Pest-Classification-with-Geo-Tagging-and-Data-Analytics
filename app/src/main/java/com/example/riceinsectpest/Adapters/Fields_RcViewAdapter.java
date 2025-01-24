package com.example.riceinsectpest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riceinsectpest.Features.FieldviewActivity;
import com.example.riceinsectpest.Models.Fields_ModelClass;
import com.example.riceinsectpest.R;

import java.util.ArrayList;

public class Fields_RcViewAdapter extends RecyclerView.Adapter<Fields_RcViewAdapter.RVviewHolder> {

    public static  int ItemCounter = 0;
    public static int imagePost;
    private static String ImagePATH;
    private static String ImAgE_NamE;
    private static String IMAGE_ID;
    private Context context;
    private ArrayList<Fields_ModelClass> modelClassArrayList;
    public static  int counter = 0;

    public Fields_RcViewAdapter(Context context, ArrayList<Fields_ModelClass> modelClassArrayList) {
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;
    }

    @NonNull
    @Override
    public RVviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardfieldlayout,parent,false);
        return new RVviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVviewHolder holder, int position) {
        Fields_ModelClass objectModel = modelClassArrayList.get(position);

        holder.Fieldname.setText(objectModel.getDiseaseName());
        holder.ID_holder.setText(objectModel.getId());
        ImagePATH = objectModel.getImagePath();
        holder.objectImageView.setImageBitmap(objectModel.getImage());
        holder.ViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemCounter = 1;
                imagePost = holder.getAdapterPosition();
//                IMAGE_ID = holder.Image_ID_Holder.getText().toString();
                counter = 1;

                Intent intent = new Intent(v.getContext(), FieldviewActivity.class);
                intent.putExtra("ID",objectModel.getId());
                intent.putExtra("Disease_Name",objectModel.getDiseaseName());
                intent.putExtra("Image",objectModel.getImage());
                intent.putExtra("Image_Path",objectModel.getImagePath());
                intent.putExtra("Date",objectModel.getDateTaken());


                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

    public static class RVviewHolder extends RecyclerView.ViewHolder{
        TextView Fieldname, Fieldimg, Fieldlocation, ID_holder;

        ImageView objectImageView;
        ImageButton ViewBtn;
        public RVviewHolder(@NonNull View itemView) {
            super(itemView);

            Fieldname = itemView.findViewById(R.id.fieldname);
            Fieldlocation = itemView.findViewById(R.id.locationfield);
            Fieldimg = itemView.findViewById(R.id.fieldimg);
            ID_holder = itemView.findViewById(R.id.id_holder);

        }
    }
}
