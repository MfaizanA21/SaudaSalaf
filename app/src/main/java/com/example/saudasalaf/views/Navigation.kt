package com.example.saudasalaf.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.saudasalaf.ui.theme.DarkTeal
import com.example.saudasalaf.ui.theme.NotGreen
import com.example.saudasalaf.views.adminViews.AddProduct
import com.example.saudasalaf.views.adminViews.AdminHome
import com.example.saudasalaf.views.adminViews.AdminLogin
import com.example.saudasalaf.views.adminViews.EditDetails
import com.example.saudasalaf.views.adminViews.ProductDetails
import com.example.saudasalaf.views.userViews.Cart
import com.example.saudasalaf.views.userViews.Category
import com.example.saudasalaf.views.userViews.Home
import com.example.saudasalaf.views.userViews.LoginScreen
import com.example.saudasalaf.views.userViews.ProductDetail
import com.example.saudasalaf.views.userViews.Settings
import com.example.saudasalaf.views.userViews.SignupScreen
import com.example.saudasalaf.views.userViews.navigationBar.BottomNavItem
import com.example.saudasalaf.views.userViews.navigationBar.BottomNavigation


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = BottomNavItem.Home.route) {
        composable("login_screen") {
            LoginScreen(navController = navController)
        }
        composable("signup_screen") {
            SignupScreen(navController = navController)
        }
        composable("adminLogin") {
            AdminLogin(navController = navController)
        }
        composable("adminHome") {
            AdminHome(navController = navController)
        }
        composable(BottomNavItem.Home.route) {
            Home(navController)
        }
        composable("add_product") {
            AddProduct(navController = navController)
        }
        composable(
            route = "productDetails" + "/{id}",
            arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
                nullable = false
            })
        ){
            val id = it.arguments?.getString("id")
            ProductDetails(navController = navController, Id = id!!)
        }

        composable(BottomNavItem.Category.route){
            Category(navController = navController)
        }
        composable(BottomNavItem.Cart.route) {
            Cart(navController = navController)
        }
        composable(BottomNavItem.Settings.route) {
            Settings()
        }
        composable(
            "editDetails" + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                    nullable = false
                })
        ) {
            val id = it.arguments?.getString("id")
            EditDetails(navController = navController, id = id!!)
        }
        composable(
            "productDetail" + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            val id = it.arguments?.getString("id")
            ProductDetail(navController = navController, id = id!!)
        }
}
    val currentRoute = currentRoute(navController)

    val shouldShowBottomBar = currentRoute != null && shouldShowBottomBar(currentRoute)


    Scaffold(
        bottomBar = {
            if(shouldShowBottomBar){
                BottomNavigation(navController = navController)
            }
        },
        topBar = {
            if(shouldShowBottomBar){

                Row (modifier = Modifier
                    .background(NotGreen)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center){
                    Text(
                        text = "Sauda Salaf",
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkTeal,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) {

        NavigateToScreen(navController,currentRoute)
//        when(currentRoute) {
//            BottomNavItem.Home.route -> Home()
//            BottomNavItem.Category.route -> Category()
//            BottomNavItem.Settings.route -> Settings()
//            BottomNavItem.Orders.route -> Orders()
//            else -> {
//                when(currentRoute) {
//                    "login_screen" -> LoginScreen(navController = navController)
//                    "signup_screen" -> SignupScreen(navController = navController)
//                    "adminLogin" -> AdminLogin(navController = navController)
//                    "adminHome" -> AdminHome(navController = navController)
//                    "add_product" -> AddProduct(navController = navController)
//                    "productDetails" -> {
//
//                    }
//
//                }
//            }
//        }
    }
}

@Composable
fun NavigateToScreen(navController: NavHostController, route: String?) {
    when(route) {
        BottomNavItem.Home.route -> Home(navController = navController)
        BottomNavItem.Cart.route -> Cart(navController = navController)
        BottomNavItem.Settings.route -> Settings()
        BottomNavItem.Category.route -> Category(navController = navController)
        "login_screen" -> LoginScreen(navController = navController)
        "signup_screen" -> SignupScreen(navController = navController)
        "adminLogin" -> AdminLogin(navController = navController)
        "adminHome" -> AdminHome(navController = navController)
        "add_product" -> AddProduct(navController = navController)
        "productDetails" + "/{id}" -> {
            val id = navController.currentBackStackEntry?.arguments?.getString("id")
            ProductDetails(navController = navController, Id = id!!)
        }
        "editDetails" + "/{id}" -> {
            val id = navController.currentBackStackEntry?.arguments?.getString("id")
            EditDetails(navController = navController, id = id!!)
        }
        "productDetail" + "/{id}" -> {
            val id = navController.currentBackStackEntry?.arguments?.getString("id")
            ProductDetail(navController = navController, id = id!!)
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

private fun shouldShowBottomBar(route: String): Boolean {
    return route in listOf(
        BottomNavItem.Home.route,
        BottomNavItem.Settings.route,
        BottomNavItem.Category.route,
        BottomNavItem.Cart.route,
        "productDetail" + "/{id}"
    )
}


