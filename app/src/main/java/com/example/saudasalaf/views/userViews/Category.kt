package com.example.saudasalaf.views.userViews

import android.hardware.lights.Light
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.saudasalaf.model.Product
import com.example.saudasalaf.ui.theme.DarkTeal
import com.example.saudasalaf.ui.theme.IsItEvenGreen
import com.example.saudasalaf.ui.theme.LightTeal
import com.example.saudasalaf.ui.theme.NotGreen
import com.example.saudasalaf.ui.theme.NotWhite
import com.example.saudasalaf.viewModel.fireViewModel.CartViewModel
import com.example.saudasalaf.viewModel.fireViewModel.FirestoreViewModel

@Composable
fun Category(
    navController: NavController,
    viewModel: FirestoreViewModel = hiltViewModel(),
    fireModel: CartViewModel = hiltViewModel(),
) {

    viewModel.getProduct()
    val products = viewModel.getProductFlow.collectAsState()


    val categories = listOf(
        "Fruits",
        "Vegetables",
        "Meat",
        "Others"
    )

    var filter by remember {
        mutableStateOf("Fruits")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NotWhite)
            .padding(vertical = 50.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Categories",
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(4.dp)
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories.forEach { cat->
                ElevatedButton(
                    onClick = {
                        filter = cat
                              },
                    shape = RoundedCornerShape(20.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = IsItEvenGreen,
                        containerColor = NotGreen,
                        disabledContentColor = LightTeal,
                        contentColor = DarkTeal
                    )
                ) {
                    Text(
                        text = cat,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
        when(products.value) {
            is List<Product> -> {
                //Log.d("Sizee", products.value!!.size.toString())
                products.value?.forEach {

                    if(it.category.contains(filter,true)) {
                        Log.d("Sizee", filter)
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .padding(6.dp)
                                .clickable { navController.navigate("productDetail/${it.productId}") },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = LightTeal
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp,
                                hoveredElevation = 8.dp,
                                focusedElevation = 8.dp
                            )
                        ) {
                            Row {
                                SubcomposeAsyncImage(
                                    model = it.image,
                                    contentDescription = null,
                                    loading = { CircularProgressIndicator(
                                        color = LightTeal,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(24.dp)
                                    ) },
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .clip(shape = RoundedCornerShape(40.dp))
                                        .width(110.dp)
                                        .padding(4.dp),
                                    contentScale = ContentScale.Fit
                                )
//                                Image(
//                                    painter = rememberAsyncImagePainter(model = it.image),
//                                    contentDescription = "Uploaded Image",
//                                    modifier = Modifier
//                                        .fillMaxHeight()
//                                        .clip(shape = RoundedCornerShape(40.dp))
//                                        .width(110.dp)
//                                        .padding(4.dp),
//                                    contentScale = ContentScale.Fit
//                                )
                                Column {
                                    Text(
                                        text = it.title,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 22.sp,
                                        color = Color.Black,
                                        textAlign = TextAlign.Start,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )

                                    Spacer(modifier = Modifier.padding(vertical = 2.dp))

                                    Text(
                                        text = it.category,
                                        fontWeight = FontWeight.Light,
                                        fontSize = 16.sp,
                                        fontStyle = FontStyle.Italic,
                                        color = Color.Black
                                        )
                                    Spacer(modifier = Modifier.padding(vertical = 2.dp))
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(style = SpanStyle(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 22.sp,
                                                color = Color.Black
                                                )
                                            ){
                                                append("$"+"${it.price}")
                                            }
                                            append("/kg")
                                        },
                                        modifier = Modifier.padding(2.dp),
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}