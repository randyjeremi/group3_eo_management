package com.randy.EOToolsManagement.controller

import com.randy.EOToolsManagement.dto.EquipmentRequest
import com.randy.EOToolsManagement.dto.EquipmentResponse
import com.randy.EOToolsManagement.service.EquipmentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/equipment")
class EquipmentController(
    private val equipmentService: EquipmentService
) {

    @GetMapping
    fun getAllEquipment(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) search: String?
    ): ResponseEntity<List<EquipmentResponse>> {
        val list = equipmentService.getAllEquipment(category, search)
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    fun getEquipmentById(@PathVariable id: Long): ResponseEntity<EquipmentResponse> {
        val equipment = equipmentService.getEquipmentById(id)
        return ResponseEntity.ok(equipment)
    }

    @PostMapping
    fun createEquipment(@Valid @RequestBody request: EquipmentRequest): ResponseEntity<EquipmentResponse> {
        val created = equipmentService.createEquipment(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun updateEquipment(
        @PathVariable id: Long,
        @Valid @RequestBody request: EquipmentRequest
    ): ResponseEntity<EquipmentResponse> {
        val updated = equipmentService.updateEquipment(id, request)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteEquipment(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        equipmentService.deleteEquipment(id)
        return ResponseEntity.ok(mapOf("message" to "Equipment dengan ID $id berhasil dihapus"))
    }
}
