package com.isilsu.week3_assignment

data class HomeControlState(
    val deviceList: List<SmartDevice> = emptyList(),
    val currentPowerLoad: Double = 0.0,
    val searchText: String = "",
    val filteredRoom: String? = null,
    val roomOptions: List<String> = emptyList()
)