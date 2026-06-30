package com.pucetec.students.services

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.entities.Professor
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.repositories.ProfessorRepository
import com.pucetec.students.repositories.SubjectRepository
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
class SubjectServiceTest {

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var subjectService: SubjectService

    private val professor = Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu")

    @Test
    fun `createSubject retorna la materia cuando el profesor existe`() {
        val request = SubjectRequest(name = "Arquitectura", code = "AE101", professorId = 1L)
        val savedSubject = Subject(id = 1L, name = "Arquitectura", code = "AE101", professor = professor)

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any(Subject::class.java))).thenReturn(savedSubject)

        val response = subjectService.createSubject(request)

        assertEquals(1L, response.id)
        assertEquals("Arquitectura", response.name)
        assertEquals("AE101", response.code)
    }

    @Test
    fun `createSubject lanza ProfessorNotFound cuando el profesor no existe`() {
        val request = SubjectRequest(name = "Arquitectura", code = "AE101", professorId = 99L)

        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFound::class.java) {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `getAllSubjects retorna la lista de materias`() {
        val subjects = listOf(Subject(id = 1L, name = "Arquitectura", code = "AE101", professor = professor))

        `when`(subjectRepository.findAll()).thenReturn(subjects)

        val response = subjectService.getAllSubjects()

        assertEquals(1, response.size)
        assertEquals("Arquitectura", response[0].name)
    }

    @Test
    fun `getSubjectById retorna la materia cuando existe`() {
        val subject = Subject(id = 1L, name = "Arquitectura", code = "AE101", professor = professor)

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        val response = subjectService.getSubjectById(1L)

        assertEquals(1L, response.id)
        assertEquals("Arquitectura", response.name)
    }

    @Test
    fun `getSubjectById lanza SubjectNotFound cuando no existe`() {
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(SubjectNotFound::class.java) {
            subjectService.getSubjectById(99L)
        }
    }

    @Test
    fun `updateSubject retorna la materia actualizada cuando existe`() {
        val request = SubjectRequest(name = "Arquitectura Avanzada", code = "AE102", professorId = 1L)
        val existingSubject = Subject(id = 1L, name = "Arquitectura", code = "AE101", professor = professor)
        val updatedSubject = Subject(id = 1L, name = "Arquitectura Avanzada", code = "AE102", professor = professor)

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any(Subject::class.java))).thenReturn(updatedSubject)

        val response = subjectService.updateSubject(1L, request)

        assertEquals("Arquitectura Avanzada", response.name)
        assertEquals("AE102", response.code)
    }

    @Test
    fun `updateSubject lanza SubjectNotFound cuando la materia no existe`() {
        val request = SubjectRequest(name = "Arquitectura", code = "AE101", professorId = 1L)

        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(SubjectNotFound::class.java) {
            subjectService.updateSubject(99L, request)
        }
    }

    @Test
    fun `updateSubject lanza ProfessorNotFound cuando el profesor no existe`() {
        val request = SubjectRequest(name = "Arquitectura", code = "AE101", professorId = 99L)
        val existingSubject = Subject(id = 1L, name = "Arquitectura", code = "AE101", professor = professor)

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFound::class.java) {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `deleteSubject elimina la materia cuando existe`() {
        val existingSubject = Subject(id = 1L, name = "Arquitectura", code = "AE101", professor = professor)

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))

        subjectService.deleteSubject(1L)

        verify(subjectRepository).delete(existingSubject)
    }

    @Test
    fun `deleteSubject lanza SubjectNotFound cuando no existe`() {
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(SubjectNotFound::class.java) {
            subjectService.deleteSubject(99L)
        }
    }
}
