package com.everything.time.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.everything.time.ui.screen.common.GenericContent
import com.everything.time.ui.screen.viewmodel.JavaViewModel

@Composable
fun JavaScreen(
    viewModel: JavaViewModel = hiltViewModel(),
) {
    val state by viewModel.stateFlow.collectAsState()
    Scaffold { innerPadding ->
        GenericContent(
            times = state.times,
            modifier = Modifier.padding(paddingValues = innerPadding)
        )
    }
}
