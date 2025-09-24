package io.silentsea.geomac.data.db.entities

import androidx.room.Relation

data class GeomacItemWithCoordinates(
    val mac: Long,
    @Relation(
        parentColumn = "mac",
        entityColumn = "mac"
    )
    val coordinates: List<GeomacCoordinates>
)