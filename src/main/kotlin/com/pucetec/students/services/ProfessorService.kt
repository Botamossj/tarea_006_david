package com.pucetec.students.services

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.entities.Professor
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.ProfessorRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProfessorService (
    private val professorRepository: ProfessorRepository
) {
    val logger = LoggerFactory.getLogger(javaClass)

    fun createProfessor(request: ProfessorRequest): ProfessorResponse {
        logger.info("Registrando profesor: ${request.name}")

        if (request.name.isBlank()) {
            throw BlankNameException("Name cannot be blank")
        }

        val professorEntity = request.toEntity()
        val savedProfessor = professorRepository.save(professorEntity)
        return savedProfessor.toResponse()
    }

    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Obteniendo todos los profesores registrados")
        return professorRepository.findAll().map { it.toResponse() }
    }

    fun getProfessorById(id: Long): ProfessorResponse {
        logger.info("Consultando profesor por id: $id")
        val professor = professorRepository.findById(id).orElseThrow {
            ProfessorNotFound("Professor with id $id not found")
        }
        return professor.toResponse()
    }

    fun updateProfessor(id: Long, request: ProfessorRequest): ProfessorResponse {
        logger.info("Modificando profesor con id: $id")
        val existingProfessor = professorRepository.findById(id).orElseThrow {
            ProfessorNotFound("Professor with id $id not found")
        }

        if (request.name.isBlank()) {
            throw BlankNameException("Name cannot be blank")
        }

        val updatedProfessor = Professor(
            id = existingProfessor.id,
            name = request.name,
            email = request.email
        )

        return professorRepository.save(updatedProfessor).toResponse()
    }

    fun deleteProfessor(id: Long) {
        logger.info("Removiendo profesor con id: $id")
        val existingProfessor = professorRepository.findById(id).orElseThrow {
            ProfessorNotFound("Professor with id $id not found")
        }
        professorRepository.delete(existingProfessor)
    }
}
