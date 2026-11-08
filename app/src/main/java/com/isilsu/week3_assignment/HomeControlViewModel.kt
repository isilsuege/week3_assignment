package com.isilsu.week3_assignment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeControlViewModel : ViewModel() {

    private val initialData = listOf(
        SmartDevice(1, "Köşe Lambası", DeviceType.LIGHT, "Salon", isOnline = true, isActive = true, powerUsage = 15.0),
        SmartDevice(2, "Tavan Avizesi", DeviceType.LIGHT, "Salon", isOnline = true, isActive = false, powerUsage = 60.0),
        SmartDevice(3, "Bahçe Kamerası", DeviceType.CAMERA, "Bahçe", isOnline = true, isActive = true, powerUsage = 8.5),
        SmartDevice(4, "Giriş Kamerası", DeviceType.CAMERA, "Antre", isOnline = true, isActive = true, powerUsage = 8.5),
        SmartDevice(5, "Merkezi Termostat", DeviceType.THERMOSTAT, "Salon", isOnline = true, isActive = true, powerUsage = 12.0),
        SmartDevice(6, "Yatak Odası Kliması", DeviceType.THERMOSTAT, "Yatak Odası", isOnline = true, isActive = false, powerUsage = 1200.0),
        SmartDevice(7, "Mutfak Spotları", DeviceType.LIGHT, "Mutfak", isOnline = true, isActive = false, powerUsage = 45.0),
        SmartDevice(8, "Roborock S7", DeviceType.VACUUM, "Koridor", isOnline = true, isActive = true, powerUsage = 50.0),
        SmartDevice(9, "Balkon Aydınlatması", DeviceType.LIGHT, "Balkon", isOnline = false, isActive = false, powerUsage = 25.0),
        SmartDevice(10, "Çalışma Odası Lambası", DeviceType.LIGHT, "Ofis", isOnline = true, isActive = true, powerUsage = 12.0)
    )

    private val _devices = MutableStateFlow(initialData)
    private val _searchText = MutableStateFlow("")
    private val _filteredRoom = MutableStateFlow<String?>(null)

    val uiState: StateFlow<HomeControlState> = combine(
        _devices, _searchText, _filteredRoom
    ) { devices, query, room ->

        val filteredList = devices.filter { device ->
            val roomMatch = room == null || device.roomName == room
            val queryMatch = query.isBlank() || device.name.contains(query, ignoreCase = true)
            roomMatch && queryMatch
        }

        val totalLoad = devices
            .filter { it.isOnline && it.isActive }
            .sumOf { it.powerUsage }

        val rooms = devices.map { it.roomName }.distinct().sorted()

        HomeControlState(
            deviceList = filteredList,
            currentPowerLoad = totalLoad,
            searchText = query,
            filteredRoom = room,
            roomOptions = rooms
        )
    }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeControlState()
        )

    fun updateSearchText(newText: String) {
        _searchText.value = newText
    }

    fun selectRoom(room: String?) {
        _filteredRoom.value = if (_filteredRoom.value == room) null else room
    }

    fun toggleDevicePower(deviceId: Int) {
        _devices.update { currentList ->
            currentList.map { device ->
                if (device.id == deviceId && device.isOnline) {
                    device.copy(isActive = !device.isActive)
                } else {
                    device
                }
            }
        }
    }
}
