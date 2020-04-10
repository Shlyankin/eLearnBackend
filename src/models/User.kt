package com.elearn.models

data class User(var email: String, var country: String, var address : String, var password: String){
    var id : String? = null
}