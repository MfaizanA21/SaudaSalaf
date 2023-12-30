package com.example.saudasalaf.viewModel.fireViewModel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saudasalaf.fireRepository.AuthRepository
import com.example.saudasalaf.model.User
import com.example.saudasalaf.utils.NetworkResult
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository): ViewModel() {
    private val _loginLiveData = MutableLiveData<NetworkResult<FirebaseUser>?>()
    val loginLiveData: LiveData<NetworkResult<FirebaseUser>?> = _loginLiveData

    private val _signupLiveData = MutableLiveData<NetworkResult<FirebaseUser>?>(null)
    val signupLiveData: LiveData<NetworkResult<FirebaseUser>?> = _signupLiveData

    private val _adminLiveData = MutableLiveData<Boolean?>(null)
    val adminLiveData: LiveData<Boolean?> = _adminLiveData

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    suspend fun login(user: User) = viewModelScope.launch {
        val result = repository.login(user)
        _loginLiveData.value = result
    }

    suspend fun signup(user: User) = viewModelScope.launch {
        val result = repository.signup(user)
        _signupLiveData.value = result
    }

    suspend fun adminLogin(user: User) = viewModelScope.launch {
        val result = repository.adminLogin(user)
        _adminLiveData.value = result
    }

    fun validation(user: User, isLogin: Boolean): Pair<Boolean, String> {

        val withDigitAndChar = user.password.any { it.isDigit() } && user.password.any { it.isLetter() }
        var result = Pair(true, "")

        if(!isLogin && TextUtils.isEmpty(user.username) || TextUtils.isEmpty(user.password) || TextUtils.isEmpty(user.email)) {
            result = Pair(false, "Please Fill all Fields")
        }
        else if (user.password.length < 8 && !withDigitAndChar) {
            result = Pair(false, "Password Must be of at least 8 characters and Must Contain Special Character")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
            result = Pair(false, "Please enter valid Email Address")
        }
        return result
    }
}