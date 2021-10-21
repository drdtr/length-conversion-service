package me.david.lengthconversionservice.web

import io.swagger.v3.oas.annotations.Operation
import me.david.lengthconversionservice.service.LengthConversionService
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class LengthConversionRestController(private val lengthConversionService: LengthConversionService) {

    @GetMapping(path = [URL_RESOURCE], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Returns the supported length units.")
    fun getLengthUnits() = ResponseEntity.ok(lengthConversionService.getLengthUnits())

    @GetMapping(path = [URL_UNIT_BASE], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Returns the base length unit.")
    fun getLengthUnitBase() = ResponseEntity.ok(lengthConversionService.getLengthUnitBase())

    @GetMapping(path = [URL_UNIT], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Returns the length unit for the specified unit symbol.")
    fun getLengthUnit(
            @PathVariable(PARAM_UNIT_SYMBOL) unitSymbol: String,
    ) = ResponseEntity.ok(lengthConversionService.getLengthUnit(unitSymbol))

    @GetMapping(path = [URL_UNIT_CONVERT], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Returns the conversion result for the given units and source value.")
    fun getLengthUnitConversionResult(
            @PathVariable(PARAM_UNIT_SYMBOL_SOURCE) sourceUnitSymbol: String,
            @PathVariable(PARAM_VALUE_SOURCE) sourceValue: BigDecimal,
            @PathVariable(PARAM_UNIT_SYMBOL_TARGET) targetUnitSymbol: String,
    ) = ResponseEntity.ok(
            lengthConversionService.convertLengthUnit(
                    sourceSymbol = sourceUnitSymbol,
                    sourceValue = sourceValue,
                    targetSymbol = targetUnitSymbol,
            ))


    companion object {
        internal const val URL_RESOURCE = "/length-unit/"
        internal const val PARAM_UNIT_SYMBOL = "unit-symbol"
        internal const val PARAM_UNIT_SYMBOL_SOURCE = "source-unit-symbol"
        internal const val PARAM_UNIT_SYMBOL_TARGET = "target-unit-symbol"
        internal const val PARAM_VALUE_SOURCE = "source-value"

        internal const val URL_UNIT = "$URL_RESOURCE/{$PARAM_UNIT_SYMBOL}"
        internal const val URL_UNIT_BASE = "$URL_RESOURCE/base"
        internal const val URL_UNIT_CONVERT = "$URL_RESOURCE/{$PARAM_UNIT_SYMBOL_SOURCE}/{$PARAM_VALUE_SOURCE}/converted-to/{$PARAM_UNIT_SYMBOL_TARGET}"
    }
}