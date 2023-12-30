package com.example.saudasalaf.views.adminViews

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct(navController: NavController, viewModel: FirestoreViewModel = hiltViewModel()) {

    var pId by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var price:Int by remember { mutableStateOf(0) }
    var image by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var popular by remember { mutableStateOf(false) }


    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(), onResult = {uri: Uri? -> imageUri = uri})
    val uploadResult by viewModel.uploadProduct.collectAsState()


    val context = LocalContext.current
    val life = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        onResult = { uri: Uri? ->  image = uri.toString() }
//    )



        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = NotGreen)
        ) {
            Text(
                text = "Enter Details",
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkTeal,
                style = MaterialTheme.typography.titleLarge
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
                        //launcher.launch("image/*")
                        launcher.launch("image/*")

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Upload Image",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            TextField(
                value = pId,
                onValueChange = { pId = it },
                label = { Text(text = "Product Id", color = LightTeal) },
                placeholder = { Text(text = "PF 001", color = LightTeal) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Star,
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
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Name", color = LightTeal) },
                placeholder = { Text(text = "Red Apples", color = LightTeal) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "product name",
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
                placeholder = { Text(text = "000", color = LightTeal) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "price",
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
                singleLine = true
            )
            Spacer(modifier = Modifier.padding(8.dp))

            TextField(
                value = category,
                onValueChange = { category = it },
                label = { Text(text = "category", color = LightTeal) },
                placeholder = {
                    Text(
                        text = "Fruit, Vegetable, etc...",
                        color = LightTeal
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Category",
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
                singleLine = true
            )



            Spacer(modifier = Modifier.padding(4.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val product = Product(
                            title = title,
                            productId = pId,
                            description = desc,
                            price = price,
                            category = category,
                            isPopular = true,
                            //image = image
                        )
                        val validate = viewModel.required(product)
                        scope.launch {
                            if (validate.first) {
                                //product.let { viewModel.uploadProduct(product) }
                                imageUri?.let { viewModel.uploadProduct(product, it) }
                                //product.let { imageUri?.let { it1 -> viewModel.withImage(product, it1) } }
                            } else {
                                Toast.makeText(context, validate.second, Toast.LENGTH_LONG).show()
                            }

                            uploadResult?.let { result ->
                                when (result) {
                                    is NetworkResult.Success -> {
                                        Toast.makeText(context, "Uploaded", Toast.LENGTH_LONG)
                                            .show()
                                        navController.popBackStack()
                                    }

                                    is NetworkResult.Error -> {
                                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                                        navController.popBackStack()
                                    }

                                    else -> {}
                                }
                            }

                            //                        viewModel.addProductLiveData.observe(life){
                            //
                            //                            when(it) {
                            //                                is NetworkResult.Success -> {
                            //                                    Toast.makeText(context, "Saved!", Toast.LENGTH_LONG).show()
                            //                                    navController.popBackStack()
                            //                                }
                            //                                is NetworkResult.Error -> {
                            //                                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_LONG).show()
                            //                                }
                            //                                is NetworkResult.Loading -> {
                            //
                            //                                }
                            //                                else -> {
                            //
                            //                                }
                            //                            }
                            //                        }
                        }
                    },
                ) {
                    Text(
                        text = "Submit",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                    )
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


//    when(upResult) {
//        is NetworkResult.Success -> {
//            Toast.makeText(context, "Saved!", Toast.LENGTH_LONG).show()
//        }
//        is NetworkResult.Error -> {
//            Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show()
//        }
//
//        else -> {}
//    }
}