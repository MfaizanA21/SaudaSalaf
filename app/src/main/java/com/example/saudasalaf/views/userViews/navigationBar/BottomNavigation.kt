package com.example.saudasalaf.views.userViews.navigationBar

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.saudasalaf.ui.theme.DarkTeal
import com.example.saudasalaf.ui.theme.LightTeal
import com.example.saudasalaf.ui.theme.NotGreen

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Category,
        BottomNavItem.Cart,
        BottomNavItem.Settings,
    )
    BottomAppBar(
        containerColor = NotGreen,
        contentColor = LightTeal,
        modifier = Modifier.clip(shape = RoundedCornerShape(15.dp)),
        tonalElevation = 25.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = "icon",
                        modifier = Modifier.size(28.dp)
                    )},
                label = { Text(text = item.title, fontSize = 10.sp)},
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DarkTeal,
                    unselectedIconColor = LightTeal,
                    indicatorColor = Color.White,
                    selectedTextColor = DarkTeal,
                    unselectedTextColor = LightTeal
                ),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) {
                    navController.graph.startDestinationRoute?.let{
                        route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                    }
                }
            )
        }
    }
}