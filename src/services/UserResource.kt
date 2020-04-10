package com.elearn.services

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.elearn.models.User
import com.elearn.services.UserService
import com.sun.org.apache.xpath.internal.operations.Bool
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.http.cio.websocket.*
import io.ktor.websocket.webSocket
import org.apache.http.HttpStatus

/*

GET
.../user/
get all users
arg: Nothing
return: List<User>

POST
.../user/
create new user
(send me user info)
arg: User: JSON object (without id)
return: User


GET
.../user/{id}
get user by id
arg: id
return: User

PUT
.../user/
update user or create new user
arg: User: JSON object
return: status

DELETE
.../user/{id}
remove user
arg: id
return: status

 */


fun Route.widget(userService: UserService){
    val WIDGET_END_POINT_USER = "/user"
    val mapper = jacksonObjectMapper().apply { setSerializationInclusion(JsonInclude.Include.NON_NULL) }

    route(WIDGET_END_POINT_USER){
        get("/"){ call.respond(userService.all()) }

        get("/{id}"){
            val user = userService.getUser(call.parameters["id"]?.toString()!!)
            if (user == null){
                call.respond(false)
            }else{
                call.respond(user)
            }
        }

        post("/signin") {
            val user: User? = userService.signIn(
                call.parameters["email"].toString(),
                call.parameters["password"].toString())
            if (user != null) {
                call.respond(user)
            }
            else {
                call.respond(false)
            }
        }

        post("/") {
            val user: User = call.receive<User>()
            val created: User? = userService.new(user)
            if (created == null) {
                call.respond(false)
            }
            else {
                call.respond(HttpStatusCode.Created, created)
            }
        }

        put("/") {
            val u = call.receive<User>()
            val updated = userService.update(u)
            if(updated == null){
                call.respond(false)
            } else {
                call.respond(HttpStatusCode.OK, updated)
            }
        }

        delete("/{id}") {
            val removed: Boolean = userService.delete(call.parameters["id"]?.toString()!!)
            if (removed) {
                call.respond(HttpStatusCode.OK)
            }
            else {
                call.respond(false)
            }
        }
    }
}