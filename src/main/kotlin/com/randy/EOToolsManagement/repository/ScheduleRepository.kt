package com.randy.EOToolsManagement.repository

import com.randy.EOToolsManagement.entity.Schedule
import com.randy.EOToolsManagement.entity.ScheduleStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ScheduleRepository : JpaRepository<Schedule, Long> {
    fun findByStatus(status: ScheduleStatus): List<Schedule>

    @Query("""
        SELECT s FROM Schedule s 
        WHERE (s.startDate <= :endDate AND s.endDate >= :startDate)
    """)
    fun findSchedulesInDateRange(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<Schedule>
}
