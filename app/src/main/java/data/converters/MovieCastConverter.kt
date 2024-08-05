package data.converters

import data.dto.ActorResponse
import data.dto.CastItemResponse
import data.dto.DirectorsResponse
import data.dto.MovieCastResponse
import data.dto.OtherResponse
import data.dto.WritersResponse
import domain.models.MovieCast
import domain.models.MovieCastPerson

class MovieCastConverter {

    fun convert(response: MovieCastResponse): MovieCast {
        return with(response) {
            MovieCast(
                imDbId = imDbId,
                fullTitle = fullTitle,
                directors = convertDirectors(this.directors),
                writers = convertWriters(this.writers),
                actors = convertActors(this.actors),
                others = convertOthers(this.others),
            )
        }
    }

    private fun convertDirectors(directorsResponse: DirectorsResponse): List<MovieCastPerson> {
        return directorsResponse.items.map {
            it.toMovieCastPerson() // Здесь `it` (внутри map) представляет текущий элемент из `directorsResponse.items`
        }
    }

    private fun convertWriters(writersResponse: WritersResponse): List<MovieCastPerson> {
        return writersResponse.items.map {
            it.toMovieCastPerson()
        }
    }

    private fun convertActors(actorsResponse: List<ActorResponse>): List<MovieCastPerson> {
        return actorsResponse.map { actor ->
            MovieCastPerson(
                id = actor.id,
                name = actor.name,
                description = actor.asCharacter,
                image = actor.image
            )
        }
    }

    private fun convertOthers(othersResponse: List<OtherResponse>): List<MovieCastPerson> {
        return othersResponse.flatMap { othersResponse ->
            othersResponse.items.map { person ->
                MovieCastPerson(
                    id = person.id,
                    name = person.name,
                    description = "${othersResponse.job} -- ${person.description}",
                    image = null,
                )
            }
        }
    }


    private fun CastItemResponse.toMovieCastPerson(jobPrefix: String = ""): MovieCastPerson {   //в Kotlin можно указывать значения по умолчанию для параметров функции
        return MovieCastPerson(
            id = this.id,
            name = this.name,
            description = if (jobPrefix.isEmpty()) this.description else "$jobPrefix -- ${this.description}",
            image = null
        )
    }
}
