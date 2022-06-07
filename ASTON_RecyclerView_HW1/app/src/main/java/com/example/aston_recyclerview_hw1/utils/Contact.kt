package com.example.aston_recyclerview_hw1.utils

data class Contact(
    val id: Int,
    var name: String,
    var lastName: String,
    var phoneNumber: String,
    var info: String,
    var avatarUrl: String,
    var colored: Boolean = false
) {
    fun toStringArray(): Array<String> {
        return arrayOf(
            id.toString(),
            name,
            lastName,
            phoneNumber,
            info,
            avatarUrl,
            colored.toString()
        )
    }

}