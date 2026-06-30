package com.pucetec.students.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "professors")
class Professor (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long =0L,

    val name : String = "",

    val email: String? = null,

    @OneToMany(mappedBy = "professor")
    val subject: MutableList<Subject> = mutableListOf(),
)