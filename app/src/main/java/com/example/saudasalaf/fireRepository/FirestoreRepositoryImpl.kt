package com.example.saudasalaf.fireRepository

import android.net.Uri
import android.util.Log
import com.example.saudasalaf.model.Product
import com.example.saudasalaf.utils.NetworkResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore): FirestoreRepository {

    override val result = firestore


//    override suspend fun uploadProduct(product: Product): NetworkResult<Any> = withContext(Dispatchers.IO) {
//        return@withContext try{
//            firestore.collection("Products")
//                .document(product.productId)
//                .set(product)
//                .addOnSuccessListener {
//                    Log.d("Uploadong", "Upload Successful")
//                }
//                .addOnFailureListener {
//                    Log.d("ErrorUploadong", it.toString())
//                }
//            NetworkResult.Success(result)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            NetworkResult.Error("$e")
//        }
//    }

    override suspend fun deleteProduct(productId: String): NetworkResult<Any> = withContext(Dispatchers.IO) {
       return@withContext try {
           firestore.collection("Products").document(productId).delete()
               .addOnSuccessListener {
                   Log.d("Deleting","Successful")
               }.await()
           NetworkResult.Success(result)
       } catch (e: Exception) {
           e.printStackTrace()
           NetworkResult.Error("$e")
       }
    }

    override suspend fun updateProduct(id: String, title: String, description: String, price: Int): NetworkResult<Any> = withContext(Dispatchers.IO) {
       return@withContext try {
            firestore.collection("Prods").document(id).update(
                "title", title,
                "description", description,
                "price", price
            )
                .addOnSuccessListener {
                    Log.d("updating", "Successful")
                }.await()
            NetworkResult.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
           NetworkResult.Error("$e")
        }

    }
     override suspend fun getProduct(): List<Product> = withContext(Dispatchers.IO) {
         val products= mutableListOf<Product>()
         try{
            firestore.collection("Products").get()
                .addOnCompleteListener{data->
                    NetworkResult.Loading<Any>()
                    if(data.isSuccessful){
                        val res=data.result
                        for(document in res.documents){
                            Log.d("data",document.data.toString())
                            val product = document.toObject(Product::class.java)
                            if (product != null) {
                                products.add(product)
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.d("ErrorGettong", it.toString())
                }
                .await()

        } catch (e: Exception) {
            e.printStackTrace()
        }

         return@withContext products
    }

    private val storage = FirebaseStorage.getInstance().reference

    override suspend fun uploadProduct(product: Product, imageUri: Uri): NetworkResult<Any> {
        return try {
            val productRef = firestore.collection("Products").document(product.productId)
            val imageRef = storage.child("product_images/${productRef.id}.jpg")

            NetworkResult.Loading<Any>()

            imageRef.putFile(imageUri).await()
            val imageUrl = imageRef.downloadUrl.await().toString()

            productRef.set(product.copy(image = imageUrl)).await()

            NetworkResult.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("$e")
        }
    }

}