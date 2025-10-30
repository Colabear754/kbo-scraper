package com.colabear754.kbo_scraper.api.dto

import org.springframework.http.HttpStatus

data class GlobalResponse<T>(
    val code: HttpStatus,
    val message: String? = null,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T? = null): GlobalResponse<T> {
            return GlobalResponse(HttpStatus.OK, "성공", data)
        }

        fun error(code: HttpStatus, message: String): GlobalResponse<*> {
            return GlobalResponse(code, message, null)
        }
    }
}