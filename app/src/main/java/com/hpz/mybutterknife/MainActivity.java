package com.hpz.mybutterknife;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hpz.butterknife.ButterKnife;
import com.hpz.butterknife_annotation.BindView;


/**
 * Created by hpz on 2020/8/31.
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tvContent)
    public TextView  tvContent;
    @BindView(R.id.ivBg)
    public ImageView ivBg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvContent.setText("BINDING VIEW SUCCESS");
        ivBg.setBackgroundColor(Color.BLACK);
    }
}

