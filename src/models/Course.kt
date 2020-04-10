package com.elearn.models

data class Course(var id: String? = null, var courseName: String, var tittle: String, var description: String,
                  var logoUrl: String, var price: String, var body: Array<String>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Course

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}