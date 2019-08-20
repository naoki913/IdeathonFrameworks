package com.example.ideathonframeworksapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_brainstorming.*

class BrainstormingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brainstorming)

        val min=intent.getIntExtra("MIN",2)
        val sec=intent.getIntExtra("SEC",0)

        textView_min.text=min.toString()
        textView_sec.text=sec.toString()


        val vg = findViewById<View>(R.id.LinearLayout) as ViewGroup


        button_add.setOnClickListener {
            getLayoutInflater().inflate(R.layout.brainstorming_card,vg)
        }
    }
}
