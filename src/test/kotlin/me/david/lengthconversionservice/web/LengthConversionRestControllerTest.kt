package me.david.lengthconversionservice.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import me.david.lengthconversionservice.model.LengthUnit
import me.david.lengthconversionservice.model.LengthUnitConversionResult
import me.david.lengthconversionservice.service.LengthConversionService
import me.david.lengthconversionservice.web.LengthConversionRestController.Companion.URL_UNIT
import me.david.lengthconversionservice.web.LengthConversionRestController.Companion.URL_UNIT_BASE
import me.david.lengthconversionservice.web.LengthConversionRestController.Companion.URL_UNIT_CONVERT
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
class LengthConversionRestControllerTest {

    @MockkBean
    private lateinit var serviceMock: LengthConversionService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jacksonObjectMapper: ObjectMapper

    private fun Any.toJson() = jacksonObjectMapper.writeValueAsString(this)

    @Test
    fun `test getLengthUnitBase`() {
        val expected = LengthUnit("baseUnit", "baseUnitName", BigDecimal(1))
        every { serviceMock.getLengthUnitBase() } returns expected

        mockMvc.perform(get(URL_UNIT_BASE))
                .andExpect(status().isOk)
                .andExpect(content().json(expected.toJson()))
    }

    @Test
    fun `test getLengthUnit`() {
        val unitSymbol = "unitSymbol"
        val expected = LengthUnit(unitSymbol, "unitName", BigDecimal(123))
        every { serviceMock.getLengthUnit(unitSymbol) } returns expected

        mockMvc.perform(get(URL_UNIT, unitSymbol))
                .andExpect(status().isOk)
                .andExpect(content().json(expected.toJson()))
    }

    @Test
    fun `test getLengthUnit, bad request`() {
        val unitSymbol = "unitSymbol"
        val errMsg = "Some error"
        every { serviceMock.getLengthUnit(unitSymbol) } throws IllegalArgumentException(errMsg)

        mockMvc.perform(get(URL_UNIT, unitSymbol))
                .andExpect(status().isBadRequest)
                .andExpect(content().json("{ \"message\" : \"$errMsg\" }"))
    }

    @Test
    fun `test getLengthUnitConversionResult`() {
        val sourceUnitSymbol = "sourceUnitSymbol"
        val sourceValue = BigDecimal("1.23")
        val targetUnitSymbol = "targetUnitSymbol"
        val targetValue = BigDecimal(321)
        val expected = LengthUnitConversionResult(sourceValue, sourceUnitSymbol, targetValue, targetUnitSymbol)
        every { serviceMock.convertLengthUnit(sourceValue, sourceUnitSymbol, targetUnitSymbol) } returns expected

        mockMvc.perform(get(URL_UNIT_CONVERT, sourceUnitSymbol, sourceValue, targetUnitSymbol))
                .andExpect(status().isOk)
                .andExpect(content().json(expected.toJson()))
    }

    @Test
    fun `test getLengthUnitConversionResult, bad request`() {
        val sourceUnitSymbol = "sourceUnitSymbol"
        val sourceValue = BigDecimal("12.34")
        val targetUnitSymbol = "targetUnitSymbol"
        val errMsg = "Some error"
        every { serviceMock.convertLengthUnit(sourceValue, sourceUnitSymbol, targetUnitSymbol) } throws IllegalArgumentException(errMsg)

        mockMvc.perform(get(URL_UNIT_CONVERT, sourceUnitSymbol, sourceValue, targetUnitSymbol))
                .andExpect(status().isBadRequest)
                .andExpect(content().json("{ \"message\" : \"$errMsg\" }"))
    }
}