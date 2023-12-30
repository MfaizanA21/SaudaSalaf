package com.example.saudasalaf.views.userViews

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.saudasalaf.R
import com.example.saudasalaf.model.Product
import com.example.saudasalaf.ui.theme.LightTeal
import com.example.saudasalaf.ui.theme.NotWhite
import com.example.saudasalaf.viewModel.fireViewModel.FirestoreViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(navController: NavController, viewModel: FirestoreViewModel = hiltViewModel()) {

    var loading by remember {
        mutableStateOf(true)
    }

    val scroll = rememberScrollState()

    val products = viewModel.getProductFlow.collectAsState()

    viewModel.getProduct()

    val state = rememberPagerState()
    val carousal = listOf(
        R.drawable.fresh_banner,
        R.drawable.super_banner
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NotWhite),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(55.dp))
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(10.dp))
                .height(230.dp)
                .padding(10.dp)
                ){
            HorizontalPager(
                pageCount = carousal.size,
                state = state,
            ) {
                Image(
                    painter = painterResource(id = carousal[it]),
                    contentDescription = "Delivery",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(5.dp))
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp)
                .height(45.dp)
                .background(Color.Transparent)
                .clip(shape = RoundedCornerShape(5.dp))

        ) {
            Text(
                text = "Popular",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )

        }
        Spacer(modifier = Modifier.padding(4.dp))
        Row (
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .horizontalScroll(scroll),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ){
        when(products.value) {
            is List<Product> -> {
                products.value?.forEach {
                    if (it.isPopular) {
                        loading = true
                        Card(
                            modifier = Modifier
                                .size(width = 150.dp, height = 180.dp)
                                .padding(4.dp)
                                .clickable { navController.navigate("productDetail/${it.productId}") },
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 1.dp,
                                hoveredElevation = 4.dp
                            ),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = LightTeal
                            )
                        ) {
                            SubcomposeAsyncImage(
                                model = it.image,
                                contentDescription = null,
                                loading = { CircularProgressIndicator(
                                    color = LightTeal,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(10.dp)
                                )},
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(20.dp))
                                    .width(150.dp)
                                    .padding(4.dp)
                                    .height(80.dp)
                            )

                            Spacer(modifier = Modifier.padding(3.dp))
                            Text(
                                text = it.title,
                                fontSize = 24.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(horizontal = 2.dp),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.padding(3.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 22.sp,
                                            color = Color.Black,
                                        )
                                    ) {
                                        append("$" + it.price)
                                    }
                                    append("/kg")
                                },
                                modifier = Modifier.padding(horizontal = 2.dp),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        }
                    }
                }
            else -> {
                CircularProgressIndicator(
                    color = LightTeal,
                    modifier = Modifier
                        .size(50.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    strokeWidth = 2.dp
                )
            }
            }
        }
        Spacer(modifier = Modifier.padding(2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp)
                .height(45.dp)
                .background(Color.Transparent)
                .clip(shape = RoundedCornerShape(5.dp))

        ) {
            Text(
                text = "Newly Added",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}