package me.david.lengthconversionservice.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class LoggingConfig {

    @Bean
    @Scope("prototype")
    /** Enable autowiring for [Logger]. */
    fun logger(ip: InjectionPoint): Logger = LoggerFactory.getLogger(ip.methodParameter?.containingClass ?: ip.field?.declaringClass)
}
