package me.david.lengthconversionservice.service

import me.david.lengthconversionservice.model.LengthUnit
import me.david.lengthconversionservice.model.LengthUnitConversionResult
import me.david.lengthconversionservice.unitloader.LengthUnitProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode.*

@Component
class LengthConversionService(
        private val lengthUnitProvider: LengthUnitProvider,

        /** Base unit symbol, e.g. `m`. */
        @Value("\${length.units.base.symbol}")
        private val baseUnitSymbol: String,

        /** Number of decimal places for length unit values. */
        @Value("\${length.units.decimal.places}")
        private val decimalPlaces: Int,
) {

    /** @return a list of supported length units. */
    fun getLengthUnits(): List<LengthUnit> = lengthUnitProvider.lengthUnits

    /** @return the base length unit as specified by [baseUnitSymbol] */
    fun getLengthUnitBase(): LengthUnit = getLengthUnit(baseUnitSymbol)

    /** @return the length unit specified by [symbol] */
    fun getLengthUnit(symbol: String): LengthUnit = requireNotNull(lengthUnitProvider.getLengthUnitBySymbol(symbol)) { "Length unit '$symbol' not supported" }

    /** Convert the source value of the given source unit to the target unit and return the [LengthUnitConversionResult]. `*/
    fun convertLengthUnit(sourceValue: BigDecimal, sourceSymbol: String, targetSymbol: String): LengthUnitConversionResult {
        val sourceUnit = getLengthUnit(sourceSymbol)
        val targetUnit = getLengthUnit(targetSymbol)
        val targetValue = (sourceValue * sourceUnit.conversionFactor).divide(targetUnit.conversionFactor, decimalPlaces, HALF_UP)
        return LengthUnitConversionResult(sourceValue, sourceSymbol, targetValue, targetSymbol)
    }
}