package com.example.saudasalaf.views.userViews

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.saudasalaf.R
import com.example.saudasalaf.fireRepository.FirestoreCartRepository
import com.example.saudasalaf.model.CartItems
import com.example.saudasalaf.model.Product
import com.example.saudasalaf.ui.theme.DarkTeal
import com.example.saudasalaf.ui.theme.LightTeal
import com.example.saudasalaf.ui.theme.NotWhite
import com.example.saudasalaf.utils.NetworkResult
import com.example.saudasalaf.utils.gradient
import com.example.saudasalaf.viewModel.fireViewModel.CartViewModel
import com.example.saudasalaf.viewModel.fireViewModel.FirestoreViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductDetail(
    navController: NavController,
    viewModel: FirestoreViewModel = hiltViewModel(),
    vm: CartViewModel = hiltViewModel(),
    id: String
) {

    var loading  by remember {
        mutableStateOf(false)
    }

    viewModel.getProduct()
    val state = viewModel.getProductFlow.collectAsState()

    var existed = false

    val exists = vm.showCartFlow.collectAsState().value

    val cartState by vm.addCart.collectAsState()

    val context = LocalContext.current
    val life = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val scroll = rememberScrollState()

    when (state.value) {
        is List<Product> -> {
            state.value?.forEach {
                if (it.productId == id) {
                    val cart = CartItems(
                        cartId = it.productId,
                        title = it.title,
                        price = it.price,
                        description = it.description,
                        image = it.image!!,
                        category = it.category,
                    )
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(NotWhite),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.every),
                            contentDescription = "every detail",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .height(350.dp)
                                .clip(shape = RoundedCornerShape(45.dp))
                        )
                        Spacer(modifier = Modifier.padding(4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = it.title,
                                fontWeight = FontWeight.Normal,
                                fontSize = 32.sp,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "$" + it.price.toString(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 32.sp,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = "Description",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Start,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scroll)
                                .padding(horizontal = 12.dp)
                                .height(125.dp)
                        ) {
                            Text(
                                text = it.description,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Light,
                                textAlign = TextAlign.Start,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Box(
                            modifier = Modifier
                                .padding(54.dp, 0.dp, 54.dp, 0.dp)
                                .clip(shape = RoundedCornerShape(24.dp))
                                .background(gradient)
                        ) {
                            Button(
                                onClick = {
                                    loading = true
                                    vm.showCart()
                                    exists?.forEach { existing ->
                                        if(cart.cartId == existing.cartId) {
                                            loading = false
                                            Toast.makeText(context, "Already in Cart", Toast.LENGTH_SHORT).show()
                                            existed = true
                                        }
                                    }
                                    if(!existed) {
                                        loading = true
                                        vm.addToCart(cart)
                                        cartState.let {
                                            when (it) {
                                                is NetworkResult.Success -> {
                                                    loading = false
                                                    Toast.makeText(
                                                        context,
                                                        "Added to Cart",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }

                                                is NetworkResult.Error -> {
                                                    loading = false
                                                    Toast.makeText(
                                                        context,
                                                        "Error",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }

                                                is NetworkResult.Loading -> {
                                                    loading = true
                                                }

                                                else -> {
                                                    loading = true
                                                }
                                            }
                                        }
                                    }

                                },
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                if(!loading){
                                    Text(
                                        text = "Add to Cart",
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 18.sp,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                                else {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(32.dp),
                                        strokeWidth = 3.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }


        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "back",
            tint = DarkTeal,
            modifier = Modifier
                .clickable { navController.popBackStack() }
                .align(Alignment.TopStart)
                .padding(4.dp)
        )
    }

}
