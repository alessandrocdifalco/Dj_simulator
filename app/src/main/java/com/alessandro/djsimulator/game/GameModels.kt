package com.alessandro.djsimulator.game

enum class Scene(val label: String) { HOUSE("House"), TECHNO("Techno"), OPEN_FORMAT("Open format") }

data class Career(
    val created: Boolean = false,
    val djName: String = "",
    val scene: Scene = Scene.HOUSE,
    val week: Int = 1,
    val day: Int = 1,
    val money: Int = 180,
    val energy: Int = 85,
    val reputation: Int = 4,
    val technique: Int = 8,
    val library: Int = 6,
    val production: Int = 3,
    val followers: Int = 12,
    val gigs: Int = 0,
    val lastEvent: String = "La tua carriera comincia in una cameretta, con una console usata e una grande idea."
)

enum class Activity(val title: String, val subtitle: String, val energy: Int) {
    PRACTICE("Allenati ai deck", "+ tecnica", 18),
    DIG("Cerca nuova musica", "+ libreria, - €", 12),
    PRODUCE("Produci un edit", "+ produzione", 22),
    NETWORK("Fai networking", "+ reputazione", 14),
    WORK("Lavora", "+ denaro", 16),
    REST("Riposa", "+ energia", 0)
}

data class GameUiState(val career: Career = Career(), val gameOver: Boolean = false)
