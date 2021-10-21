package me.david.lengthconversionservice.model

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

/** Represents the conversion result from source to target unit, e.g. `MeasurementUnitConversionResult(2.5, "cm", 25, "mm")`. */
data class LengthUnitConversionResult(
        @Schema(required = true, description = "Source unit value for conversion", example = "2.5")
        val sourceValue: BigDecimal,

        @Schema(required = true, description = "Source unit symbol", example = "cm")
        val sourceSymbol: String,

        @Schema(required = true, description = "Target unit converted value", example = "25")
        val targetValue: BigDecimal,

        @Schema(required = true, description = "Target unit symbol", example = "mm")
        val targetSymbol: String,
)