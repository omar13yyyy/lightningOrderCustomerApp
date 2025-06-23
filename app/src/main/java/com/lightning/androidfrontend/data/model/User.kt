package com.lightning.androidfrontend.data.model

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

@Serializable
data class UserLoginParams(
    val number: String,
    val password: String
)

@Serializable
data class UserLoginRes(
    val token: String,
    val message: String
)
data class CheckCodeParams(
    val phoneNumber: String,
    val code: String
)
data class RestPasswordParams(
    val phoneNumber: String,
    val newPassword : String,
    val code: String
)

    : MutableStateFlow<UserLoginRes?> {
    override val subscriptionCount: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val replayCache: List<UserLoginRes?>
        get() = TODO("Not yet implemented")
    override var value: UserLoginRes?
        get() = TODO("Not yet implemented")
        set(value) {}

    override suspend fun collect(collector: FlowCollector<UserLoginRes?>): Nothing {
        TODO("Not yet implemented")
    }

    override fun compareAndSet(expect: UserLoginRes?, update: UserLoginRes?): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun emit(value: UserLoginRes?) {
        TODO("Not yet implemented")
    }

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() {
        TODO("Not yet implemented")
    }

    override fun tryEmit(value: UserLoginRes?): Boolean {
        TODO("Not yet implemented")
    }
}
@Serializable
data class UserRegParams(
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val birthDate: String,
    val address: String,
    val code:String,
)

@Serializable
data class PhoneNumberClass(
    val phoneNumber: String,
    )

@Serializable
data class RegisterRes(
    val token: String,
    val message: String
)