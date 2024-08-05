package domain.api

import domain.models.Person
import kotlinx.coroutines.flow.Flow

interface NamesInteractor {

    fun getNames(expression: String): Flow<Pair<List<Person>?, String?>>
}