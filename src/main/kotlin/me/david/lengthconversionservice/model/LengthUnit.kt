package me.david.lengthconversionservice.model

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

data class LengthUnit(
        @Schema(required = true, description = "Symbol of the unit", example = "mm")
        val symbol: String,

        @Schema(required = true, description = "Name of the unit", example = "millimeter")
        val name: String,

        @Schema(required = true, description = "Conversion factor relative to the base unit", example = "0.001")
        val conversionFactor: BigDecimal,
)