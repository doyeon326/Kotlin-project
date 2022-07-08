package com.doyeon.chapter6.tradingapp.chatDetail

data class ChatItem (
    val senderId: String,
    val message: String,
    ) {
    constructor(): this("","")
}