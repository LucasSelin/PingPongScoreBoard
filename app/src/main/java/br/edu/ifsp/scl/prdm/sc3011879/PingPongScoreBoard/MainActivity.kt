package br.edu.ifsp.scl.prdm.sc3011879.PingPongScoreBoard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.scl.prdm.sc3011879.PingPongScoreBoard.ui.theme.PingPongScoreBoardTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember


data class Scoreboard(
    var scoreA : Int = 0,
    var scoreB : Int = 0
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PingPongScoreBoardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        RememberScreen()
        MutableStateScreen()
        StateFlowScreen()
        SavedStateScreen()
    }
}

@Composable
fun RememberScreen() {
    var scoreA by remember { mutableIntStateOf(0) }
    var scoreB by remember { mutableIntStateOf(0) }

    Placar("1 - remember", scoreA, scoreB, { scoreA++ }, { scoreB++ }) {
        scoreA = 0
        scoreB = 0
    }
}

@Composable
fun Placar(
    titulo: String,
    scoreA: Int,
    scoreB: Int,
    onIncrementA: () -> Unit,
    onIncrementB: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(titulo)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Jogador A: $scoreA")
            Button(onClick = onIncrementA) { Text("+1") }
            Text("Jogador B: $scoreB")
            Button(onClick = onIncrementB) { Text("+1") }
        }
        Button(onClick = onReset) { Text("Reiniciar partida") }
    }
}

@Composable
fun MutableStateScreen(mutableStateViewModel: MutableStateViewModel = viewModel()) {
    val uiState = mutableStateViewModel.uiState

    Placar(
        "2 - ViewModel + mutableStateOf",
        uiState.scoreA,
        uiState.scoreB,
        mutableStateViewModel::incrementA,
        mutableStateViewModel::incrementB,
        mutableStateViewModel::reset
    )
}

class MutableStateViewModel : ViewModel() {
    var uiState by mutableStateOf(Scoreboard())
        private set

    fun incrementA() {
        uiState = uiState.copy(scoreA = uiState.scoreA + 1)
    }

    fun incrementB() {
        uiState = uiState.copy(scoreB = uiState.scoreB + 1)
    }

    fun reset() {
        uiState = Scoreboard()
    }
}

@Composable
fun StateFlowScreen(stateFlowViewModel: StateFlowViewModel = viewModel()) {
    val uiState by stateFlowViewModel.uiState.collectAsState()

    Placar(
        "3 - ViewModel + StateFlow",
        uiState.scoreA,
        uiState.scoreB,
        stateFlowViewModel::incrementA,
        stateFlowViewModel::incrementB,
        stateFlowViewModel::reset
    )
}

class StateFlowViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(Scoreboard())
    val uiState: StateFlow<Scoreboard> = _uiState.asStateFlow()

    fun incrementA() = _uiState.update { it.copy(scoreA = it.scoreA + 1) }

    fun incrementB() = _uiState.update { it.copy(scoreB = it.scoreB + 1) }

    fun reset() = _uiState.update { Scoreboard() }
}

@Composable
fun SavedStateScreen(savedStateViewModel: SavedStateViewModel = viewModel()) {
    val uiState by savedStateViewModel.uiState.collectAsState()

    Placar(
        "4 - ViewModel + SavedStateHandle",
        uiState.scoreA,
        uiState.scoreB,
        savedStateViewModel::incrementA,
        savedStateViewModel::incrementB,
        savedStateViewModel::reset
    )
}

class SavedStateViewModel(val savedStateHandle: SavedStateHandle) : ViewModel() {
    private companion object {
        const val SCORE_A_KEY = "scoreA"
        const val SCORE_B_KEY = "scoreB"
    }
    private val _uiState = MutableStateFlow(
        Scoreboard(savedStateHandle[SCORE_A_KEY] ?: 0, savedStateHandle[SCORE_B_KEY] ?: 0)
    )
    val uiState: StateFlow<Scoreboard> = _uiState.asStateFlow()

    fun incrementA() = save { it.copy(scoreA = it.scoreA + 1) }

    fun incrementB() = save { it.copy(scoreB = it.scoreB + 1) }

    fun reset() = save { Scoreboard() }

    private fun save(transform: (Scoreboard) -> Scoreboard) {
        _uiState.update(transform)
        savedStateHandle[SCORE_A_KEY] = _uiState.value.scoreA
        savedStateHandle[SCORE_B_KEY] = _uiState.value.scoreB
    }
}
