package com.pucetec.students.mappers

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.entities.Student
import com.pucetec.students.entities.Subject

fun EnrollmentRequest.toEntity(student: Student, subject: Subject)= Enrollment (

    status = "ACTIVE",
    student = student,
    subject = subject,
)

fun Enrollment.toResponse() = EnrollmentResponse(
    id = this.id,
    createdAt= this.createdAt.toString(),
    status = this.status,
    student= this.student.toResponse(),
    subject= this.subject.toResponse()
)