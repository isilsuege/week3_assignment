package com.isilsu.week3_assignment

enum class DeviceType { LIGHT, CAMERA, THERMOSTAT, VACUUM }

data class SmartDevice(
    val id: Int,
    val name: String,
    val type: DeviceType,
    val roomName: String,
    val isOnline: Boolean,
    val isActive: Boolean,
    val powerUsage: Double
)