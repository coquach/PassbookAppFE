package com.se104.passbookapp.utils.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement

import java.lang.reflect.Type
import java.math.BigDecimal

class BigDecimalDeserializer : JsonDeserializer<BigDecimal> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BigDecimal? {
        return json?.asDouble?.let { BigDecimal.valueOf(it) }
    }
}