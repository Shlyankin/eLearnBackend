package com.elearn.services

import com.elearn.models.*
import kotlinx.css.em
import kotlinx.html.InputType

class UserService {

    private val users = ArrayList<User>()

    private fun getUserPosition(id: Int): Int? {
        for (i in 0 until users.size)
            if (users[i].id == id)
                return i
        return null
    }
    suspend fun all(): List<User> = users

    suspend fun getUser(id: Int): User? {
        val position: Int? = getUserPosition(id)
        return if(position != null) users[position] else return null
    }

    suspend fun update(u: User): User? {
        val id = u.id
        if (id == null) {
            return new(u)
        } else {
            val position: Int = getUserPosition(id) ?: return new(u)
            users[position].address = u.address
            users[position].country = u.country
            users[position].email = u.email
            users[position].password = u.password
            return users[position]
        }
    }

    suspend fun signIn(email: String, password: String): Boolean {
        for (user in users) {
            if (user.email == email && user.password == password) return true
        }
        return false
    }

    suspend fun new(u: User): User {
        u.id = users.size
        users.add(u)
        return u
    }

    suspend fun delete(id: Int): Boolean {
        val position: Int? = getUserPosition(id)
        return if(position != null) {
            users.removeAt(position)
            true
        } else false
    }
}