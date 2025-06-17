package com.se104.passbookapp.data.model.enums

import android.view.Display

enum class TransactionType(val display: String) {
    DEPOSIT("Nạp tiền"),
    WITHDRAWAL("Rút tiền"),
    SAVE("Tiết kiệm"),
    WITHDRAW_SAVING("Rút tiết kiệm");

    fun getDisplayName(): String = display

    override fun toString(): String = name

    companion object {
        fun fromDisplay(display: String): TransactionType {
            return TransactionType.entries.first { it.display == display }
        }
        fun fromName(name: String): TransactionType {
            return TransactionType.entries.first { it.name == name }
        }
    }
}