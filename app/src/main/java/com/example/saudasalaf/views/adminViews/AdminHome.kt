package com.example.saudasalaf.views.adminViews

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import coil.compose.rememberAsyncImagePainter
import com.example.saudasalaf.model.Product
import com.example.saudasalaf.ui.theme.DarkTeal
import com.example.saudasalaf.ui.theme.LightTeal
import com.example.saudasalaf.ui.theme.NotGreen
import com.example.saudasalaf.utils.NetworkResult
import com.example.saudasalaf.viewModel.fireViewModel.FirestoreViewModel

@Composable
 fun AdminHome(navController: NavController, viewModel: FirestoreViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val life = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val scroll = rememberScrollState()

    viewModel.getProduct()

    val state = viewModel.getProductFlow.collectAsState()
    when (state.value) {
        is List<Product> -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NotGreen)
            ) {

                FloatingActionButton(
                    onClick = {
                        navController.navigate("add_product")
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(vertical = 14.dp, horizontal = 8.dp)
                ) {
                    Row(modifier = Modifier.padding(2.dp)) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            "add",
                            modifier = Modifier.size(24.dp)
                            )

                        Modifier.padding(horizontal = 4.dp)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scroll),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = "Uploaded Products",
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkTeal,
                        style = MaterialTheme.typography.titleLarge
                    )

                    state.value?.forEach {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .padding(6.dp)
                                .clickable { navController.navigate("productDetails/${it.productId}") },
                            shape = RoundedCornerShape(15.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp,
                                hoveredElevation = 8.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = LightTeal
                            )
                        ) {
                            Row {
                                Image(
                                    painter = rememberAsyncImagePainter(model = it.image),
                                    contentDescription = "Uploaded Image",
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(4.dp)
                                        .clip(shape = RoundedCornerShape(40.dp))
                                        .width(90.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Column {
                                    Text(
                                        text = it.title,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 22.sp,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )

                                    Spacer(modifier = Modifier.padding(vertical = 2.dp))

                                    Text(
                                        text = it.category,
                                        fontWeight = FontWeight.Light,
                                        fontSize = 16.sp,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Spacer(modifier = Modifier.padding(vertical = 2.dp))
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(style = SpanStyle(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 22.sp,
                                                //color = Color.White,
                                            )){
                                                append("$"+"${it.price}")
                                            }
                                            append("/kg")
                                        },
                                    modifier = Modifier.padding(2.dp)
                                    )
                                }
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    IconButton(
                                        onClick = {
                                            viewModel.deleteProduct(it.productId)
                                            viewModel.delProductLiveData.observe(life) {
                                                when(it) {
                                                    is NetworkResult.Success -> {
                                                        Toast.makeText(context, "Product Deleted", Toast.LENGTH_SHORT).show()
                                                    }
                                                    is NetworkResult.Error -> {
                                                        Toast.makeText(context, "Error Deleting Product", Toast.LENGTH_SHORT).show()
                                                    }
                                                    else -> {}
                                                }
                                            }
                                        },
                                        modifier = Modifier.align(Alignment.TopEnd)

                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            //tint = Color.White,
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
}

