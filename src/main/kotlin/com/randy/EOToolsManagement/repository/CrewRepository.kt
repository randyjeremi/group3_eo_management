package com.randy.EOToolsManagement.repository

import com.randy.EOToolsManagement.entity.Crew
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CrewRepository : JpaRepository<Crew, Long> {
    fun findByRoleContainingIgnoreCase(role: String): List<Crew>
    fun findByNameContainingIgnoreCase(name: String): List<Crew>
    fun findByEquipmentsId(equipmentId: Long): List<Crew>
}
