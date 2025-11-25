package com.colabear754.kbo_scraper.api.config

import com.colabear754.kbo_scraper.api.dto.GlobalResponse
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor

@Component
class StringToGlobalResponsePostProcessor : BeanPostProcessor {
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        if (bean is RequestMappingHandlerAdapter) {
            val handlers = bean.returnValueHandlers ?: return bean
            val index = handlers.indexOfFirst { it is RequestResponseBodyMethodProcessor }
            val stringToGlobalResponseHandler = object : RequestResponseBodyMethodProcessor(bean.messageConverters) {
                override fun supportsReturnType(returnType: MethodParameter): Boolean {
                    return returnType.parameterType == String::class.java
                }

                override fun handleReturnValue(
                    returnValue: Any?,
                    returnType: MethodParameter,
                    mavContainer: ModelAndViewContainer,
                    webRequest: NativeWebRequest) {
                    super.handleReturnValue(
                        GlobalResponse.success(returnValue),
                        returnType,
                        mavContainer,
                        webRequest
                    )
                }
            }

            bean.returnValueHandlers = buildList {
                addAll(handlers.take(index))
                add(stringToGlobalResponseHandler)
                addAll(handlers.drop(index))
            }
        }
        return bean
    }
}