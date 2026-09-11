package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

data class CoupleProfile(
    val partner1: String = "Marcos",
    val partner2: String = "Paola",
    val anniversaryMillis: Long,
    val romanticQuote: String = "Juntos es nuestro lugar favorito en el mundo"
)

class CouplePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("couple_prefs", Context.MODE_PRIVATE)

    private val _profileFlow = MutableStateFlow(loadProfile())
    val profileFlow: StateFlow<CoupleProfile> = _profileFlow.asStateFlow()

    private fun loadProfile(): CoupleProfile {
        // Anniversary date: July 4, 2026 (El día que nos conocimos)
        val defaultCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2026)
            set(Calendar.MONTH, Calendar.JULY)
            set(Calendar.DAY_OF_MONTH, 4)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val p1 = prefs.getString(KEY_PARTNER1, "Marcos") ?: "Marcos"
        val p2 = prefs.getString(KEY_PARTNER2, "Paola") ?: "Paola"
        
        // If explicit July 4, 2026 migration flag is not set, initialize to July 4, 2026
        val anniversary = if (prefs.contains(KEY_EXPLICIT_DATE_SET)) {
            prefs.getLong(KEY_ANNIVERSARY, defaultCal.timeInMillis)
        } else {
            prefs.edit()
                .putLong(KEY_ANNIVERSARY, defaultCal.timeInMillis)
                .putBoolean(KEY_EXPLICIT_DATE_SET, true)
                .apply()
            defaultCal.timeInMillis
        }
        val quote = prefs.getString(KEY_QUOTE, "Juntos es nuestro lugar favorito en el mundo") ?: "Juntos es nuestro lugar favorito en el mundo"

        return CoupleProfile(p1, p2, anniversary, quote)
    }

    fun updateAnniversary(millis: Long) {
        prefs.edit()
            .putLong(KEY_ANNIVERSARY, millis)
            .putBoolean(KEY_EXPLICIT_DATE_SET, true)
            .apply()
        _profileFlow.value = _profileFlow.value.copy(anniversaryMillis = millis)
    }

    fun updatePartners(p1: String, p2: String) {
        prefs.edit().putString(KEY_PARTNER1, p1).putString(KEY_PARTNER2, p2).apply()
        _profileFlow.value = _profileFlow.value.copy(partner1 = p1, partner2 = p2)
    }

    fun updateQuote(quote: String) {
        prefs.edit().putString(KEY_QUOTE, quote).apply()
        _profileFlow.value = _profileFlow.value.copy(romanticQuote = quote)
    }

    companion object {
        private const val KEY_PARTNER1 = "key_partner_1"
        private const val KEY_PARTNER2 = "key_partner_2"
        private const val KEY_ANNIVERSARY = "key_anniversary"
        private const val KEY_QUOTE = "key_quote"
        private const val KEY_EXPLICIT_DATE_SET = "key_anniversary_jul_4_2026"
    }
}
