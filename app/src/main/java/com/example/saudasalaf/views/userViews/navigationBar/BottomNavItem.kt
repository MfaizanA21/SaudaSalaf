package com.example.saudasalaf.views.userViews.navigationBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.saudasalaf.R

sealed class BottomNavItem(var title: String, var icon:Int, var route: String){
    object Home:BottomNavItem("Home", R.drawable.home, "home_screen")
    object Category:BottomNavItem("Category", R.drawable.category, "category_screen")
    object Cart:BottomNavItem("Cart", R.drawable.cart, "cart_screen")
    object Settings:BottomNavItem("Settings", R.drawable.settings, "setting_screen")
}


