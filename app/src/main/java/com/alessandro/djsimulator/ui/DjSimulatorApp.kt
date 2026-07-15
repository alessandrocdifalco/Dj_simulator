package com.alessandro.djsimulator.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alessandro.djsimulator.game.*

@Composable fun DjSimulatorApp(vm: GameViewModel = viewModel()) {
    val ui by vm.state.collectAsState()
    if (!ui.career.created) NewCareer(vm::start) else CareerScreen(ui.career, vm::doActivity, vm::reset)
}

@Composable private fun NewCareer(onStart: (String, Scene) -> Unit) {
    var name by remember { mutableStateOf("") }
    var scene by remember { mutableStateOf(Scene.HOUSE) }
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        Text("DJ SIMULATOR", color = Neon, fontWeight = FontWeight.Black, fontSize = 15.sp)
        Text("Da cameretta\na headliner.", fontSize = 42.sp, lineHeight = 45.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(12.dp)); Text("Costruisci tecnica, selezione e reputazione. Ogni settimana conta.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(28.dp))
        OutlinedTextField(name, { name = it.take(18) }, Modifier.fillMaxWidth(), label = { Text("Nome d'arte") }, singleLine = true)
        Spacer(Modifier.height(18.dp)); Text("La tua scena", fontWeight = FontWeight.Bold)
        Row(Modifier.padding(vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Scene.entries.forEach { FilterChip(scene == it, { scene = it }, { Text(it.label) }) }
        }
        Button({ onStart(name, scene) }, Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(16.dp)) { Text("INIZIA LA CARRIERA", fontWeight = FontWeight.Black) }
    }
}

@Composable private fun CareerScreen(c: Career, act: (Activity) -> Unit, reset: () -> Unit) {
    var confirmReset by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text(c.djName.uppercase(), color = Neon, fontWeight = FontWeight.Black); Text("Settimana ${c.week} · Giorno ${c.day}/5", fontSize = 28.sp, fontWeight = FontWeight.Black) }
            IconButton({ confirmReset = true }) { Icon(Icons.Default.Settings, "Impostazioni") }
        }
        Spacer(Modifier.height(14.dp)); StatStrip(c)
        Spacer(Modifier.height(16.dp)); Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(Modifier.padding(18.dp)) { Text("ULTIMO EVENTO", color = Neon, fontWeight = FontWeight.Bold, fontSize = 12.sp); Spacer(Modifier.height(7.dp)); Text(c.lastEvent, fontSize = 17.sp, lineHeight = 23.sp) }
        }
        Spacer(Modifier.height(24.dp)); Text("Scegli la tua giornata", fontWeight = FontWeight.Black, fontSize = 22.sp)
        Text("Dopo il quinto giorno suonerai la serata.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        Activity.entries.chunked(2).forEach { row -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            row.forEach { a -> ActivityCard(a, c.energy >= a.energy || a == Activity.REST, { act(a) }, Modifier.weight(1f)) }
            if (row.size == 1) Spacer(Modifier.weight(1f))
        }; Spacer(Modifier.height(10.dp)) }
        Spacer(Modifier.height(8.dp)); Skills(c)
    }
    if (confirmReset) AlertDialog(onDismissRequest = { confirmReset = false }, title = { Text("Nuova carriera?") }, text = { Text("Il salvataggio attuale verrà eliminato.") }, confirmButton = { TextButton({ confirmReset = false; reset() }) { Text("RESET") } }, dismissButton = { TextButton({ confirmReset = false }) { Text("ANNULLA") } })
}

@Composable private fun StatStrip(c: Career) = Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    MiniStat(Icons.Default.Euro, "${c.money}", "Cassa", Modifier.weight(1f)); MiniStat(Icons.Default.Bolt, "${c.energy}%", "Energia", Modifier.weight(1f)); MiniStat(Icons.Default.Groups, "${c.followers}", "Follower", Modifier.weight(1f))
}

@Composable private fun MiniStat(icon: ImageVector, value: String, label: String, mod: Modifier) = Surface(mod, shape = RoundedCornerShape(15.dp), color = MaterialTheme.colorScheme.surface) {
    Column(Modifier.padding(12.dp)) { Icon(icon, null, tint = Neon, modifier = Modifier.size(19.dp)); Text(value, fontWeight = FontWeight.Black, fontSize = 19.sp); Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
}

@Composable private fun ActivityCard(a: Activity, enabled: Boolean, click: () -> Unit, mod: Modifier) = OutlinedCard(click, mod.height(105.dp), enabled = enabled, shape = RoundedCornerShape(16.dp)) {
    Column(Modifier.fillMaxSize().padding(13.dp), verticalArrangement = Arrangement.Center) { Text(a.title, fontWeight = FontWeight.Bold); Text(a.subtitle, color = Neon, fontSize = 12.sp); if (a.energy > 0) Text("-${a.energy} energia", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
}

@Composable private fun Skills(c: Career) {
    Text("Profilo artistico", fontWeight = FontWeight.Black, fontSize = 22.sp); Spacer(Modifier.height(10.dp))
    listOf("Tecnica" to c.technique, "Selezione" to c.library, "Produzione" to c.production, "Reputazione" to c.reputation).forEach { (n, v) ->
        Row(Modifier.padding(top = 8.dp)) { Text(n, Modifier.weight(1f)); Text("$v", color = Neon, fontWeight = FontWeight.Bold) }
        LinearProgressIndicator({ (v / 100f).coerceIn(0f, 1f) }, Modifier.fillMaxWidth().height(7.dp), color = Neon, trackColor = MaterialTheme.colorScheme.surface)
    }
    Spacer(Modifier.height(28.dp)); Text("${c.gigs} serate suonate · ${c.scene.label}", color = MaterialTheme.colorScheme.onSurfaceVariant)
}
