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

const val WIDGET_END_POINT = "/user"
val mapper = jacksonObjectMapper().apply { setSerializationInclusion(JsonInclude.Include.NON_NULL) }

fun Route.widget(userService: UserService){

    route(WIDGET_END_POINT){
        get("/"){ call.respond(userService.all()) }

        get("/{id}"){
            val widget = userService.getUser(call.parameters["id"]?.toInt()!!)
            if (widget == null){
                call.respond(HttpStatusCode.NotFound)
            }else{
                call.respond(widget)
            }
        }

        post("/") {
            val u = call.receive<User>()
            call.respond(HttpStatusCode.Created, userService.new(u))
        }

        put("/") {
            val u = call.receive<User>()
            val updated = userService.update(u)
            if(updated == null){
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, updated)
            }
        }

        delete("/{id}") {
            val removed: Boolean = userService.delete(call.parameters["id"]?.toInt()!!)
            if (removed) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}