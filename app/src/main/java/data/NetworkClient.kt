package data

import data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}