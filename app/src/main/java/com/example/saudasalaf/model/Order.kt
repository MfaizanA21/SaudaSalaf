package com.example.saudasalaf.model

import com.google.type.DateTime
import java.time.LocalDateTime

data class Order(
    var orderId: String = "",
    var productId: List<String> = emptyList(),
    var title: List<String> = emptyList(),
    var userId: String? = "",
    var price: String = "",
    //var time: String = SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z").format(Date()),
    var time: LocalDateTime?
) {
    //constructor(): this("","","","","")
}
