package io.silentsea.geomac.di

import io.silentsea.geomac.data.repositories.GeomacRepositoryImpl
import io.silentsea.geomac.domain.repositories.GeomacRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoriesModule = module {
    singleOf(::GeomacRepositoryImpl) bind GeomacRepository::class
}