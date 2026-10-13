package com.randy.EOToolsManagement.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "schedules")
class Schedule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "event_name", nullable = false)
    var eventName: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(nullable = false)
    var location: String,

    @Column(name = "start_date", nullable = false)
    var startDate: LocalDate,

    @Column(name = "end_date", nullable = false)
    var endDate: LocalDate,

    @Column(name = "start_time")
    var startTime: LocalTime? = null,

    @Column(name = "end_time")
    var endTime: LocalTime? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ScheduleStatus = ScheduleStatus.UPCOMING,

    @OneToMany(mappedBy = "schedule", cascade = [CascadeType.ALL], orphanRemoval = true)
    var scheduleEquipments: MutableList<ScheduleEquipment> = mutableListOf(),

    @ManyToMany
    @JoinTable(
        name = "schedule_crews",
        joinColumns = [JoinColumn(name = "schedule_id")],
        inverseJoinColumns = [JoinColumn(name = "crew_id")]
    )
    var crews: MutableSet<Crew> = mutableSetOf()
)
