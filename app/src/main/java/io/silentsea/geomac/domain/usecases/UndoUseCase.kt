package io.silentsea.geomac.domain.usecases

import io.silentsea.geomac.data.db.entities.GeomacItemWithCoordinates
import io.silentsea.geomac.domain.repositories.GeomacRepository

class UndoUseCase(private val geomacRepository: GeomacRepository) {
    suspend operator fun invoke(vararg items: GeomacItemWithCoordinates) =
        geomacRepository.undo(*items)
}