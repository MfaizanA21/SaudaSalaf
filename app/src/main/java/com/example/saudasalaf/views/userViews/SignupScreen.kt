package com.example.saudasalaf.views.userViews

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.saudasalaf.model.User
import com.example.saudasalaf.ui.theme.DarkTeal
import com.example.saudasalaf.ui.theme.LightTeal
import com.example.saudasalaf.ui.theme.NotGreen
import com.example.saudasalaf.utils.NetworkResult
import com.example.saudasalaf.utils.gradient
import com.example.saudasalaf.viewModel.fireViewModel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val lifeCycle = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NotGreen),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create a new account",
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DarkTeal,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.padding(12.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username", color = LightTeal) },
            placeholder = { Text(text = "Jonah Hill", color = LightTeal) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "email",
                    tint = LightTeal
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = DarkTeal,
                focusedBorderColor = DarkTeal,
                unfocusedBorderColor = LightTeal
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email", color = LightTeal) },
            placeholder = { Text(text = "jonnah2@gmail.com", color = LightTeal) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "email",
                    tint = LightTeal
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = DarkTeal,
                focusedBorderColor = DarkTeal,
                unfocusedBorderColor = LightTeal
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "password", color = LightTeal) },
            placeholder = { Text(text = "......", color = LightTeal)},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "password",
                    tint = LightTeal
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = DarkTeal,
                focusedBorderColor = DarkTeal,
                unfocusedBorderColor = LightTeal
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(15.dp))

        Box(
            modifier = Modifier
                .padding(54.dp, 0.dp, 54.dp, 0.dp)
                .clip(shape = RoundedCornerShape(24.dp))
                .background(gradient)
        ) {
            Button(
                onClick = {
                    loading = true
                    val use = User(username, email, password)
                    val valid = viewModel.validation(use, false)
                    scope.launch {
                        if(valid.first) {
                            use.let { viewModel.signup(it) }
                        } else {
                            loading = false
                            Toast.makeText(context, valid.second, Toast.LENGTH_SHORT).show()
                        }
                        viewModel.signupLiveData.observe(lifeCycle) {
                            when(it) {
                                is NetworkResult.Success -> {
                                    loading = false
                                    navController.navigate("home_screen")
                                }
                                is NetworkResult.Error -> {
                                    loading = false
                                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                }
                                is NetworkResult.Loading -> {
                                    loading = true
                                }

                                else -> {
                                    loading = true
                                    Log.d("Nulll","It is Null")
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
            )  {
                if(!loading){
                    Text(
                        text = "Signup",
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

        Spacer(modifier = Modifier.padding(20.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text(
                text = "Already have an account?",
                color = LightTeal,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Light
            )
        }
    }
}