package com.example.fe.myapplication.listview;

/**
 * Created by prize on 2018/4/11.
 */

public class ImageListArray {
    private final String name;
    private final int imageId;

    public ImageListArray(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
