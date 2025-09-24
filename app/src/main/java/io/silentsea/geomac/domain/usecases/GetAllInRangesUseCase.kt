package io.silentsea.geomac.domain.usecases

import io.silentsea.geomac.domain.repositories.GeomacRepository

class GetAllInRangesUseCase(private val geomacRepository: GeomacRepository) {
    operator fun invoke(vararg ranges: Pair<Long, Long>) = geomacRepository.getAllInRanges(*ranges)
}