package com.randy.EOToolsManagement.dto

import jakarta.validation.constraints.NotBlank

data class CrewRequest(
    @field:NotBlank(message = "Nama kru tidak boleh kosong")
    val name: String,

    @field:NotBlank(message = "Nomor telepon tidak boleh kosong")
    val phone: String,

    @field:NotBlank(message = "Role / Jabatan tidak boleh kosong")
    val role: String,

    val equipmentIds: List<Long>? = emptyList()
)

data class CrewResponse(
    val id: Long,
    val name: String,
    val phone: String,
    val role: String,
    val assignedEquipments: List<SimpleEquipmentResponse> = emptyList()
)

data class SimpleCrewResponse(
    val id: Long,
    val name: String,
    val phone: String,
    val role: String
)
