package com.skyworth.car.rulerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @Author: 高烁
 * @CreateDate: 2019-06-12 10:18
 * @Email: gaoshuo521@foxmail.com
 */
public class MainActivity extends AppCompatActivity {

    private RulerView mIdRuler;
    private TextView mCurrentFm;
    private EditText mIdEditText;
    private Button mIdGotoBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIdRuler = findViewById(R.id.id_ruler);
        mCurrentFm = findViewById(R.id.id_current_fm);
        mIdEditText = findViewById(R.id.id_edit_text);
        mIdGotoBtn = findViewById(R.id.id_goto_btn);

        mIdGotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIdRuler.setFmChanel(Double.valueOf(mIdEditText.getText().toString()));
            }
        });

        mIdRuler.setFmChanel(95.5);
        mIdRuler.setOnMoveActionListener(new RulerView.OnMoveActionListener() {
            @Override
            public void onMove(double x) {
                mCurrentFm.setText("当前FM："+x);
            }
        });
    }
}
