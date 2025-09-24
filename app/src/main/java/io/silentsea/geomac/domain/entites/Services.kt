package io.silentsea.geomac.domain.entites

import io.silentsea.geomac.R

enum class Services {
    APPLE,
    GOOGLE,
    MICROSOFT,
    MYLNIKOV,
    YANDEX
}

val Services.resId: Int
    get() = when (this) {
        Services.APPLE -> R.string.apple
        Services.GOOGLE -> R.string.google
        Services.MICROSOFT -> R.string.microsoft
        Services.MYLNIKOV -> R.string.mylnikov
        Services.YANDEX -> R.string.yandex
    }