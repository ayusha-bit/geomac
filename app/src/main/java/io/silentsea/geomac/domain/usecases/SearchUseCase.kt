package io.silentsea.geomac.domain.usecases

import io.silentsea.geomac.domain.repositories.GeomacRepository

class SearchUseCase(private val geomacRepository: GeomacRepository) {
    operator fun invoke(vararg macs: Long) = geomacRepository.search(*macs)
}