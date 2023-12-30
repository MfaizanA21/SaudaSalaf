package com.example.saudasalaf.views.adminViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.saudasalaf.viewModel.fireViewModel.FirestoreViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.saudasalaf.model.Product
import com.example.saudasalaf.ui.theme.DarkTeal
import com.example.saudasalaf.ui.theme.LightTeal
import com.example.saudasalaf.ui.theme.NotWhite
import com.example.saudasalaf.utils.gradient


@Composable
fun ProductDetails(
    navController: NavController,
    viewModel: FirestoreViewModel = hiltViewModel(),
    Id: String
) {
    val state = viewModel.getProductFlow.collectAsState()
    val scroll = rememberScrollState()

    when(state.value) {
        is List<Product> -> {
            state.value?.forEach {
                if(it.productId == Id) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(NotWhite),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = it.image),
                            contentDescription = "Images",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .clip(shape = RoundedCornerShape(10.dp))
                                .padding(4.dp),
                        )
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.title,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 32.sp,
                                color = DarkTeal,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "$"+"${it.price}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp,
                                color = DarkTeal,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))

                        Text(
                            text = "Description",
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = DarkTeal,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .height(150.dp)
                                .verticalScroll(scroll)
                        ) {
                            Text(
                                text = it.description,
                                fontWeight = FontWeight.Light,
                                fontSize = 18.sp,
                                color = LightTeal,
                                softWrap = true,
                                style = TextStyle(lineBreak = LineBreak.Paragraph),
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(54.dp, 0.dp, 54.dp, 0.dp)
                                .clip(shape = RoundedCornerShape(24.dp))
                                .background(gradient)
                        ) {
                            Button(
                                onClick = { navController.navigate("editDetails/${it.productId}") },
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(
                                    text = "Edit Details",
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
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