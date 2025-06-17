package com.se104.passbookapp.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.se104.passbookapp.data.model.SavingType
import kotlinx.serialization.json.Json

val savingTypeNavType = object : NavType<SavingType>(false) {
    override fun get(bundle: Bundle, key: String): SavingType {
        return parseValue(bundle.getString(key).toString())

    }

    override fun parseValue(value: String): SavingType {
        return Json.decodeFromString(SavingType.serializer(), value)
    }

    override fun serializeAsValue(value: SavingType): String {
        return Json.encodeToString(SavingType.serializer(), value)
    }

    override fun put(bundle: Bundle, key: String, value: SavingType) {
        bundle.putString(key, serializeAsValue(value))
    }
}
val savingTicketNavType = object : NavType<SavingTicket>(false) {
    override fun get(bundle: Bundle, key: String): SavingTicket {
        return parseValue(bundle.getString(key).toString())

    }

    override fun parseValue(value: String): SavingTicket {
        return Json.decodeFromString(SavingTicket.serializer(), value)
    }

    override fun serializeAsValue(value: SavingTicket): String {
        return Json.encodeToString(SavingTicket.serializer(), value)
    }

    override fun put(bundle: Bundle, key: String, value: SavingTicket) {
        bundle.putString(key, serializeAsValue(value))
    }
}