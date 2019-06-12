package com.skyworth.car.rulerdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        id_ruler.setFmChannl(95.0)

        id_goto_btn.setOnClickListener { id_ruler.setFmChannl(java.lang.Double.valueOf(id_edit_text.text.toString())) }
    }
}
