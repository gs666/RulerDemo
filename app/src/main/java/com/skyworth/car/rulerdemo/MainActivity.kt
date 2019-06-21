package com.skyworth.car.rulerdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @Author: 高烁
 * @CreateDate: 2019-06-12 10:18
 * @Email: gaoshuo521@foxmail.com
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        id_goto_btn.setOnClickListener { id_ruler.setFmChanel(java.lang.Double.valueOf(id_edit_text.text.toString())) }

        id_ruler.setFmChanel(95.5)

        id_ruler.setOnMoveActionListener(object : RulerView.OnMoveActionListener {
            override fun onMove(x: Double) {
                id_current_fm.text = "当前FM：$x"
            }
        })
    }
}
