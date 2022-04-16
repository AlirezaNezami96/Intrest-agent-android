package com.mindlab.intrest_agent_android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Alireza Nezami on 12/19/2021.
 */
@Parcelize
data class Profiles(
    val profiles: List<String>
) : Parcelable
