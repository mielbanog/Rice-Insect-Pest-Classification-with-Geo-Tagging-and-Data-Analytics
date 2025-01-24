package com.example.riceinsectpest.Models;

import android.graphics.Bitmap;

public class Fields_ModelClass {
    String id,diseaseName,imagePath,dateTaken;
    Bitmap image;

    public Fields_ModelClass(String id, String diseaseName, Bitmap image, String imagePath, String dateTaken ) {
        this.id = id;
        this.diseaseName = diseaseName;
        this.imagePath = imagePath;
        this.dateTaken = dateTaken;
        this.image = image;
    }

    public Fields_ModelClass(String id, String diseaseName, String imagePath, String dateTaken, Bitmap image) {
        this.id = id;
        this.diseaseName = diseaseName;
        this.imagePath = imagePath;
        this.dateTaken = dateTaken;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
