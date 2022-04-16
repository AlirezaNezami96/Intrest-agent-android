package com.mindlab.intrest_agent_android.presentation.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mindlab.intrest_agent_android.presentation.components.theme.IntRestagentandroidTheme
import com.mindlab.intrest_agent_android.presentation.history.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Alireza Nezami on 12/18/2021.
 */
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MenuFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                IntRestagentandroidTheme(false) {
                    Surface(color = MaterialTheme.colors.background) {
                        MenuManagementViews(

                        )
                    }
                }
            }
        }
    }

}