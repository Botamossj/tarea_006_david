package com.pucetec.students.services

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.entities.Professor
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.repositories.ProfessorRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class ProfessorServiceTest {

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var professorService: ProfessorService

    @Test
    fun `createProfessor lanza BlankNameException cuando el nombre esta vacio`() {
        val request = ProfessorRequest(name = "", email = "prof@puce.edu")

        assertThrows(BlankNameException::class.java) {
            professorService.createProfessor(request)
        }
    }

    @Test
    fun `createProfessor retorna respuesta valida cuando el nombre es valido`() {
        val request = ProfessorRequest(name = "Dr. Perez", email = "perez@puce.edu")
        val savedProfessor = Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu")

        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(savedProfessor)

        val response = professorService.createProfessor(request)

        assertEquals(1L, response.id)
        assertEquals("Dr. Perez", response.name)
        assertEquals("perez@puce.edu", response.email)
    }

    @Test
    fun `createProfessor retorna respuesta valida cuando el email es null`() {
        val request = ProfessorRequest(name = "Dr. Perez", email = null)
        val savedProfessor = Professor(id = 1L, name = "Dr. Perez", email = null)

        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(savedProfessor)

        val response = professorService.createProfessor(request)

        assertEquals(1L, response.id)
        assertEquals("Dr. Perez", response.name)
        assertEquals(null, response.email)
    }

    @Test
    fun `getAllProfessors retorna la lista de profesores`() {
        val professors = listOf(Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu"))

        `when`(professorRepository.findAll()).thenReturn(professors)

        val response = professorService.getAllProfessors()

        assertEquals(1, response.size)
        assertEquals("Dr. Perez", response[0].name)
    }

    @Test
    fun `getProfessorById retorna el profesor cuando existe`() {
        val professor = Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu")

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))

        val response = professorService.getProfessorById(1L)

        assertEquals(1L, response.id)
        assertEquals("Dr. Perez", response.name)
    }

    @Test
    fun `getProfessorById lanza ProfessorNotFound cuando no existe`() {
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFound::class.java) {
            professorService.getProfessorById(99L)
        }
    }

    @Test
    fun `updateProfessor retorna respuesta actualizada cuando el nombre es valido`() {
        val request = ProfessorRequest(name = "Dr. Perez Actualizado", email = "nuevo@puce.edu")
        val existingProfessor = Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu")
        val updatedProfessor = Professor(id = 1L, name = "Dr. Perez Actualizado", email = "nuevo@puce.edu")

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(existingProfessor))
        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(updatedProfessor)

        val response = professorService.updateProfessor(1L, request)

        assertEquals("Dr. Perez Actualizado", response.name)
        assertEquals("nuevo@puce.edu", response.email)
    }

    @Test
    fun `updateProfessor lanza ProfessorNotFound cuando el profesor no existe`() {
        val request = ProfessorRequest(name = "Dr. Perez", email = "perez@puce.edu")

        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFound::class.java) {
            professorService.updateProfessor(99L, request)
        }
    }

    @Test
    fun `updateProfessor lanza BlankNameException cuando el nombre esta vacio`() {
        val request = ProfessorRequest(name = "", email = "perez@puce.edu")
        val existingProfessor = Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu")

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(existingProfessor))

        assertThrows(BlankNameException::class.java) {
            professorService.updateProfessor(1L, request)
        }
    }

    @Test
    fun `deleteProfessor elimina el profesor cuando existe`() {
        val existingProfessor = Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu")

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(existingProfessor))

        professorService.deleteProfessor(1L)

        verify(professorRepository).delete(existingProfessor)
    }

    @Test
    fun `deleteProfessor lanza ProfessorNotFound cuando no existe`() {
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFound::class.java) {
            professorService.deleteProfessor(99L)
        }
    }
}
