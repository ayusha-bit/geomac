package io.silentsea.geomac.domain.entites

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoIntegerType
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.ProtoType

@Serializable
data class GoogleRequest(
    @ExperimentalSerializationApi
    @ProtoNumber(1)
    val header: Header? = null,
    @ExperimentalSerializationApi
    @ProtoNumber(4)
    val location: List<Location> = emptyList()
) {
    @Serializable
    data class Header(
        @ExperimentalSerializationApi
        @ProtoNumber(1)
        val version: String? = null,
        @ExperimentalSerializationApi
        @ProtoNumber(2)
        val platform: String? = null,
        @ExperimentalSerializationApi
        @ProtoNumber(5)
        val locale: String? = null,
    )

    @Serializable
    data class Location(
        @ExperimentalSerializationApi
        @ProtoNumber(2)
        val data: Data? = null
    ) {
        @Serializable
        data class Data(
            @ExperimentalSerializationApi
            @ProtoNumber(1)
            @ProtoType(ProtoIntegerType.DEFAULT)
            val timestamp: Long? = null,
            @ExperimentalSerializationApi
            @ProtoNumber(2)
            val wifis: List<Wifi> = emptyList(),
            @ExperimentalSerializationApi
            @ProtoNumber(3)
            @ProtoType(ProtoIntegerType.DEFAULT)
            val size: Int? = null
        ) {
            @Serializable
            data class Wifi(
                @ExperimentalSerializationApi
                @ProtoNumber(1)
                val text: String? = null,
                @ExperimentalSerializationApi
                @ProtoNumber(8)
                @ProtoType(ProtoIntegerType.DEFAULT)
                val mac: Long? = null
            )
        }
    }
}

@Serializable
data class GoogleResponse(
    @ExperimentalSerializationApi
    @ProtoNumber(2)
    val data: Data? = null
) {
    @Serializable
    data class Data(
        @ExperimentalSerializationApi
        @ProtoNumber(3)
        val wifis: List<Wifi> = emptyList()
    ) {
        @Serializable
        data class Wifi(
            @ExperimentalSerializationApi
            @ProtoNumber(1)
            val wifiData: WifiData? = null
        ) {
            @Serializable
            data class WifiData(
                @ExperimentalSerializationApi
                @ProtoNumber(1)
                val location: Location? = null
            ) {
                @Serializable
                data class Location(
                    @ExperimentalSerializationApi
                    @ProtoNumber(1)
                    @ProtoType(ProtoIntegerType.FIXED)
                    val latitude: Int? = null,
                    @ExperimentalSerializationApi
                    @ProtoNumber(2)
                    @ProtoType(ProtoIntegerType.FIXED)
                    val longitude: Int? = null
                )
            }
        }
    }
}