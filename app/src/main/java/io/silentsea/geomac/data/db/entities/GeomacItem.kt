package io.silentsea.geomac.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeomacItem(
    @PrimaryKey
    val mac: Long,
)