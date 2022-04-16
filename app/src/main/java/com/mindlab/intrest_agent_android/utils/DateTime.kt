package com.mindlab.intrest_agent_android.utils

import com.mindlab.intrest_agent_android.utils.extention.toTimeStamp
import java.sql.Timestamp

/**
 * Created by Alireza Nezami on 1/1/2022.
 */
object DateTime {

    fun getTimeStamp(): String =
        Timestamp(System.currentTimeMillis()).toString().toTimeStamp()

}