package me.david.lengthconversionservice.unitloader

import me.david.lengthconversionservice.model.LengthUnit
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class LengthUnitLoader(
        /** Path to the CSV file with the supported length units. */
        @Value("\${length.units.csv.file.path}")
        private val csvFilePath: String,
) {

        val lengthUnits: List<LengthUnit> = readLengthUnitsFromCsv()

        private fun readLengthUnitsFromCsv(): List<LengthUnit> {
                val path = javaClass.classLoader.getResource(csvFilePath)?.path ?: error("CSV file with length units not found")
                return File(path)
                        .readLines()
                        .map { it.split(",") }
                        .map { line ->
                                val (symbol, name, factor) = line.map { it.trim() }
                                LengthUnit(symbol, name, factor.toBigDecimal())
                        }
        }
}