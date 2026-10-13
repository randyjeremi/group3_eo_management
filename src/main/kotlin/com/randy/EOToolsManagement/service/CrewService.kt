package com.randy.EOToolsManagement.service

import com.randy.EOToolsManagement.dto.CrewRequest
import com.randy.EOToolsManagement.dto.CrewResponse
import com.randy.EOToolsManagement.dto.SimpleEquipmentResponse
import com.randy.EOToolsManagement.entity.Crew
import com.randy.EOToolsManagement.exception.ResourceNotFoundException
import com.randy.EOToolsManagement.repository.CrewRepository
import com.randy.EOToolsManagement.repository.EquipmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CrewService(
    private val crewRepository: CrewRepository,
    private val equipmentRepository: EquipmentRepository
) {

    @Transactional(readOnly = true)
    fun getAllCrews(): List<CrewResponse> {
        return crewRepository.findAll().map { toCrewResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getCrewById(id: Long): CrewResponse {
        val crew = crewRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Kru dengan ID $id tidak ditemukan") }
        return toCrewResponse(crew)
    }

    @Transactional
    fun createCrew(request: CrewRequest): CrewResponse {
        val crew = Crew(
            name = request.name,
            phone = request.phone,
            role = request.role
        )

        if (!request.equipmentIds.isNullOrEmpty()) {
            val equipments = equipmentRepository.findAllById(request.equipmentIds)
            crew.equipments.addAll(equipments)
        }

        val saved = crewRepository.save(crew)
        return toCrewResponse(saved)
    }

    @Transactional
    fun updateCrew(id: Long, request: CrewRequest): CrewResponse {
        val crew = crewRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Kru dengan ID $id tidak ditemukan") }

        crew.name = request.name
        crew.phone = request.phone
        crew.role = request.role

        if (request.equipmentIds != null) {
            crew.equipments.clear()
            val equipments = equipmentRepository.findAllById(request.equipmentIds)
            crew.equipments.addAll(equipments)
        }

        val updated = crewRepository.save(crew)
        return toCrewResponse(updated)
    }

    @Transactional
    fun deleteCrew(id: Long) {
        if (!crewRepository.existsById(id)) {
            throw ResourceNotFoundException("Kru dengan ID $id tidak ditemukan")
        }
        crewRepository.deleteById(id)
    }

    @Transactional
    fun assignEquipmentToCrew(crewId: Long, equipmentId: Long): CrewResponse {
        val crew = crewRepository.findById(crewId)
            .orElseThrow { ResourceNotFoundException("Kru dengan ID $crewId tidak ditemukan") }
        val equipment = equipmentRepository.findById(equipmentId)
            .orElseThrow { ResourceNotFoundException("Equipment dengan ID $equipmentId tidak ditemukan") }

        crew.equipments.add(equipment)
        val saved = crewRepository.save(crew)
        return toCrewResponse(saved)
    }

    @Transactional
    fun removeEquipmentFromCrew(crewId: Long, equipmentId: Long): CrewResponse {
        val crew = crewRepository.findById(crewId)
            .orElseThrow { ResourceNotFoundException("Kru dengan ID $crewId tidak ditemukan") }

        crew.equipments.removeIf { it.id == equipmentId }
        val saved = crewRepository.save(crew)
        return toCrewResponse(saved)
    }

    @Transactional(readOnly = true)
    fun getCrewsByEquipment(equipmentId: Long): List<CrewResponse> {
        return crewRepository.findByEquipmentsId(equipmentId).map { toCrewResponse(it) }
    }

    fun toCrewResponse(crew: Crew): CrewResponse {
        val assignedEquipments = crew.equipments.map { eq ->
            val borrowed = eq.id?.let { equipmentRepository.countBorrowedQuantityInActiveSchedules(it) } ?: 0
            SimpleEquipmentResponse(
                id = eq.id ?: 0,
                name = eq.name,
                category = eq.category,
                totalQuantity = eq.totalQuantity,
                availableQuantity = (eq.totalQuantity - borrowed).coerceAtLeast(0)
            )
        }

        return CrewResponse(
            id = crew.id ?: 0,
            name = crew.name,
            phone = crew.phone,
            role = crew.role,
            assignedEquipments = assignedEquipments
        )
    }
}
