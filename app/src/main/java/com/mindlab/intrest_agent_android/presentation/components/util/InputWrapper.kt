package com.mindlab.intrest_agent_android.presentation.components.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InputWrapper(var value: String = "", val error: String? = null) : Parcelable

@Parcelize
data class InputWrapperById(val value: Int = 0, val error: String? = null) : Parcelable