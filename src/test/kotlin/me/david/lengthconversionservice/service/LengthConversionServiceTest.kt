package me.david.lengthconversionservice.service

import io.mockk.every
import io.mockk.mockk
import me.david.lengthconversionservice.model.LengthUnit
import me.david.lengthconversionservice.unitloader.LengthUnitLoader
import org.hamcrest.MatcherAssert.*
import org.hamcrest.text.MatchesPattern
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal
import java.math.RoundingMode.*
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LengthConversionServiceTest {
    private val lengthUnitLoader = mockk<LengthUnitLoader> {
        every { lengthUnits } returns LENGTH_UNITS
    }

    private val lengthConversionService = LengthConversionService(lengthUnitLoader, BASE_UNIT_SYMBOL, DECIMAL_PLACES)

    @ParameterizedTest
    @ValueSource(strings = [BASE_UNIT_SYMBOL, LONG_UNIT_SYMBOL, SHORT_UNIT_SYMBOL])
    fun `test getLengthUnit`(unitSymbol: String) {
        val res = lengthConversionService.getLengthUnit(unitSymbol)

        val expected = LENGTH_UNITS.first { it.symbol == unitSymbol }
        assertEquals(expected.symbol, res.symbol)
        assertEquals(expected.name, res.name)
        assertEquals(expected.conversionFactor, res.conversionFactor)
    }

    @Test
    fun `test getLengthUnit, unsupported unit`() {
        val unitSymbol = "unsupportedUnit"
        val ex = assertFailsWith<IllegalArgumentException> { lengthConversionService.getLengthUnit(unitSymbol) }
        assertThat(ex.message, MatchesPattern.matchesPattern("(?s)(?i).*$unitSymbol.*"))
    }

    @Test
    fun `test getLengthUnits`() {
        assertEquals(LENGTH_UNITS, lengthConversionService.getLengthUnits())
    }

    private class ArgumentsProviderGetLengthUnits : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?) = Stream.of(
                Arguments.of(BASE_UNIT_SYMBOL, SHORT_UNIT_SYMBOL, FACTOR_BASE_TO_SHORT),
                Arguments.of(BASE_UNIT_SYMBOL, LONG_UNIT_SYMBOL, FACTOR_BASE_TO_LONG),
                Arguments.of(SHORT_UNIT_SYMBOL, BASE_UNIT_SYMBOL, FACTOR_SHORT_TO_BASE),
                Arguments.of(LONG_UNIT_SYMBOL, BASE_UNIT_SYMBOL, FACTOR_LONG_TO_BASE),
                Arguments.of(SHORT_UNIT_SYMBOL, LONG_UNIT_SYMBOL, FACTOR_SHORT_TO_LONG),
                Arguments.of(LONG_UNIT_SYMBOL, SHORT_UNIT_SYMBOL, FACTOR_LONG_TO_SHORT),
        )
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentsProviderGetLengthUnits::class)
    fun `test convertLengthUnit`(sourceSymbol: String, targetSymbol: String, expectedCoefficient: Double) {
        val sourceValue = BigDecimal("123.4567")

        val expectedTargetValue = sourceValue.multiply(expectedCoefficient.toBigDecimal()).setScale(DECIMAL_PLACES, HALF_UP)
        val res = lengthConversionService.convertLengthUnit(sourceValue, sourceSymbol, targetSymbol)

        assertEquals(sourceValue, res.sourceValue)
        assertEquals(sourceSymbol, res.sourceSymbol)
        assertEquals(targetSymbol, res.targetSymbol)
        assertEquals(expectedTargetValue, res.targetValue)
    }


    companion object {
        private const val BASE_UNIT_SYMBOL = "B"
        private const val BASE_UNIT_NAME = "Base unit"
        private const val LONG_UNIT_SYMBOL = "L"
        private const val SHORT_UNIT_SYMBOL = "S"
        private val LENGTH_UNITS = listOf(
                LengthUnit(BASE_UNIT_SYMBOL, BASE_UNIT_NAME, BigDecimal.ONE),
                LengthUnit(LONG_UNIT_SYMBOL, "Long unit", BigDecimal(10)),
                LengthUnit(SHORT_UNIT_SYMBOL, "Short unit", BigDecimal(0.2)),
        ).sortedBy { it.symbol }

        private const val DECIMAL_PLACES = 5

        private const val FACTOR_SHORT_TO_BASE = 0.2
        private const val FACTOR_BASE_TO_SHORT = 1 / FACTOR_SHORT_TO_BASE
        private const val FACTOR_LONG_TO_BASE = 10.0
        private const val FACTOR_BASE_TO_LONG = 1 / FACTOR_LONG_TO_BASE
        private const val FACTOR_SHORT_TO_LONG = FACTOR_SHORT_TO_BASE * FACTOR_BASE_TO_LONG
        private const val FACTOR_LONG_TO_SHORT = 1 / FACTOR_SHORT_TO_LONG
    }
}