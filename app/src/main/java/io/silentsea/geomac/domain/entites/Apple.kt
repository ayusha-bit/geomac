package io.silentsea.geomac.domain.entites

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoIntegerType
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.ProtoType

@Serializable
data class AppleRequest(
    @ExperimentalSerializationApi
    @ProtoNumber(2)
    val wifis: List<Wifi> = emptyList(),
    @ExperimentalSerializationApi
    @ProtoNumber(3)
    @ProtoType(ProtoIntegerType.DEFAULT)
    val noise: Int? = 0,
    @ExperimentalSerializationApi
    @ProtoNumber(4)
    @ProtoType(ProtoIntegerType.DEFAULT)
    val signal: Int? = 100
) {
    @Serializable
    data class Wifi(
        @ExperimentalSerializationApi
        @ProtoNumber(1)
        val mac: String? = null
    )
}

@Serializable
data class AppleResponse(
    @ExperimentalSerializationApi
    @ProtoNumber(2)
    val wifis: List<Wifi> = emptyList()
) {
    @Serializable
    data class Wifi(
        @ExperimentalSerializationApi
        @ProtoNumber(2)
        val location: Location? = null
    ) {
        @Serializable
        data class Location(
            @ExperimentalSerializationApi
            @ProtoNumber(1)
            @ProtoType(ProtoIntegerType.DEFAULT)
            val latitude: Long? = null,
            @ExperimentalSerializationApi
            @ProtoNumber(2)
            @ProtoType(ProtoIntegerType.DEFAULT)
            val longitude: Long? = null
        )
    }
}