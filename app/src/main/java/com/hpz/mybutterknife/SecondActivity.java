package com.hpz.mybutterknife;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.hpz.butterknife_annotation.BindView;

/**
 * Created by hpz on 2020/8/31.
 */
public class SecondActivity extends MainActivity {

    @BindView(R.id.button)
    Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button.setText("success");
    }
}
