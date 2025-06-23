package com.lightning.androidfrontend.ui.screens.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lightning.androidfrontend.data.model.CheckCodeParams
import com.lightning.androidfrontend.data.model.PostVirtualRes
import com.lightning.androidfrontend.data.model.RegisterRes
import com.lightning.androidfrontend.data.model.RestPasswordParams
import com.lightning.androidfrontend.data.model.UserLoginParams
import com.lightning.androidfrontend.data.model.UserLoginRes
import com.lightning.androidfrontend.data.model.UserRegParams
import com.lightning.androidfrontend.data.repository.UserRepository.VisitorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String = "Login Successful") : LoginState()
    data class Error(val error: String) : LoginState()
}
sealed class ConfirmationState {
    object Idle : ConfirmationState()
    object Loading : ConfirmationState()
    data class Success(val message: String = "Login Successful") : ConfirmationState()
    data class Error(val error: String) : ConfirmationState()
}
sealed class RestConfirmationState {
    object Idle : RestConfirmationState()
    object Loading : RestConfirmationState()
    data class Success(val message: String = "Login Successful") : RestConfirmationState()
    data class Error(val error: String) : RestConfirmationState()
}
sealed class CheckCodeValidityState {
    object Idle : CheckCodeValidityState()
    object Loading : CheckCodeValidityState()
    data class Success(val message: String = "Login Successful") : CheckCodeValidityState()
    data class Error(val error: String) : CheckCodeValidityState()
}
sealed class RestPasswordState {
    object Idle : RestPasswordState()
    object Loading : RestPasswordState()
    data class Success(val message: String = "Login Successful") : RestPasswordState()
    data class Error(val error: String) : RestPasswordState()
}
sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val message: String = "Login Successful") : RegisterState()
    data class Error(val error: String) : RegisterState()
}
class AuthViewModel(private val visitorRepository: VisitorRepository) : ViewModel() {

    private val _loginResponse = MutableStateFlow<UserLoginRes?>(null)
    var loginResponse = _loginResponse
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState


    fun login(phoneNumber: String, password: String) {
        val params = UserLoginParams(phoneNumber, password)
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                val res = visitorRepository.login(params)
                if(res != null) {

                    _loginResponse.value = res
                    _loginState.value = LoginState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //------------------------------------------------------------------------------------


    private val _confirmationResponse = MutableStateFlow<PostVirtualRes?>(null)
    var confirmationResponse = _confirmationResponse
    private val _confirmationState = MutableStateFlow<ConfirmationState>(ConfirmationState.Idle)
    val confirmationState: StateFlow<ConfirmationState> = _confirmationState


    fun confirmation(phoneNumber: String) {
        viewModelScope.launch {
            _confirmationState.value = ConfirmationState.Loading

            try {
                val res =visitorRepository.confirmation(phoneNumber)
                if(res != null) {

                    _confirmationResponse.value = res
                _confirmationState.value = ConfirmationState.Success()
                Log.d("debugTag", "done")
            }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _confirmationState.value = ConfirmationState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //------------------------------------------------------------------------------------


    private val _restConfirmationResponse = MutableStateFlow<PostVirtualRes?>(null)
    var restConfirmationResponse = _restConfirmationResponse
    private val _restConfirmationState =
        MutableStateFlow<RestConfirmationState>(RestConfirmationState.Idle)
    val restConfirmationState: StateFlow<RestConfirmationState> = _restConfirmationState


    fun restconfirmation(phoneNumber: String) {
        viewModelScope.launch {
            _restConfirmationState.value = RestConfirmationState.Loading

            try {
                val res =visitorRepository.restConfirmation(phoneNumber)
                if(res != null) {

                    _confirmationResponse.value =res
                        _restConfirmationState.value = RestConfirmationState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _restConfirmationState.value = RestConfirmationState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------


    private val _checkCodeValidityResponse = MutableStateFlow<PostVirtualRes?>(null)
    var checkCodeValidityResponse = _checkCodeValidityResponse
    private val _checkCodeValidityState =
        MutableStateFlow<CheckCodeValidityState>(CheckCodeValidityState.Idle)
    val checkCodeValidityState: StateFlow<CheckCodeValidityState> =
        _checkCodeValidityState


    fun checkCodeValidity(phoneNumber: String, code: String) {
        val params = CheckCodeParams(phoneNumber, code)

        viewModelScope.launch {
            _checkCodeValidityState.value = CheckCodeValidityState.Loading

            try {
                val res =visitorRepository.checkCodeValidity(params)

                if(res != null) {
                    _checkCodeValidityResponse.value = res
                    _checkCodeValidityState.value = CheckCodeValidityState.Success()
                    Log.d("debugTag", "done${_checkCodeValidityResponse.value!!.message}")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _checkCodeValidityState.value = CheckCodeValidityState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //------------------------------------------------------------------------------------


    private val _restPasswordResponse = MutableStateFlow<PostVirtualRes?>(null)
    var restPasswordResponse = _restPasswordResponse


    private val _restPasswordState = MutableStateFlow<RestPasswordState>(RestPasswordState.Idle)
    val restPasswordState: StateFlow<RestPasswordState> = _restPasswordState


    fun restPassword(phoneNumber: String, password: String, code: String) {
        val params = RestPasswordParams(phoneNumber, password, code)

        viewModelScope.launch {
            _restPasswordState.value = RestPasswordState.Loading

            try {
                val res = visitorRepository.restPassword(params)
                if(res != null) {
                    _confirmationResponse.value = res
                    _restPasswordState.value = RestPasswordState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _restPasswordState.value = RestPasswordState.Error(e.message ?: "Unknown error")
            }
        }
    }

    //------------------------------------------------------------------------------------


    private val _registerResponse = MutableStateFlow<RegisterRes?>(null)
    var registerResponse = _registerResponse


    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState


    fun register(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String,
        birthDate: String,
        address: String,
        code: String,
    ) {
        val params = UserRegParams(fullName, phoneNumber, email, password, birthDate, address, code)

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            try {
                val res = visitorRepository.register(params)
                if(res != null) {
                    _registerResponse.value = res

                    _registerState.value = RegisterState.Success()
                    Log.d("debugTag", "done")
                }
            } catch (e: Exception) {
                Log.e("debugTag", "$e")

                _registerState.value = RegisterState.Error(e.message ?: "Unknown error")
            }
        }
    }

//------------------------------------------------------------------------------------


}

