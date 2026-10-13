package com.randy.EOToolsManagement.controller

import com.randy.EOToolsManagement.dto.ScheduleRequest
import com.randy.EOToolsManagement.dto.ScheduleResponse
import com.randy.EOToolsManagement.entity.ScheduleStatus
import com.randy.EOToolsManagement.service.ScheduleService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/schedules")
class ScheduleController(
    private val scheduleService: ScheduleService
) {

    @GetMapping
    fun getAllSchedules(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?,
        @RequestParam(required = false) status: ScheduleStatus?
    ): ResponseEntity<List<ScheduleResponse>> {
        val schedules = scheduleService.getAllSchedules(startDate, endDate, status)
        return ResponseEntity.ok(schedules)
    }

    @GetMapping("/{id}")
    fun getScheduleById(@PathVariable id: Long): ResponseEntity<ScheduleResponse> {
        val schedule = scheduleService.getScheduleById(id)
        return ResponseEntity.ok(schedule)
    }

    @PostMapping
    fun createSchedule(@Valid @RequestBody request: ScheduleRequest): ResponseEntity<ScheduleResponse> {
        val created = scheduleService.createSchedule(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun updateSchedule(
        @PathVariable id: Long,
        @Valid @RequestBody request: ScheduleRequest
    ): ResponseEntity<ScheduleResponse> {
        val updated = scheduleService.updateSchedule(id, request)
        return ResponseEntity.ok(updated)
    }

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: Long,
        @RequestParam status: ScheduleStatus
    ): ResponseEntity<ScheduleResponse> {
        val updated = scheduleService.updateStatus(id, status)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteSchedule(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        scheduleService.deleteSchedule(id)
        return ResponseEntity.ok(mapOf("message" to "Jadwal dengan ID $id berhasil dihapus"))
    }
}
