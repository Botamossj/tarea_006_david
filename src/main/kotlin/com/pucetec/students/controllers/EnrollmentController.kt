package com.pucetec.students.controllers

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.services.EnrollmentService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EnrollmentController (
    private val enrollmentService: EnrollmentService
){
    val logger = LoggerFactory.getLogger(EnrollmentController::class.java)

    @PostMapping("/api/enrollments")
    fun createEnrollment(
        @RequestBody request: EnrollmentRequest
    ): EnrollmentResponse {
        logger.info("Creando inscripción para estudiante ${request.studentId}")
        return enrollmentService.createEnrollment(request)
    }

    @GetMapping("/api/enrollments")
    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Consultando todas las inscripciones")
        return enrollmentService.getAllEnrollments()
    }

    @GetMapping("/api/enrollments/{id}")
    fun getEnrollmentById(
        @PathVariable id: Long
    ): EnrollmentResponse {
        logger.info("Buscando inscripción con id $id")
        return enrollmentService.getEnrollmentById(id)
    }

    @PutMapping("/api/enrollments/{id}")
    fun updateEnrollment(
        @PathVariable id: Long,
        @RequestBody request: EnrollmentRequest
    ): EnrollmentResponse {
        logger.info("Actualizando inscripción con id $id")
        return enrollmentService.updateEnrollment(id, request)
    }

    @DeleteMapping("/api/enrollments/{id}")
    fun deleteEnrollment(
        @PathVariable id: Long
    ) {
        logger.info("Eliminando inscripción con id $id")
        enrollmentService.deleteEnrollment(id)
    }
}
