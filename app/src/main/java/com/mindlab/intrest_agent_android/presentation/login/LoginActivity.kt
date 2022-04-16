package com.mindlab.intrest_agent_android.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.databinding.ActivityLoginBinding
import com.mindlab.intrest_agent_android.presentation.home.HomeActivity
import com.mindlab.intrest_agent_android.utils.Constants
import com.mindlab.intrest_agent_android.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber

@InternalCoroutinesApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private val bindings: ActivityLoginBinding by viewBinding()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupNavController()
        collect()
        Timber.d("View Model Name 1: $viewModel")

    }

    private fun collect() {
        viewModel.successLogin.collectIn(this) {
            if (it) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        viewModel.showChooseRestaurant.collectIn(this) {
            it?.let {userResponse->
                navController.navigate(R.id.signin_to_choose_profile,
                    bundleOf(Constants.RESTAURANT_LIST to userResponse)

//                    ,Bundle().apply {
//                    putParcelable(Constants.RESTAURANT_LIST,userResponse)
//                }
                )
            }
        }
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
    }
}

