package com.example.joke.service

import lombok.SneakyThrows
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import com.fasterxml.jackson.databind.ObjectMapper

@Lazy
@Service
class BaseService {
    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun getRequest(
        path: String?,
        request: HttpEntity<*>?,
        responseType: Class<*>?
    ): ResponseEntity<*> {
        logDataRequest(request)
        val response: ResponseEntity<*> = testRestTemplate.exchange(path, HttpMethod.GET, request, responseType)
        logDataResponse(response)
        return response
    }

    fun putRequest(
        path: String,
        urlVariables: Map<String, *>,
        request: HttpEntity<*>?,
        responseType: Class<*>?
    ): ResponseEntity<*> {
        logDataRequest(request)
        val response: ResponseEntity<*> = testRestTemplate.exchange(
            path + paramsToRequest(urlVariables),
            HttpMethod.PUT,
            request,
            responseType,
            urlVariables
        )
        logDataResponse(response)
        return response
    }

    fun postRequest(
        path: String?,
        request: HttpEntity<*>?,
        responseType: Class<*>?
    ): ResponseEntity<*> {
        logDataRequest(request)
        val response: ResponseEntity<*> = testRestTemplate.postForEntity(path, request, responseType)
        logDataResponse(response)
        return response
    }

    private fun paramsToRequest(urlVariables: Map<String, *>): String {
        val params: StringBuilder = StringBuilder("?")
        for (k in urlVariables.keys) {
            params.append(k).append("{").append(k).append("}&")
        }
        return params.toString()
    }

    @SneakyThrows
    private fun logDataRequest(request: HttpEntity<*>?) {
        if (request == null) {
            logger.info("Request body: null")
            logger.info("Request headers: null")
        } else {
            logger.info("Request headers: {}", objectMapper.writeValueAsString(request.getHeaders().toString()))
            logger.info("Request body: {}", objectMapper.writeValueAsString(request.getHeaders().toString()))
        }
    }

    @SneakyThrows
    private fun logDataResponse(response: ResponseEntity<*>) {
        logger.info("Response headers: {}", objectMapper.writeValueAsString(response.getHeaders()))
        logger.info("Response body: {}", objectMapper.writeValueAsString(response.getBody()))
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(BaseService::class.java)
    }
}