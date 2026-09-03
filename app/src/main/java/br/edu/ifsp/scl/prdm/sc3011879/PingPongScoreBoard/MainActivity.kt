package br.edu.ifsp.scl.prdm.sc3011879.PingPongScoreBoard

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.edu.ifsp.scl.prdm.sc3011879.PingPongScoreBoard.ui.theme.PingPongScoreBoardTheme
import kotlinx.parcelize.Parcelize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Parcelize
data class Scoreboard(
    var scoreA : Int = 0,
    var scoreB : Int = 0
): Parcelable

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


