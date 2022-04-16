package com.mindlab.intrest_agent_android.utils

import android.content.res.Resources

/**
 * Created by Alireza Nezami on 11/5/2021.
 */
object Display {

    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels

    fun getScreenHeight() = Resources.getSystem().displayMetrics.heightPixels
}