package com.example.saudasalaf.viewModel.fireViewModel

import android.net.Uri
import android.text.TextUtils
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saudasalaf.fireRepository.FirestoreRepository
import com.example.saudasalaf.model.CartItems
import com.example.saudasalaf.model.Product
import com.example.saudasalaf.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirestoreViewModel @Inject constructor(private val fireRepository : FirestoreRepository): ViewModel() {

    init {
        getProduct()
    }

    private val _getProductFlow = MutableStateFlow<List<Product>?>(null)
    val getProductFlow: StateFlow<List<Product>?> = _getProductFlow

//    private val _addProductLiveData = MutableLiveData<NetworkResult<Any>?>()
//    val addProductLiveData: LiveData<NetworkResult<Any>?> = _addProductLiveData

    private val _delProductLiveData = MutableLiveData<NetworkResult<Any>?>()
    val delProductLiveData: LiveData<NetworkResult<Any>?> = _delProductLiveData

    val uploadProduct = MutableStateFlow<NetworkResult<Any>?>(null)

    private val _updateProduct = MutableLiveData<NetworkResult<Any>?>(null)
    val updateProduct: LiveData<NetworkResult<Any>?> = _updateProduct


    fun getProduct() = viewModelScope.launch {
        val result = fireRepository.getProduct()
        _getProductFlow.value = result
    }

//    fun uploadProduct(product: Product) = viewModelScope.launch {
//        val result = fireRepository.uploadProduct(product)
//        _addProductLiveData.value = result
//    }

    fun deleteProduct(productId: String) = viewModelScope.launch {
        val deletes = fireRepository.deleteProduct(productId)
        _delProductLiveData.value = deletes
    }

    fun uploadProduct(product: Product, imageUri: Uri)= viewModelScope.launch{
        val up = fireRepository.uploadProduct(product, imageUri)
        uploadProduct.value = up
    }

    fun updateProduct(id: String, title: String, description: String, price: Int) = viewModelScope.launch {
        val updated = fireRepository.updateProduct(id, title, description, price)
        _updateProduct.value = updated

    }



    fun required(product: Product): Pair<Boolean, String> {
        var result = Pair(true, "")
        if(
            TextUtils.isEmpty(product.title)
            || TextUtils.isEmpty(product.description)
            || TextUtils.isEmpty(product.productId)
            || TextUtils.isEmpty(product.category)
            || TextUtils.isEmpty(product.price.toString())
//            || TextUtils.isEmpty(product.image.toString())

        ) {
            result = Pair(false, "Please fill all Details, Nothing must be empty")
        }
        return result
    }

}