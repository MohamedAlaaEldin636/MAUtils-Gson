package com.maproductions.lib.flow_1.custom_classes

sealed class DataResult<T> {

    class Loading<T> : DataResult<T>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Loading<*>) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    data class Success<T>(val value: T) : DataResult<T>()

    sealed class Error<T>(open val userErrorMsg: String) : DataResult<T>() {

        data class NoInternetConnection<T>(override val userErrorMsg: String): Error<T>(userErrorMsg)

        data class Unknown<T>(override val userErrorMsg: String): Error<T>(userErrorMsg)

        class CanNotBeFoundOnServer<T>: Error<T>("") {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is CanNotBeFoundOnServer<*>) return false
                return true
            }

            override fun hashCode(): Int {
                return javaClass.hashCode()
            }
        }

    }

}
