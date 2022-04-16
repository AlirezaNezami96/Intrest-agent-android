package com.mindlab.intrest_agent_android.presentation.login.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mindlab.intrest_agent_android.presentation.login.LoginViewModel
import com.mindlab.intrest_agent_android.presentation.login.choose.ChooseProfileViews
import com.mindlab.intrest_agent_android.presentation.components.theme.IntRestagentandroidTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Created by Alireza Nezami on 12/19/2021.
 */
@InternalCoroutinesApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class SignInFragment(

) : Fragment() {
    private val viewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                IntRestagentandroidTheme(false) {
                    Surface(color = MaterialTheme.colors.background) {
                        SignInViews(
                            viewModel
                        )
                    }
                }
            }
        }
    }
}