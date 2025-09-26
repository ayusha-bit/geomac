package io.silentsea.geomac.domain.entites

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
@XmlSerialName("GetLocationUsingFingerprint", "http://inference.location.live.com")
data class MicrosoftRequest(
    @XmlSerialName("RequestHeader")
    @XmlElement
    val requestHeader: RequestHeader? = null,
    @XmlSerialName("BeaconFingerprint")
    @XmlElement
    val beaconFingerprint: BeaconFingerprint? = null
) {
    @Serializable
    data class RequestHeader(
        @OptIn(ExperimentalTime::class)
        @XmlSerialName("Timestamp")
        @XmlElement
        val timestamp: LocalDateTime = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        @XmlSerialName("ApplicationId")
        @XmlElement
        val applicationId: String = "e1e71f6b-2149-45f3-a298-a20682ab5017",
        @XmlSerialName("TrackingId")
        @XmlElement
        val trackingId: String = "21BF9AD6-CFD3-46B3-B041-EE90BD34FDBC",
        @XmlSerialName("DeviceProfile")
        @XmlElement
        val deviceProfile: DeviceProfile? = null,
        @XmlSerialName("Authorization")
        @XmlElement
        val authorization: Authorization = Authorization
    ) {
        @Serializable
        data class DeviceProfile(
            @XmlSerialName("ClientGuid")
            @XmlElement(false)
            val clientGuid: String = "0fc571be-4624-4ce0-b04e-911bdeb1a222",
            @XmlSerialName("Platform")
            @XmlElement(false)
            val platform: String = "Windows7",
            @XmlSerialName("DeviceType")
            @XmlElement(false)
            val deviceType: String = "PC",
            @XmlSerialName("OSVersion")
            @XmlElement(false)
            val osVersion: String = "7600.16695.amd64fre.win7_gdr.101026-1503",
            @XmlSerialName("LFVersion")
            @XmlElement(false)
            val lfVersion: String = "9.0.8080.16413",
            @XmlSerialName("ExtendedDeviceInfo")
            @XmlElement(false)
            val extendedDeviceInfo: String = ""
        )

        @Serializable
        object Authorization
    }

    @Serializable
    data class BeaconFingerprint(
        @XmlSerialName("Detections")
        @XmlElement
        val detections: Detections? = null
    ) {
        @Serializable
        data class Detections(
            @XmlSerialName("Wifi7")
            @XmlElement
            val wifi7: Wifi7? = null
        ) {
            @Serializable
            data class Wifi7(
                @XmlSerialName("BssId")
                @XmlElement(false)
                val bssId: String? = null,
                @XmlSerialName("rssi")
                @XmlElement(false)
                val rssi: String = "-1"
            )
        }
    }
}

@Serializable
@XmlSerialName("GetLocationUsingFingerprintResponse", "http://inference.location.live.com")
data class MicrosoftResponse(
    @XmlSerialName("GetLocationUsingFingerprintResult")
    @XmlElement
    val result: GetLocationUsingFingerprintResult? = null
) {
    @Serializable
    data class GetLocationUsingFingerprintResult(
        @XmlSerialName("ResponseStatus")
        @XmlElement
        val responseStatus: String? = null,
        @XmlSerialName("LocationResult")
        @XmlElement
        val locationResult: LocationResult? = null
    ) {
        @Serializable
        data class LocationResult(
            @XmlSerialName("ResolverStatus")
            @XmlElement
            val resolverStatus: ResolverStatus? = null,
            @XmlSerialName("ResolvedPosition")
            @XmlElement
            val resolvedPosition: ResolvedPosition? = null,
            @XmlSerialName("RadialUncertainty")
            @XmlElement
            val radialUncertainty: Int? = null
        ) {
            @Serializable
            data class ResolverStatus(
                @XmlSerialName("Status")
                @XmlElement(false)
                val status: String? = null,
                @XmlSerialName("Source")
                @XmlElement(false)
                val source: String? = null
            )

            @Serializable
            data class ResolvedPosition(
                @XmlSerialName("Latitude")
                @XmlElement(false)
                val latitude: String? = null,
                @XmlSerialName("Longitude")
                @XmlElement(false)
                val longitude: String? = null
            )
        }
    }
}