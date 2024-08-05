package data.dto

import data.dto.CastItemResponse

class WritersResponse(
    val items: List<CastItemResponse>,
    val job: String
)