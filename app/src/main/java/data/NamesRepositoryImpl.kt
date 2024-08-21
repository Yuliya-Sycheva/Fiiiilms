package data

import data.dto.NamesSearchRequest
import data.dto.NamesSearchResponse
import domain.api.NamesRepository
import domain.models.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import utils.Resource

class NamesRepositoryImpl(private val networkClient: NetworkClient) : NamesRepository {

    override fun getNames(expression: String): Flow<Resource<List<Person>>> = flow {

        val response = networkClient.doRequest(NamesSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                emit(Resource.Success((response as NamesSearchResponse).results.map {
                    Person(it.id, it.image, it.title, it.description)
                }))
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}