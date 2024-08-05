package domain.impl

import domain.api.NamesInteractor
import domain.api.NamesRepository
import domain.models.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import utils.Resource
import java.util.concurrent.Executors

class NamesInteractorImpl(private val repository: NamesRepository) : NamesInteractor {

    override fun getNames(expression: String) : Flow<Pair<List<Person>?, String?>> {
        return repository.getNames(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}