package com.example.saudasalaf.viewModel.fireViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saudasalaf.fireRepository.FirestoreCartRepository
import com.example.saudasalaf.model.CartItems
import com.example.saudasalaf.model.Order
import com.example.saudasalaf.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: FirestoreCartRepository): ViewModel() {

    val addCart = MutableStateFlow<NetworkResult<Any>?>(null)

    private val _showCartFlow = MutableStateFlow<List<CartItems>?>(null)
    val showCartFlow: StateFlow<List<CartItems>?> = _showCartFlow

    private val _delCartLiveData = MutableLiveData<NetworkResult<Any>?>()
    val delCartLiveData: LiveData<NetworkResult<Any>?> = _delCartLiveData

    val placeOrder = MutableStateFlow<NetworkResult<Any>?>(null)

    fun addToCart(cart: CartItems) = viewModelScope.launch {
        val added = cartRepository.addToCart(cart)
        addCart.value = added
    }

    fun showCart() = viewModelScope.launch {
        val got = cartRepository.showCart()
        _showCartFlow.value = got
    }

    fun deleteCart(cartId: String) = viewModelScope.launch {
        val del = cartRepository.removeFromCart(cartId)
        _delCartLiveData.value = del
    }

    fun placeOrder(order: Order) = viewModelScope.launch {
        val placed = cartRepository.placeOrder(order)
        placeOrder.value = placed
    }

}