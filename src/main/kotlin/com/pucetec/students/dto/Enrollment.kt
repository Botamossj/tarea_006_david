package com.pucetec.students.dto

data class EnrollmentRequest (

    val studentId: Long,
    val subjectId: Long,

)

data class EnrollmentResponse (
    val id: Long,
    val createdAt: String,
    val status: String,
    val student: StudentResponse,
    val subject: SubjectResponse,
)