package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.entities.Professor
import com.pucetec.students.entities.Student
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentRepository
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
class EnrollmentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var enrollmentRepository: EnrollmentRepository

    @InjectMocks
    private lateinit var enrollmentService: EnrollmentService

    private val student = Student(id = 1L, name = "Santiago Vallejo", email = "vallejodavid1106@gmail.com")
    private val professor = Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu")
    private val subject = Subject(id = 1L, name = "Arquitectura", code = "AE101", professor = professor)
    private val enrollment = Enrollment(id = 1L, status = "ACTIVE", student = student, subject = subject)

    @Test
    fun `createEnrollment lanza StudentNotFoundException cuando el estudiante no existe`() {
        val request = EnrollmentRequest(studentId = 99L, subjectId = 1L)

        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment lanza StudentNotFoundException cuando la materia no existe`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 99L)

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment retorna la inscripcion cuando estudiante y materia existen`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L)

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(enrollment)

        val response = enrollmentService.createEnrollment(request)

        assertEquals(1L, response.id)
        assertEquals("ACTIVE", response.status)
        assertEquals("Santiago Vallejo", response.student.name)
    }

    @Test
    fun `getAllEnrollments retorna la lista de inscripciones`() {
        `when`(enrollmentRepository.findAll()).thenReturn(listOf(enrollment))

        val response = enrollmentService.getAllEnrollments()

        assertEquals(1, response.size)
        assertEquals("ACTIVE", response[0].status)
    }

    @Test
    fun `getEnrollmentById retorna la inscripcion cuando existe`() {
        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))

        val response = enrollmentService.getEnrollmentById(1L)

        assertEquals(1L, response.id)
        assertEquals("Santiago Vallejo", response.student.name)
    }

    @Test
    fun `getEnrollmentById lanza StudentNotFoundException cuando no existe`() {
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            enrollmentService.getEnrollmentById(99L)
        }
    }

    @Test
    fun `updateEnrollment lanza StudentNotFoundException cuando la inscripcion no existe`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L)

        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            enrollmentService.updateEnrollment(99L, request)
        }
    }

    @Test
    fun `updateEnrollment lanza StudentNotFoundException cuando el estudiante no existe`() {
        val request = EnrollmentRequest(studentId = 99L, subjectId = 1L)

        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            enrollmentService.updateEnrollment(1L, request)
        }
    }

    @Test
    fun `updateEnrollment lanza StudentNotFoundException cuando la materia no existe`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 99L)

        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            enrollmentService.updateEnrollment(1L, request)
        }
    }

    @Test
    fun `updateEnrollment retorna la inscripcion actualizada cuando todo existe`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L)
        val updatedEnrollment = Enrollment(id = 1L, status = "ACTIVE", student = student, subject = subject)

        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(updatedEnrollment)

        val response = enrollmentService.updateEnrollment(1L, request)

        assertEquals(1L, response.id)
        assertEquals("ACTIVE", response.status)
    }

    @Test
    fun `deleteEnrollment elimina la inscripcion cuando existe`() {
        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))

        enrollmentService.deleteEnrollment(1L)

        verify(enrollmentRepository).delete(enrollment)
    }

    @Test
    fun `deleteEnrollment lanza StudentNotFoundException cuando no existe`() {
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            enrollmentService.deleteEnrollment(99L)
        }
    }
}
