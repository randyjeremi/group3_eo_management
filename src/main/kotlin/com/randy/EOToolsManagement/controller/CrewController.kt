package com.randy.EOToolsManagement.controller

import com.randy.EOToolsManagement.dto.CrewRequest
import com.randy.EOToolsManagement.dto.CrewResponse
import com.randy.EOToolsManagement.service.CrewService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/crews")
class CrewController(
    private val crewService: CrewService
) {

    @GetMapping
    fun getAllCrews(): ResponseEntity<List<CrewResponse>> {
        val crews = crewService.getAllCrews()
        return ResponseEntity.ok(crews)
    }

    @GetMapping("/{id}")
    fun getCrewById(@PathVariable id: Long): ResponseEntity<CrewResponse> {
        val crew = crewService.getCrewById(id)
        return ResponseEntity.ok(crew)
    }

    @PostMapping
    fun createCrew(@Valid @RequestBody request: CrewRequest): ResponseEntity<CrewResponse> {
        val created = crewService.createCrew(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun updateCrew(
        @PathVariable id: Long,
        @Valid @RequestBody request: CrewRequest
    ): ResponseEntity<CrewResponse> {
        val updated = crewService.updateCrew(id, request)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteCrew(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        crewService.deleteCrew(id)
        return ResponseEntity.ok(mapOf("message" to "Kru dengan ID $id berhasil dihapus"))
    }

    @PostMapping("/{crewId}/equipments/{equipmentId}")
    fun assignEquipment(
        @PathVariable crewId: Long,
        @PathVariable equipmentId: Long
    ): ResponseEntity<CrewResponse> {
        val updated = crewService.assignEquipmentToCrew(crewId, equipmentId)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{crewId}/equipments/{equipmentId}")
    fun removeEquipment(
        @PathVariable crewId: Long,
        @PathVariable equipmentId: Long
    ): ResponseEntity<CrewResponse> {
        val updated = crewService.removeEquipmentFromCrew(crewId, equipmentId)
        return ResponseEntity.ok(updated)
    }

    @GetMapping("/by-equipment/{equipmentId}")
    fun getCrewsByEquipment(@PathVariable equipmentId: Long): ResponseEntity<List<CrewResponse>> {
        val crews = crewService.getCrewsByEquipment(equipmentId)
        return ResponseEntity.ok(crews)
    }
}
