package domain.models

sealed class NetworkError {
    class SomethingWentWrong() : NetworkError()
    class NothingFound() : NetworkError()
}