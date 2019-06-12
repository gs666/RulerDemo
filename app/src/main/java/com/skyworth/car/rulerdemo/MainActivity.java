package com.skyworth.car.rulerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @Author: 高烁
 * @CreateDate: 2019-06-12 10:18
 * @Email: gaoshuo521@foxmail.com
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RulerView mIdRuler;
    private EditText mIdEditText;
    /**
     * 打开
     */
    private Button mIdGotoBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mIdRuler.setFmChannl(95.5);
    }

    private void initView() {
        mIdRuler = findViewById(R.id.id_ruler);
        mIdEditText = findViewById(R.id.id_edit_text);
        mIdGotoBtn = findViewById(R.id.id_goto_btn);
        mIdGotoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.id_goto_btn:
                mIdRuler.setFmChannl(Double.valueOf(mIdEditText.getText().toString()));
                break;
        }
    }
}
