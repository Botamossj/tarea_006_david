package com.pucetec.students.controllers

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.services.SubjectService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SubjectController (
    val subjectService: SubjectService
){
    @PostMapping("/api/subject")
    fun createSubject(
        @RequestBody request: SubjectRequest
    ): SubjectResponse {
        return subjectService.createSubject(request)
    }

    @GetMapping("/api/subject")
    fun getAllSubjects(): List<SubjectResponse> {
        return subjectService.getAllSubjects()
    }

    @GetMapping("/api/subject/{id}")
    fun getSubjectById(
        @PathVariable id: Long
    ): SubjectResponse {
        return subjectService.getSubjectById(id)
    }

    @PutMapping("/api/subject/{id}")
    fun updateSubject(
        @PathVariable id: Long,
        @RequestBody request: SubjectRequest
    ): SubjectResponse {
        return subjectService.updateSubject(id, request)
    }

    @DeleteMapping("/api/subject/{id}")
    fun deleteSubject(
        @PathVariable id: Long
    ) {
        subjectService.deleteSubject(id)
    }
}