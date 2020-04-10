package com.elearn.services

import com.elearn.models.Course
import java.util.*
import kotlin.collections.ArrayList

class CourseService {

    private val courses = ArrayList<Course>()

    private fun getCoursePosition(id: String): Int? {
        for (i in 0 until courses.size)
            if (courses[i].id == id)
                return i
        return null
    }

    suspend fun all(): List<Course> = courses

    suspend fun getCourse(id: String): Course? {
        val position: Int? = getCoursePosition(id)
        return if(position != null) courses[position]
        else return null
    }

    suspend fun new(course: Course): Course {
        course.id = UUID.randomUUID().toString()
        courses.add(course)
        return course
    }

    suspend fun update(course: Course): Course? {
        val id = course.id
        if (id == null) {
            return new(course)
        } else {
            val position: Int = getCoursePosition(id) ?: return new(course)
            courses[position].courseName = course.courseName
            courses[position].body = course.body
            courses[position].description = course.description
            courses[position].logoUrl = course.logoUrl
            courses[position].price = course.price
            courses[position].tittle = course.tittle

            return courses[position]
        }
    }

    suspend fun delete(id: String): Boolean {
        val position: Int? = getCoursePosition(id)
        return if(position != null) {
            courses.removeAt(position)
            true
        } else false
    }
}