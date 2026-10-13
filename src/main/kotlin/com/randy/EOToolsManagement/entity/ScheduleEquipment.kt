package com.randy.EOToolsManagement.entity

import jakarta.persistence.*

@Entity
@Table(name = "schedule_equipments")
class ScheduleEquipment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    var schedule: Schedule? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    var equipment: Equipment,

    @Column(name = "quantity_borrowed", nullable = false)
    var quantityBorrowed: Int = 1
)
