package com.mindlab.intrest_agent_android.presentation.cost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.mindlab.intrest_agent_android.presentation.components.theme.IntRestagentandroidTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Alireza Nezami on 1/15/2022.
 */
@AndroidEntryPoint
class CostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                IntRestagentandroidTheme(false) {
                    Surface(color = MaterialTheme.colors.secondary) {
                        DeliverViews(

                        )
                    }
                }
            }
        }
    }
}