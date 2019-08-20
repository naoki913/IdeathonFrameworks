package com.example.ideathonframeworksapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button_mandala.setOnClickListener {
            val intent = Intent(this,MandalaChartHomeActivity::class.java)
            startActivity(intent)
        }

        button_bs.setOnClickListener {
            val intent = Intent(this,BrainstormingHomeActivity::class.java)
            startActivity(intent)
        }

    }
}
