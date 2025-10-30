package com.colabear754.kbo_scraper.api.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ConverterFactoryConfig : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverterFactory(object : ConverterFactory<String, Enum<*>> {
            override fun <T : Enum<*>> getConverter(targetType: Class<T>): Converter<String, T> =
                Converter { source ->
                    if (source.isBlank()) null
                    else targetType.enumConstants.firstOrNull { it.name == source.trim().uppercase() }
                }
        })
    }
}