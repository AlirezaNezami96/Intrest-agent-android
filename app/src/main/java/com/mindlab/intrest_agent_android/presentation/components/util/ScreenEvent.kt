package com.mindlab.intrest_agent_android.presentation.components.util

import androidx.compose.ui.focus.FocusDirection

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
sealed class ScreenEvent {
    class ShowToast(val messageId: Int) : ScreenEvent()
    class UpdateKeyboard(val show: Boolean) : ScreenEvent()
    class RequestFocus(val textFieldKey: FocusedTextFieldKey) : ScreenEvent()
    object ClearFocus : ScreenEvent()
    class Loading(val show: Boolean) : ScreenEvent()
    object SuccessLogin : ScreenEvent()
    class ShowError(val msg:String) : ScreenEvent()

    class MoveFocus(val direction: FocusDirection = FocusDirection.Down) : ScreenEvent()
    class LoginFailed(it: Throwable) : ScreenEvent() {

    }
}
