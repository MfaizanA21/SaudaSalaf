package com.example.saudasalaf.views.adminViews

import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.saudasalaf.model.Product
import com.example.saudasalaf.ui.theme.DarkTeal
import com.example.saudasalaf.ui.theme.LightTeal
import com.example.saudasalaf.ui.theme.NotGreen
import com.example.saudasalaf.utils.NetworkResult
import com.example.saudasalaf.utils.gradient
import com.example.saudasalaf.viewModel.fireViewModel.FirestoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDetails(
    navController: NavController,
    viewModel: FirestoreViewModel = hiltViewModel(),
    id: String
) {

    var title by remember{mutableStateOf("")}
    var desc by remember{mutableStateOf("")}
    var price by remember{mutableStateOf(0)}
    var isPopular by remember{ mutableStateOf(true) }

    val context = LocalContext.current
    val life = LocalLifecycleOwner.current

    var progressFlag = false

    val state = viewModel.getProductFlow.collectAsState()

    when(state.value) {
        is List<Product> -> {
            state.value?.forEach { product ->
                if(product.productId == id) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = NotGreen)
                    ) {
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text(text = "name", color = LightTeal) },
                            placeholder = { Text(text = "Apples", color = LightTeal) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "product Id",
                                    tint = LightTeal
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                disabledTextColor = LightTeal,
                                textColor = DarkTeal,
                                focusedIndicatorColor = DarkTeal,
                                unfocusedIndicatorColor = LightTeal
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        TextField(
                            value = desc,
                            onValueChange = { desc = it },
                            label = { Text(text = "Description", color = LightTeal) },
                            placeholder = { Text(text = "Fresh Apples...", color = LightTeal) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "product description",
                                    tint = LightTeal
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                disabledTextColor = LightTeal,
                                textColor = DarkTeal,
                                focusedIndicatorColor = DarkTeal,
                                unfocusedIndicatorColor = LightTeal
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            singleLine = false
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        TextField(
                            value = price.toString(),
                            onValueChange = { price = it.toInt() },
                            label = { Text(text = "Price", color = LightTeal) },
                            placeholder = { Text(text = "0", color = LightTeal) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "product price",
                                    tint = LightTeal
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                disabledTextColor = LightTeal,
                                textColor = DarkTeal,
                                focusedIndicatorColor = DarkTeal,
                                unfocusedIndicatorColor = LightTeal
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = false
                        )

                        Spacer(modifier = Modifier.padding(8.dp))

                        Box(
                            modifier = Modifier
                                .padding(54.dp, 0.dp, 54.dp, 0.dp)

                                .clip(shape = RoundedCornerShape(24.dp))
                                .background(gradient)
                        ) {
                            Button(
                                onClick = {
                                    if(TextUtils.isEmpty(title) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(price.toString())) {
                                        Toast.makeText(context, "Please fill all Fields", Toast.LENGTH_LONG).show()
                                    }
                                    else {
                                        viewModel.updateProduct(id, title = title, description = desc, price = price)
                                        viewModel.updateProduct.observe(life) {
                                            when(it) {
                                                is NetworkResult.Success -> {
                                                    // val product = Product(title = title, price = price, description = desc)

                                                    Toast.makeText(context, "Updated Successfully", Toast.LENGTH_LONG).show()
                                                    navController.popBackStack()
                                                }
                                                is NetworkResult.Error -> {
                                                    Toast.makeText(context, "Something is not right", Toast.LENGTH_LONG).show()
                                                }

                                                else -> {
                                                    progressFlag = true
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
                                Text(
                                    text = "Submit",
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                )
                                if(progressFlag) {
                                    CircularProgressIndicator(color = Color.White, strokeWidth = 12.dp)
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