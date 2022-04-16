package com.mindlab.intrest_agent_android.presentation.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.databinding.ActivityHomeBinding
import com.mindlab.intrest_agent_android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private val bindings: ActivityHomeBinding by viewBinding()

    private lateinit var navController: NavController

    private val selectedMenu = MutableStateFlow(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bindings.lifecycleOwner = this
        setupNavController()
        clicks()
        changeMenuSelection()
        observe()
    }

    @OptIn(InternalCoroutinesApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
    private fun observe() {
        viewModel.logo.observe(this) {
            it?.let {
                Glide.with(this)
                    .load(it)
                    .into(bindings.logo)
            }
        }
        viewModel.name.observe(this) {
            it?.let {
                bindings.name.text = it
            }
        }
        viewModel.receivingOrders.observe(this) {
            it?.let {
                bindings.stopOrder.isChecked = it
            }
        }

        viewModel.shouldLogout.observe(this) {
            if (it) {
                Intent(this, LoginActivity::class.java).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(this)
                    finish()
                }
            }
        }

    }

    private fun clicks() {
        bindings.orders.setOnClickListener {
            navController.navigate(R.id.ordersFragment)
            selectedMenu.value = bindings.orders.id
        }
        bindings.history.setOnClickListener {
            navController.navigate(R.id.historyFragment)
            selectedMenu.value = bindings.history.id
        }
        bindings.menu.setOnClickListener {
            navController.navigate(R.id.menuFragment)
            selectedMenu.value = bindings.menu.id
        }
        bindings.cost.setOnClickListener {
            navController.navigate(R.id.deliverFragment)
            selectedMenu.value = bindings.cost.id
        }
        bindings.logout.setOnClickListener {
            viewModel.logOut()
        }

        bindings.stopOrder.setOnCheckedChangeListener { _, isChecked ->
            viewModel.receivingOrders.value = isChecked
            viewModel.onStopCheckChange()
        }
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun changeMenuSelection() {
        selectOrder(true)

        lifecycleScope.launch {
            selectedMenu.collect {
                when (it) {
                    bindings.orders.id -> {
                        selectOrder(true)
                        selectMenu(false)
                        selectHistory(false)
                        selectCost(false)
                    }
                    bindings.history.id -> {
                        selectOrder(false)
                        selectMenu(false)
                        selectHistory(true)
                        selectCost(false)
                    }
                    bindings.menu.id -> {
                        selectOrder(false)
                        selectMenu(true)
                        selectHistory(false)
                        selectCost(false)
                    }
                    bindings.cost.id -> {
                        selectOrder(false)
                        selectMenu(false)
                        selectHistory(false)
                        selectCost(true)
                    }
                }
            }
        }

    }


    private fun selectOrder(isSelected: Boolean) {
        bindings.orders.isSelected = isSelected
        bindings.txtOrder.setTextColor(Color.parseColor(
            if (isSelected) "#009C77" else "#CDCDCD"))
        bindings.imgOrder.isSelected = isSelected
    }

    private fun selectMenu(isSelected: Boolean) {
        bindings.menu.isSelected = isSelected
        bindings.txtMenu.setTextColor(
            Color.parseColor(
                if (isSelected) "#009C77" else "#CDCDCD"
            )
        )
        bindings.imgMenu.isSelected = isSelected
    }

    private fun selectHistory(isSelected: Boolean) {
        bindings.history.isSelected = isSelected
        bindings.txtHistory.setTextColor(
            Color.parseColor(
                if (isSelected) "#009C77" else "#CDCDCD"
            )
        )
        bindings.imgHistory.isSelected = isSelected
    }

    private fun selectCost(isSelected: Boolean) {
        bindings.cost.isSelected = isSelected
        bindings.txtCost.setTextColor(
            Color.parseColor(
                if (isSelected) "#009C77" else "#CDCDCD"
            )
        )
        bindings.imgCost.isSelected = isSelected
    }
}