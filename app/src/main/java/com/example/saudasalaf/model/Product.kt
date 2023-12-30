package com.example.saudasalaf.model

data class Product(
    var productId: String = "",
    var title: String = "",
    var description: String = "",
    var price: Int = 0,
    var image: String? = "",
    var category: String = "",
    var isPopular: Boolean = false,
) {
    constructor(): this("","","",0,"","", false)
}
