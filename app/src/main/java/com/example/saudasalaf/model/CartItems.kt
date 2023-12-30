package com.example.saudasalaf.model

data class CartItems(
    var userId: String = "",
    var cartId: String = "",
    var title: String = "",
    var price: Int = 0,
    var description: String = "",
    var image: String = "",
    var category: String = ""
) {
    constructor(): this("","","",0,"","","")
}
