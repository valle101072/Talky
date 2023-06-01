package com.example.talk.models

data class User(
    val id:String = "",
    var phone:String = "",
    var username:String = "",
    var fullname:String = "",
    var bio:String = "",
    var state:String = "",
    var photoURL: String = "empty"
)