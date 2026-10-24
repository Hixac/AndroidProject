package com.example.coflow.data.firebase

import com.example.coflow.models.UserModel

fun UserSchema.toDomain() : UserModel{
    return UserModel(
        id = this.uid,
        name = this.name,
        email = this.email
    )
}

fun UserModel.toSchema() : UserSchema{
    return UserSchema(
        uid = this.id,
        name = this.name,
        email = this.email
    )
}