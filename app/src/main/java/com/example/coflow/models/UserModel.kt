package com.example.coflow.models

class UserModel (
    val id: Int,
    _username: String,
    _email: String
) {
    var username: String = _username.trim()
        set(value){
            require(value.isNotBlank())
            field = value.trim()
        }

    var email: String = _email.trim()
        set(value) {
            require(value.isNotBlank())
            field = value.trim()
        }
}