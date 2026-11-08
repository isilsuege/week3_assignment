package com.isilsu.week3_assignment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DashboardScreen(viewModel: HomeControlViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        state = state,
        onSearchUpdate = viewModel::updateSearchText,
        onRoomClick = viewModel::selectRoom,
        onToggleClick = viewModel::toggleDevicePower
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    state: HomeControlState,
    onSearchUpdate: (String) -> Unit,
    onRoomClick: (String?) -> Unit,
    onToggleClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {

        // Tasarımı ve metni değiştirilmiş sayaç kartı
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Aktif Enerji Tüketimi", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "${state.currentPowerLoad} W",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        OutlinedTextField(
            value = state.searchText,
            onValueChange = onSearchUpdate,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            label = { Text("Cihaz filtrele...") },
            singleLine = true
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(state.roomOptions) { room ->
                FilterChip(
                    selected = state.filteredRoom == room,
                    onClick = { onRoomClick(room) },
                    label = { Text(room) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.deviceList, key = { it.id }) { item ->
                SmartApplianceRow(device = item, onToggle = { onToggleClick(item.id) })
            }
        }
    }
}

@Composable
fun SmartApplianceRow(device: SmartDevice, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W600
                )
                Text(
                    text = "${device.roomName} | ${device.powerUsage} Watt",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (!device.isOnline) {
                    Text(
                        text = "Bağlantı Yok",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Switch(
                checked = device.isActive,
                onCheckedChange = { onToggle() },
                enabled = device.isOnline
            )
        }
    }
}