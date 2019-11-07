package com.example.ideathonframeworksapplication

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SetModePager(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(p: Int): Fragment? {

        when(p){
            0->{
                val setMCFrag : SetMandalaFragment = SetMandalaFragment()
                return setMCFrag
            }
            1 ->{
                val setBSFrag : SetBrainFragment = SetBrainFragment()
                return setBSFrag
            }

        }
        return null

    }

}