package com.randy.EOToolsManagement.service

import com.randy.EOToolsManagement.dto.EquipmentRequest
import com.randy.EOToolsManagement.dto.EquipmentResponse
import com.randy.EOToolsManagement.dto.SimpleCrewResponse
import com.randy.EOToolsManagement.entity.Equipment
import com.randy.EOToolsManagement.exception.ResourceNotFoundException
import com.randy.EOToolsManagement.repository.EquipmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EquipmentService(
    private val equipmentRepository: EquipmentRepository
) {

    @Transactional(readOnly = true)
    fun getAllEquipment(category: String?, search: String?): List<EquipmentResponse> {
        val equipmentList = when {
            !category.isNullOrBlank() && !search.isNullOrBlank() ->
                equipmentRepository.findByCategoryContainingIgnoreCaseAndNameContainingIgnoreCase(category, search)
            !category.isNullOrBlank() ->
                equipmentRepository.findByCategoryContainingIgnoreCase(category)
            !search.isNullOrBlank() ->
                equipmentRepository.findByNameContainingIgnoreCase(search)
            else ->
                equipmentRepository.findAll()
        }
        return equipmentList.map { toEquipmentResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getEquipmentById(id: Long): EquipmentResponse {
        val equipment = equipmentRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Equipment dengan ID $id tidak ditemukan") }
        return toEquipmentResponse(equipment)
    }

    @Transactional
    fun createEquipment(request: EquipmentRequest): EquipmentResponse {
        val equipment = Equipment(
            name = request.name,
            category = request.category,
            description = request.description,
            totalQuantity = request.totalQuantity,
            conditionStatus = request.conditionStatus ?: "Bagus"
        )
        val saved = equipmentRepository.save(equipment)
        return toEquipmentResponse(saved)
    }

    @Transactional
    fun updateEquipment(id: Long, request: EquipmentRequest): EquipmentResponse {
        val equipment = equipmentRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Equipment dengan ID $id tidak ditemukan") }

        equipment.name = request.name
        equipment.category = request.category
        equipment.description = request.description
        equipment.totalQuantity = request.totalQuantity
        equipment.conditionStatus = request.conditionStatus ?: "Bagus"

        val updated = equipmentRepository.save(equipment)
        return toEquipmentResponse(updated)
    }

    @Transactional
    fun deleteEquipment(id: Long) {
        if (!equipmentRepository.existsById(id)) {
            throw ResourceNotFoundException("Equipment dengan ID $id tidak ditemukan")
        }
        equipmentRepository.deleteById(id)
    }

    fun toEquipmentResponse(equipment: Equipment): EquipmentResponse {
        val borrowedQty = equipment.id?.let { equipmentRepository.countBorrowedQuantityInActiveSchedules(it) } ?: 0
        val availableQty = (equipment.totalQuantity - borrowedQty).coerceAtLeast(0)

        val assignedCrews = equipment.crews.map { crew ->
            SimpleCrewResponse(
                id = crew.id ?: 0,
                name = crew.name,
                phone = crew.phone,
                role = crew.role
            )
        }

        return EquipmentResponse(
            id = equipment.id ?: 0,
            name = equipment.name,
            category = equipment.category,
            description = equipment.description,
            totalQuantity = equipment.totalQuantity,
            availableQuantity = availableQty,
            conditionStatus = equipment.conditionStatus,
            assignedCrews = assignedCrews
        )
    }
}
