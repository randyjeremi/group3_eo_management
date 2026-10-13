package com.randy.EOToolsManagement.service

import com.randy.EOToolsManagement.dto.*
import com.randy.EOToolsManagement.entity.Schedule
import com.randy.EOToolsManagement.entity.ScheduleEquipment
import com.randy.EOToolsManagement.entity.ScheduleStatus
import com.randy.EOToolsManagement.exception.ResourceNotFoundException
import com.randy.EOToolsManagement.repository.CrewRepository
import com.randy.EOToolsManagement.repository.EquipmentRepository
import com.randy.EOToolsManagement.repository.ScheduleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
    private val equipmentRepository: EquipmentRepository,
    private val crewRepository: CrewRepository
) {

    @Transactional(readOnly = true)
    fun getAllSchedules(startDate: LocalDate?, endDate: LocalDate?, status: ScheduleStatus?): List<ScheduleResponse> {
        val schedules = when {
            startDate != null && endDate != null -> scheduleRepository.findSchedulesInDateRange(startDate, endDate)
            status != null -> scheduleRepository.findByStatus(status)
            else -> scheduleRepository.findAll()
        }
        return schedules.map { toScheduleResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getScheduleById(id: Long): ScheduleResponse {
        val schedule = scheduleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Jadwal dengan ID $id tidak ditemukan") }
        return toScheduleResponse(schedule)
    }

    @Transactional
    fun createSchedule(request: ScheduleRequest): ScheduleResponse {
        val schedule = Schedule(
            eventName = request.eventName,
            description = request.description,
            location = request.location,
            startDate = request.startDate,
            endDate = request.endDate,
            startTime = request.startTime,
            endTime = request.endTime,
            status = request.status ?: ScheduleStatus.UPCOMING
        )

        if (request.equipmentBorrowings.isNotEmpty()) {
            for (borrowReq in request.equipmentBorrowings) {
                val equipment = equipmentRepository.findById(borrowReq.equipmentId)
                    .orElseThrow { ResourceNotFoundException("Equipment dengan ID ${borrowReq.equipmentId} tidak ditemukan") }

                val scheduleEquipment = ScheduleEquipment(
                    schedule = schedule,
                    equipment = equipment,
                    quantityBorrowed = borrowReq.quantityBorrowed
                )
                schedule.scheduleEquipments.add(scheduleEquipment)
            }
        }

        if (request.crewIds.isNotEmpty()) {
            val crews = crewRepository.findAllById(request.crewIds)
            schedule.crews.addAll(crews)
        }

        val saved = scheduleRepository.save(schedule)
        return toScheduleResponse(saved)
    }

    @Transactional
    fun updateSchedule(id: Long, request: ScheduleRequest): ScheduleResponse {
        val schedule = scheduleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Jadwal dengan ID $id tidak ditemukan") }

        schedule.eventName = request.eventName
        schedule.description = request.description
        schedule.location = request.location
        schedule.startDate = request.startDate
        schedule.endDate = request.endDate
        schedule.startTime = request.startTime
        schedule.endTime = request.endTime

        if (request.status != null) {
            schedule.status = request.status
        }

        schedule.scheduleEquipments.clear()
        if (request.equipmentBorrowings.isNotEmpty()) {
            for (borrowReq in request.equipmentBorrowings) {
                val equipment = equipmentRepository.findById(borrowReq.equipmentId)
                    .orElseThrow { ResourceNotFoundException("Equipment dengan ID ${borrowReq.equipmentId} tidak ditemukan") }

                val scheduleEquipment = ScheduleEquipment(
                    schedule = schedule,
                    equipment = equipment,
                    quantityBorrowed = borrowReq.quantityBorrowed
                )
                schedule.scheduleEquipments.add(scheduleEquipment)
            }
        }

        schedule.crews.clear()
        if (request.crewIds.isNotEmpty()) {
            val crews = crewRepository.findAllById(request.crewIds)
            schedule.crews.addAll(crews)
        }

        val updated = scheduleRepository.save(schedule)
        return toScheduleResponse(updated)
    }

    @Transactional
    fun updateStatus(id: Long, status: ScheduleStatus): ScheduleResponse {
        val schedule = scheduleRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Jadwal dengan ID $id tidak ditemukan") }

        schedule.status = status
        val updated = scheduleRepository.save(schedule)
        return toScheduleResponse(updated)
    }

    @Transactional
    fun deleteSchedule(id: Long) {
        if (!scheduleRepository.existsById(id)) {
            throw ResourceNotFoundException("Jadwal dengan ID $id tidak ditemukan")
        }
        scheduleRepository.deleteById(id)
    }

    fun toScheduleResponse(schedule: Schedule): ScheduleResponse {
        val equipmentResponses = schedule.scheduleEquipments.map { se ->
            ScheduleEquipmentResponse(
                equipmentId = se.equipment.id ?: 0,
                equipmentName = se.equipment.name,
                category = se.equipment.category,
                quantityBorrowed = se.quantityBorrowed
            )
        }

        val crewResponses = schedule.crews.map { c ->
            SimpleCrewResponse(
                id = c.id ?: 0,
                name = c.name,
                phone = c.phone,
                role = c.role
            )
        }

        return ScheduleResponse(
            id = schedule.id ?: 0,
            eventName = schedule.eventName,
            description = schedule.description,
            location = schedule.location,
            startDate = schedule.startDate,
            endDate = schedule.endDate,
            startTime = schedule.startTime,
            endTime = schedule.endTime,
            status = schedule.status,
            equipments = equipmentResponses,
            crews = crewResponses
        )
    }
}
