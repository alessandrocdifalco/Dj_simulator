package com.alessandro.djsimulator.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alessandro.djsimulator.data.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class GameViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = GameRepository(app)
    private val _state = MutableStateFlow(GameUiState(repo.load()))
    val state: StateFlow<GameUiState> = _state

    fun start(name: String, scene: Scene) = update(Career(created = true, djName = name.trim().ifBlank { "NO NAME" }, scene = scene))

    fun doActivity(a: Activity) {
        val c = _state.value.career
        if (a != Activity.REST && c.energy < a.energy) { update(c.copy(lastEvent = "Sei troppo stanco. Riposa prima di continuare.")); return }
        var n = when (a) {
            Activity.PRACTICE -> c.copy(technique = c.technique + 2, energy = c.energy - a.energy, lastEvent = "Transizioni più pulite. La pratica si sente.")
            Activity.DIG -> c.copy(library = c.library + 2, money = c.money - 12, energy = c.energy - a.energy, lastEvent = "Hai trovato tre tracce che nessun altro sta suonando.")
            Activity.PRODUCE -> c.copy(production = c.production + 2, energy = c.energy - a.energy, lastEvent = "Il tuo nuovo edit comincia ad avere carattere.")
            Activity.NETWORK -> c.copy(reputation = c.reputation + 2, followers = c.followers + 5, energy = c.energy - a.energy, lastEvent = "Un promoter locale ha salvato il tuo contatto.")
            Activity.WORK -> c.copy(money = c.money + 65, energy = c.energy - a.energy, lastEvent = "Turno finito. Non è il club, ma paga le tracce.")
            Activity.REST -> c.copy(energy = min(100, c.energy + 38), lastEvent = "Hai recuperato energie e messo a fuoco le prossime mosse.")
        }
        n = if (c.day >= 5) playGig(n.copy(day = 1, week = c.week + 1)) else n.copy(day = c.day + 1)
        update(n)
    }

    private fun playGig(c: Career): Career {
        val score = c.technique * 2 + c.library * 2 + c.reputation + Random.nextInt(-12, 13)
        val great = score >= 42
        val pay = if (great) 120 + c.reputation * 2 else 55
        return c.copy(money = c.money + pay, energy = max(25, c.energy - 18), gigs = c.gigs + 1,
            reputation = max(0, c.reputation + if (great) 4 else 1), followers = c.followers + if (great) 28 else 8,
            lastEvent = if (great) "SERATA MEMORABILE · Il dancefloor esplode. +€$pay e nuovi follower." else "SERATA COMPLETATA · Qualche errore, ma hai tenuto la pista. +€$pay.")
    }

    fun reset() { repo.reset(); _state.value = GameUiState() }
    private fun update(c: Career) { repo.save(c); _state.value = GameUiState(c, c.money < -100) }
}
