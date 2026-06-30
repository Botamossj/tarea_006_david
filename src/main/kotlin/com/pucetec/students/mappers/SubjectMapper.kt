package com.pucetec.students.mappers

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.entities.Professor
import com.pucetec.students.entities.Subject

fun SubjectRequest.toEntity(professor: Professor) = Subject(

    name= this.name,
    code = this.code,
    professor = professor
)

fun Subject.toResponse() = SubjectResponse(

    id= this.id,
    name= this.name,
    code= this.code,
    professor = this.professor.toResponse()
)