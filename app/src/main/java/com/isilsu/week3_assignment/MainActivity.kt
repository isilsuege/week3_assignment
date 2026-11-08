package com.isilsu.week3_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.isilsu.week3_assignment.ui.theme.Week3_assignmentTheme

class MainActivity : ComponentActivity() {

    // Kütüphane hatası almamak için ViewModel'i Activity seviyesinde oluşturduk
    private val viewModel: HomeControlViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Week3_assignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {

                        // Ekranı çağırıyoruz ve viewModel'i veriyoruz
                        DashboardScreen(viewModel = viewModel)

                    }
                }
            }
        }
    }
}