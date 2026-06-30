package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.entities.Student
import com.pucetec.students.exceptions.BlankNameException
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.repositories.StudentRepository
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
class StudentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentService: StudentService

    @Test
    fun `createStudent lanza BlankNameException cuando el nombre esta vacio`() {
        val request = StudentRequest(name = "", email = "vallejodavid1106@gmail.com")

        assertThrows(BlankNameException::class.java) {
            studentService.createStudent(request)
        }
    }

    @Test
    fun `createStudent retorna respuesta valida cuando el nombre es valido`() {
        val request = StudentRequest(
            name = "Santiago Vallejo",
            email = "vallejodavid1106@gmail.com"
        )
        val savedStudent = Student(
            id = 1L,
            name = "Santiago Vallejo",
            email = "vallejodavid1106@gmail.com"
        )

        `when`(studentRepository.save(any(Student::class.java))).thenReturn(savedStudent)

        val response = studentService.createStudent(request)

        assertEquals(1L, response.id)
        assertEquals("Santiago Vallejo", response.name)
        assertEquals("vallejodavid1106@gmail.com", response.email)
    }

    @Test
    fun `createStudent retorna respuesta valida cuando el email es null`() {
        val request = StudentRequest(name = "Santiago Vallejo", email = null)
        val savedStudent = Student(id = 1L, name = "Santiago Vallejo", email = null)

        `when`(studentRepository.save(any(Student::class.java))).thenReturn(savedStudent)

        val response = studentService.createStudent(request)

        assertEquals(1L, response.id)
        assertEquals("Santiago Vallejo", response.name)
        assertEquals(null, response.email)
    }

    @Test
    fun `getAllStudents retorna la lista de estudiantes`() {
        val students = listOf(
            Student(id = 1L, name = "Santiago Vallejo", email = "vallejodavid1106@gmail.com")
        )

        `when`(studentRepository.findAll()).thenReturn(students)

        val response = studentService.getAllStudents()

        assertEquals(1, response.size)
        assertEquals("Santiago Vallejo", response[0].name)
    }

    @Test
    fun `getStudentById retorna el estudiante cuando existe`() {
        val student = Student(id = 1L, name = "Santiago Vallejo", email = "vallejodavid1106@gmail.com")

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        val response = studentService.getStudentById(1L)

        assertEquals(1L, response.id)
        assertEquals("Santiago Vallejo", response.name)
    }

    @Test
    fun `getStudentById lanza StudentNotFoundException cuando no existe`() {
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            studentService.getStudentById(99L)
        }
    }

    @Test
    fun `updateStudent retorna respuesta actualizada cuando el nombre es valido`() {
        val request = StudentRequest(name = "Santiago Vallejo", email = "vallejodavid1106@gmail.com")
        val existingStudent = Student(id = 1L, name = "Anterior", email = "old@test.com")
        val updatedStudent = Student(id = 1L, name = "Santiago Vallejo", email = "vallejodavid1106@gmail.com")

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent))
        `when`(studentRepository.save(any(Student::class.java))).thenReturn(updatedStudent)

        val response = studentService.updateStudent(1L, request)

        assertEquals(1L, response.id)
        assertEquals("Santiago Vallejo", response.name)
    }

    @Test
    fun `updateStudent lanza StudentNotFoundException cuando el estudiante no existe`() {
        val request = StudentRequest(name = "Santiago Vallejo", email = "vallejodavid1106@gmail.com")

        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            studentService.updateStudent(99L, request)
        }
    }

    @Test
    fun `updateStudent lanza BlankNameException cuando el nombre esta vacio`() {
        val request = StudentRequest(name = "", email = "vallejodavid1106@gmail.com")
        val existingStudent = Student(id = 1L, name = "Santiago Vallejo", email = "vallejodavid1106@gmail.com")

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent))

        assertThrows(BlankNameException::class.java) {
            studentService.updateStudent(1L, request)
        }
    }

    @Test
    fun `deleteStudent elimina el estudiante cuando existe`() {
        val existingStudent = Student(id = 1L, name = "Santiago Vallejo", email = "vallejodavid1106@gmail.com")

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent))

        studentService.deleteStudent(1L)

        verify(studentRepository).delete(existingStudent)
    }

    @Test
    fun `deleteStudent lanza StudentNotFoundException cuando no existe`() {
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            studentService.deleteStudent(99L)
        }
    }
}
