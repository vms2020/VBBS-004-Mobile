// core/data/src/androidTest/java/io/bbs/seva/vbbs004mobile/data/repository/CurrencyRateRepositoryHiltTest.kt
package io.bbs.seva.vbbs004mobile.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.bbs.seva.vbbs004mobile.domain.repository.CurrencyRateRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CurrencyRateRepositoryHiltTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var repository: CurrencyRateRepository   // ← REAL impl, REAL Hilt-built client

    @Before
    fun setUp() { hiltRule.inject() }

    @Test
    fun daily_withProductionClient() = runBlocking {
        val result = repository.getDailyRates(null)

        result.onSuccess { rates ->
            println("date=${rates.date}, total=${rates.rates.size}")
            rates.rates.take(10).forEach { println("${it.charCode}: ${it.perUnit} " +
                    "(id:${it.id} name:${it.name} value:${it.value} nominal:${it.nominal})") }
        }.onFailure { it.printStackTrace() }

        assertTrue(result.isSuccess)
    }


    @Test
    fun dynamics_withProductionClient() = runBlocking {
        val to = LocalDate.now()
        val from = to.minusDays(7)

        val result = repository.getRateDynamics("R01235", from, to)   // R01235 = USD

        println("===== CBR dynamics R01235 (USD) =====")
        result.onSuccess { pts ->
            println("points = ${pts.size}")
            pts.forEach { println("${it.date}: ${it.perUnit}") }
        }.onFailure { it.printStackTrace() }

        assertTrue(result.isSuccess)
        // business-day data: a 7-day window should yield >= 1 point; don't assert exact count
        assertTrue("expected at least one rate point in 7 days", result.getOrDefault(emptyList()).isNotEmpty())
    }
}
