package com.elearn

import com.elearn.services.CourseService
import com.elearn.services.UserService
import com.elearn.services.widget
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation){
        jackson { configure(SerializationFeature.INDENT_OUTPUT,true)}
    }
    install(CORS) {
        anyHost()
        method(HttpMethod.Options)
        header(HttpHeaders.XForwardedProto)
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }
    install(Routing){
        widget(userService = UserService())
        widget(courseService = CourseService())
    }
}
