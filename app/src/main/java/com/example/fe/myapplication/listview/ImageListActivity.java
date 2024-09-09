package com.example.fe.myapplication.listview;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fe.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addingData(); // 初始化数据
        // 创建适配器，在适配器中导入数据 1.当前类 2.list_view一行的布局 3.数据集合
        ImageListAdapter imageListAdapter = new ImageListAdapter(ImageListActivity.this, R.layout.image_list_view, onePieceList);
        ListView listView = findViewById(R.id.ImageListView); // 将适配器导入Listview
        listView.setAdapter(imageListAdapter);
    }

    private final List<ImageListArray> onePieceList = new ArrayList<>();

    public void addingData() {
        for (int i = 0; i < 30; i++) {
            ImageListArray ace = new ImageListArray("ace", R.drawable.test);
            onePieceList.add(ace);
        }
    }

}