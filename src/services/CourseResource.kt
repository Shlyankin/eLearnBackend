package com.elearn.services

import com.elearn.models.Course
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



fun Route.widget(courseService: CourseService){
    val WIDGET_END_POINT_COURSE = "/course"
    val mapper = jacksonObjectMapper().apply { setSerializationInclusion(JsonInclude.Include.NON_NULL) }

    route(WIDGET_END_POINT_COURSE){
        get("/"){
            //get All courses
            call.respond(courseService.all())
        }

        get("/{id}"){
            //get course by id
            val course = courseService.getCourse(call.parameters["id"]?.toString()!!)
            if (course == null){
                call.respond(false)
            }else{
                call.respond(course)
            }
        }

        post("/") {
            val course = call.receive<Course>()
            call.respond(HttpStatusCode.Created, courseService.new(course))
        }

        put("/") {
            val course = call.receive<Course>()
            val updated = courseService.update(course)
            if(updated == null){
                call.respond(false)
            } else {
                call.respond(HttpStatusCode.OK, updated)
            }
        }

        delete("/{id}") {
            val removed: Boolean = courseService.delete(call.parameters["id"]?.toString()!!)
            if (removed) call.respond(HttpStatusCode.OK)
            else call.respond(false)
        }
    }
}