package com.example.saudasalaf.views.userViews

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import coil.compose.SubcomposeAsyncImage
import com.example.saudasalaf.model.CartItems
import com.example.saudasalaf.model.Order
import com.example.saudasalaf.ui.theme.LightTeal
import com.example.saudasalaf.ui.theme.NotWhite
import com.example.saudasalaf.utils.NetworkResult
import com.example.saudasalaf.utils.gradient
import com.example.saudasalaf.viewModel.fireViewModel.CartViewModel
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Cart(
    viewModel: CartViewModel = hiltViewModel(),
    navController: NavController
) {

    var loading by remember{ mutableStateOf(false) }


    val cartState = viewModel.showCartFlow.collectAsState()
    val scroll = rememberScrollState()
    val lifeCycle = LocalLifecycleOwner.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val orderState by viewModel.placeOrder.collectAsState()

    val listOfProductId = mutableListOf<String>()

    val listOfTitle = mutableListOf<String>()

    lateinit var willOrder: Order


    val shippingCost = 25
    var subTotal by remember { mutableStateOf(0) }
    var total by remember{ mutableStateOf(0) }

    viewModel.showCart()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NotWhite)
    ) {
        Column(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()
                .verticalScroll(scroll)
                .padding(vertical = 50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "My Cart",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.padding(2.dp))

            when (cartState.value) {
                is List<CartItems> -> {
                    cartState.value?.forEach {
                        LaunchedEffect(scope) {
                            subTotal += it.price
                            listOfProductId.add(it.cartId)
                            listOfTitle.add(it.title)
                        }
                        //var quantity by remember { mutableStateOf(1) }
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .padding(6.dp),
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
                                    loading = {
                                        CircularProgressIndicator(
                                            color = LightTeal,
                                            strokeWidth = 2.dp,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .clip(shape = RoundedCornerShape(40.dp))
                                        .width(110.dp)
                                        .padding(4.dp),
                                    contentScale = ContentScale.Fit
                                )
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
                                        color = Color.Black,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Spacer(modifier = Modifier.padding(vertical = 2.dp))
                                    Text(
                                        buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 22.sp,
                                                    color = Color.Black,
                                                )
                                            ) {
                                                append("$" + "${it.price}")
                                            }
                                            append("/kg")
                                        },
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(2.dp)
                                    )
                                }
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "delete",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .align(Alignment.TopEnd)
                                            .padding(4.dp)
                                            .clickable {
                                                viewModel.deleteCart(it.cartId)
                                                viewModel.delCartLiveData.observe(lifeCycle) {
                                                    when (it) {
                                                        is NetworkResult.Success -> {
                                                            Toast
                                                                .makeText(
                                                                    context,
                                                                    "Item deleted",
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                .show()
                                                        }

                                                        is NetworkResult.Error -> {
                                                            Toast
                                                                .makeText(
                                                                    context,
                                                                    "Error deleting",
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                .show()
                                                        }

                                                        else -> {}
                                                    }
                                                }
                                            },
                                        tint = Color.Black
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
//                                    Row(
//                                        modifier = Modifier
//                                            .align(Alignment.BottomEnd)
//                                            .padding(vertical = 2.dp)
//                                    ) {
//                                        IconButton(
//                                            onClick = {
//                                                quantity++
//                                                subTotal += (it.price * quantity)
//                                                },
//                                            enabled = true,
//                                            modifier = Modifier
//                                                .padding(horizontal = 2.dp)
//                                        ) {
//                                            Icon(
//                                                imageVector = Icons.Default.Add,
//                                                contentDescription = "Plus",
//                                                modifier = Modifier.size(30.dp),
//                                                tint = DarkTeal
//                                            )
//                                        }
//                                        Text(
//                                            text = "$quantity",
//                                            fontWeight = FontWeight.Medium,
//                                            color = LightTeal,
//                                            fontSize = 14.sp,
//                                            textAlign = TextAlign.Center
//                                        )
//
//                                        IconButton(
//                                            onClick = {
//                                                if (quantity > 1) {
//                                                    quantity--
//                                                    subTotal -= (it.price * quantity)
//                                                }
//                                                if (quantity == 1) {
//                                                    quantity = 1
//                                                    subTotal += it.price
//                                                }
//                                            },
//                                            enabled = true
//                                        ) {
//                                            Icon(
//                                                imageVector = Icons.Default.Close,
//                                                contentDescription = "Minus",
//                                                tint = DarkTeal
//                                            )
//                                        }
//                                    }
                                }
                                LaunchedEffect( scope ){
                                    willOrder = Order(
                                        productId = listOfProductId,
                                        title = listOfTitle,
                                        price = subTotal.toString(),
                                        time = LocalDateTime.now()
                                    )
                                }
                            }
                        }
                    }
                }

                else -> {
                    CircularProgressIndicator(
                        color = LightTeal,
                        modifier = Modifier
                            .size(28.dp)
                            .fillMaxWidth(),
                        strokeWidth = 2.dp
                    )
                }

            }

        }
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 40.dp
            ),
            colors = CardDefaults.elevatedCardColors(
              containerColor = Color.White
            ),
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                //.background(IsItEvenGreen)
                .align(Alignment.BottomCenter)
                //.clip(shape = RoundedCornerShape(5.dp))
        ) {

                total = subTotal + shippingCost
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
            )
            {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    Text(
                        text = "Sub Total",
                        color = Color.Gray,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "$$subTotal",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Shipping Cost",
                        color = Color.Gray,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "$$shippingCost",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Total",
                        color = Color.Gray,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "$$total",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Box(
                    modifier = Modifier
                        .padding(54.dp, 0.dp, 54.dp, 0.dp)

                        .clip(shape = RoundedCornerShape(24.dp))
                        .background(gradient)
                ) {
                    Button(
                        onClick = {
                            viewModel.placeOrder(willOrder)
                            orderState.let {
                                when(it) {
                                    is NetworkResult.Success -> {
                                        Toast.makeText(context, "Order Placed", Toast.LENGTH_LONG).show()
                                    }
                                    is NetworkResult.Error -> {
                                        Toast.makeText(context, "Couldnot Place Order", Toast.LENGTH_LONG).show()
                                    }
                                    else -> {}
                                }
                            }

                        },
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Order",
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