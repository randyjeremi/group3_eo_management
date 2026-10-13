package com.randy.EOToolsManagement.repository

import com.randy.EOToolsManagement.entity.Equipment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EquipmentRepository : JpaRepository<Equipment, Long> {
    fun findByCategoryContainingIgnoreCase(category: String): List<Equipment>
    fun findByNameContainingIgnoreCase(name: String): List<Equipment>
    fun findByCategoryContainingIgnoreCaseAndNameContainingIgnoreCase(category: String, name: String): List<Equipment>

    @Query("""
        SELECT COALESCE(SUM(se.quantityBorrowed), 0)
        FROM ScheduleEquipment se
        JOIN se.schedule s
        WHERE se.equipment.id = :equipmentId
        AND s.status IN ('UPCOMING', 'ONGOING')
    """)
    fun countBorrowedQuantityInActiveSchedules(@Param("equipmentId") equipmentId: Long): Int
}
