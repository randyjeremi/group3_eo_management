package com.randy.EOToolsManagement.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class EquipmentRequest(
    @field:NotBlank(message = "Nama alat tidak boleh kosong")
    val name: String,

    @field:NotBlank(message = "Kategori tidak boleh kosong")
    val category: String,

    val description: String? = null,

    @field:Min(value = 0, message = "Total jumlah tidak boleh negatif")
    val totalQuantity: Int,

    val conditionStatus: String? = "Bagus"
)

data class EquipmentResponse(
    val id: Long,
    val name: String,
    val category: String,
    val description: String?,
    val totalQuantity: Int,
    val availableQuantity: Int,
    val conditionStatus: String?,
    val assignedCrews: List<SimpleCrewResponse> = emptyList()
)

data class SimpleEquipmentResponse(
    val id: Long,
    val name: String,
    val category: String,
    val totalQuantity: Int,
    val availableQuantity: Int
)
