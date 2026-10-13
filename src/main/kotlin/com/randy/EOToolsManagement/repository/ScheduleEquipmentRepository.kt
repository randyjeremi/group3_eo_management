package com.randy.EOToolsManagement.repository

import com.randy.EOToolsManagement.entity.ScheduleEquipment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScheduleEquipmentRepository : JpaRepository<ScheduleEquipment, Long> {
    fun findByScheduleId(scheduleId: Long): List<ScheduleEquipment>
    fun deleteByScheduleId(scheduleId: Long)
}
