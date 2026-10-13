package com.randy.EOToolsManagement.entity

import jakarta.persistence.*

@Entity
@Table(name = "crews")
class Crew(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var phone: String,

    @Column(nullable = false)
    var role: String,

    @ManyToMany
    @JoinTable(
        name = "crew_equipments",
        joinColumns = [JoinColumn(name = "crew_id")],
        inverseJoinColumns = [JoinColumn(name = "equipment_id")]
    )
    var equipments: MutableSet<Equipment> = mutableSetOf(),

    @ManyToMany(mappedBy = "crews")
    var schedules: MutableSet<Schedule> = mutableSetOf()
)
