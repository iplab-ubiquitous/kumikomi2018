package com.a10llip0p.android.soso

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by yuto on 2018/06/17.
 */
class HumidityFragmentPagerAdapter(fm: FragmentManager, vegetable: Vegetable) : FragmentPagerAdapter(fm) {
    var vegetable: Vegetable

    init {
        this.vegetable = vegetable
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment? {
        when(position) {
            0 -> {
                return HumidityFragment.newInstance(this.vegetable)
            }
            1 -> {
                return HumidityGraphFragment.newInstance(this.vegetable)
            }
            2 -> {
                return CameraFragment.newInstance()
            }
        }
        return null
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "湿度"
            }
            1 -> {
                return "グラフ"
            }
            2 -> {
                return "カメラ"
            }
        }
        return null
    }
}