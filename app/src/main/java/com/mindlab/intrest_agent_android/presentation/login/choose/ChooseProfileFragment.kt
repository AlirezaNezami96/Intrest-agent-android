package com.mindlab.intrest_agent_android.presentation.login.choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.mindlab.intrest_agent_android.data.model.Menu
import com.mindlab.intrest_agent_android.presentation.components.theme.IntRestagentandroidTheme
import com.mindlab.intrest_agent_android.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Alireza Nezami on 12/19/2021.
 */
@AndroidEntryPoint
class ChooseProfileFragment(

) : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()
    private val args: ChooseProfileFragmentArgs by navArgs()
    private  var menus: MutableList<Menu> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        args.user.user.restaurants.map {
            menus.add(
                Menu(
                    id = it.id,
                    title = it.name
                )
            )
        }

        return ComposeView(requireContext()).apply {
            setContent {
                IntRestagentandroidTheme(false) {
                    Surface(color = MaterialTheme.colors.background) {
                        ChooseProfileViews(
                            viewModel,
                            menus
                        ) {
                            requireActivity().onBackPressed()
                        }
                    }
                }
            }
        }
    }
}