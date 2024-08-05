package domain.api

import domain.models.Person
import kotlinx.coroutines.flow.Flow
import utils.Resource

interface NamesRepository {

    fun getNames(expression: String) : Flow<Resource<List<Person>>>
}