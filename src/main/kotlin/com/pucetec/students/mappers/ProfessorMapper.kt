package com.pucetec.students.mappers

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.entities.Professor

fun ProfessorRequest.toEntity()= Professor (
    name= this.name,
    email = this.email,
)

fun Professor.toResponse() = ProfessorResponse(
    id= this.id,
    name= this.name,
    email = this.email
)