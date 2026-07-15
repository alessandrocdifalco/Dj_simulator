package com.alessandro.djsimulator.data

import android.content.Context
import com.alessandro.djsimulator.game.Career
import com.alessandro.djsimulator.game.Scene

class GameRepository(context: Context) {
    private val p = context.getSharedPreferences("dj_career", Context.MODE_PRIVATE)

    fun load(): Career = Career(
        created = p.getBoolean("created", false), djName = p.getString("name", "") ?: "",
        scene = runCatching { Scene.valueOf(p.getString("scene", Scene.HOUSE.name)!!) }.getOrDefault(Scene.HOUSE),
        week = p.getInt("week", 1), day = p.getInt("day", 1), money = p.getInt("money", 180),
        energy = p.getInt("energy", 85), reputation = p.getInt("rep", 4), technique = p.getInt("tech", 8),
        library = p.getInt("library", 6), production = p.getInt("prod", 3), followers = p.getInt("followers", 12),
        gigs = p.getInt("gigs", 0), lastEvent = p.getString("event", Career().lastEvent) ?: Career().lastEvent
    )

    fun save(c: Career) { p.edit().putBoolean("created", c.created).putString("name", c.djName)
        .putString("scene", c.scene.name).putInt("week", c.week).putInt("day", c.day).putInt("money", c.money)
        .putInt("energy", c.energy).putInt("rep", c.reputation).putInt("tech", c.technique)
        .putInt("library", c.library).putInt("prod", c.production).putInt("followers", c.followers)
        .putInt("gigs", c.gigs).putString("event", c.lastEvent).apply() }

    fun reset() = p.edit().clear().apply()
}
