package com.pucetec.students.repositories

import com.pucetec.students.entities.Enrollment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EnrollmentRepository: JpaRepository<Enrollment, Long>