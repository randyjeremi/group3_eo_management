package com.randy.EOToolsManagement.entity

import jakarta.persistence.*

@Entity
@Table(name = "equipment")
class Equipment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var category: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(name = "total_quantity", nullable = false)
    var totalQuantity: Int = 0,

    @Column(name = "condition_status")
    var conditionStatus: String? = "Bagus",

    @OneToMany(mappedBy = "equipment", cascade = [CascadeType.ALL], orphanRemoval = true)
    var scheduleEquipments: MutableList<ScheduleEquipment> = mutableListOf(),

    @ManyToMany(mappedBy = "equipments")
    var crews: MutableSet<Crew> = mutableSetOf()
)
