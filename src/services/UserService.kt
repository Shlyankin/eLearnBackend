package com.elearn.services

import com.elearn.models.*
import java.util.*
import kotlin.collections.ArrayList

class UserService {

    private val users = ArrayList<User>()

    private fun getUserPositionById(id: String): Int? {
        for (i in 0 until users.size)
            if (users[i].id == id)
                return i
        return null
    }

    private fun getUserPositionByEmail(email: String): Int? {
        for (i in 0 until users.size)
            if (users[i].email == email)
                return i
        return null
    }

    suspend fun all(): List<User> = users

    suspend fun getUser(id: String): User? {
        val position: Int? = getUserPositionById(id)
        return if(position != null) users[position]
        else return null
    }

    suspend fun update(u: User): User? {
        val id = u.id
        if (id == null) {
            return new(u)
        } else {
            val position: Int = getUserPositionById(id) ?: return new(u)
            users[position].address = u.address
            users[position].country = u.country
            users[position].email = u.email
            users[position].password = u.password
            return users[position]
        }
    }

    suspend fun signIn(email: String, password: String): User? {
        for (user in users) {
            if (user.email == email && user.password == password)
                return user
        }
        return null
    }

    suspend fun new(u: User): User? {
        getUserPositionByEmail(u.email)?.let {
            return null
        }
        u.id = UUID.randomUUID().toString()
        users.add(u)
        return u
    }

    suspend fun delete(id: String): Boolean {
        val position: Int? = getUserPositionById(id)
        return if(position != null) {
            users.removeAt(position)
            true
        } else false
    }
}