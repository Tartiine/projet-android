package com.example.ensihub.mainClasses

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
): ViewModel() {
    val loginUiState: MutableState<LoginUiState> = mutableStateOf(LoginUiState())
    val currentUser = repository.currentUser
    val userName: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")
    private val _isLoggedIn = MutableStateFlow(false)

    private val db = Firebase.firestore

    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn
    init {
        _isLoggedIn.value = repository.hasUser()
    }

    fun updateStatusLogin(isLoggedIn: Boolean) {
        _isLoggedIn.value = isLoggedIn
    }

    val hasUser:Boolean
        get() = repository.hasUser()

    fun onEmailChange(newUserName: String) {
        Log.d("LoginViewModel", "onUserNameChange called with: $newUserName")
        loginUiState.value = loginUiState.value.copy(eMail = newUserName)
    }

    fun onPasswordChange(password:String){
        Log.d("LoginViewModel", "onPasswordChange called with: $password")
        loginUiState.value = loginUiState.value.copy(password = password)
    }

    fun onEmailSignUpChange(userNameSignUp:String){
        Log.d("LoginViewModel", "onUserNameSignUpChange called with: $userNameSignUp")
        loginUiState.value = loginUiState.value.copy(eMailSignUp = userNameSignUp)
    }

    fun onPasswordSignUpChange(passwordSignUp:String){
        Log.d("LoginViewModel", "onPasswordSignUpChange called with: $passwordSignUp")
        loginUiState.value = loginUiState.value.copy(passwordSignUp = passwordSignUp)
    }

    fun onConfirmPasswordSignUpChange(confirmPasswordSignUp:String){
        Log.d("LoginViewModel", "onConfirmPasswordSignUpChange called with: $confirmPasswordSignUp")
        loginUiState.value = loginUiState.value.copy(confirmPasswordSignUp = confirmPasswordSignUp)
    }

    fun onUserNameChange(newUserName: String) {
        Log.d("LoginViewModel", "onUserNameChange called with: $newUserName")
        loginUiState.value = loginUiState.value.copy(userName = newUserName)
    }

    private fun validateLoginForm(): Boolean {
        Log.d("LoginViewModel", "validateLoginForm called.")
        return loginUiState.value.eMail.isNotBlank() &&
                loginUiState.value.password.isNotBlank()
    }

    private fun validateSignUpForm(): Boolean {
        Log.d("LoginViewModel", "validateSignUpForm called.")
        return loginUiState.value.eMailSignUp.isBlank() &&
                loginUiState.value.passwordSignUp.isBlank() &&
                loginUiState.value.confirmPasswordSignUp.isBlank()
    }


    fun resetPassword(context: Context) = viewModelScope.launch{
        Log.d("LoginViewModel", "resetPassword called.")
        try{
            if(loginUiState.value.eMail.isBlank()){
                throw Exception("Please fill in your email")
            }
            loginUiState.value = loginUiState.value.copy(isLoading = true)
            repository.resetPassword(loginUiState.value.eMail)
            Toast.makeText(context, "Please check your email !", Toast.LENGTH_SHORT).show()
            loginUiState.value = loginUiState.value.copy(isLoading = false)
        }catch (e:Exception){
            loginUiState.value = loginUiState.value.copy(isLoading = false)
            loginUiState.value = loginUiState.value.copy(resetPasswordError = e.message)
        }
    }

    fun createUser(context: Context) = viewModelScope.launch{
            Log.d("LoginViewModel", "createUser called.")
        try{
            if(validateSignUpForm()){
                throw Exception("Please fill in all the fields")
            }
            loginUiState.value = loginUiState.value.copy(isLoading = true)
            if(loginUiState.value.passwordSignUp != loginUiState.value.confirmPasswordSignUp){
                throw Exception("Passwords do not match")
            }
            loginUiState.value = loginUiState.value.copy(signUpError = null)
            repository.createUser(
                loginUiState.value.userName,
                loginUiState.value.eMailSignUp + "@uha.fr",
                loginUiState.value.passwordSignUp
            ){ isSuccessful ->
                if(isSuccessful){
                    Toast.makeText(context, "Success Login", Toast.LENGTH_SHORT).show()
                    loginUiState.value = loginUiState.value.copy(isSuccessLogin = true)
                }else{
                    Toast.makeText(context, "Failed Login", Toast.LENGTH_SHORT).show()
                    loginUiState.value = loginUiState.value.copy(isSuccessLogin = false)
                }
            }

        } catch (e:Exception){
            loginUiState.value = loginUiState.value.copy(signUpError = e.localizedMessage)
        } finally {
            loginUiState.value = loginUiState.value.copy(isLoading = false)
        }
    }

    fun loginUser(context: Context) = viewModelScope.launch{
        Log.d("LoginViewModel", "loginUser called.")
        try{
            if(!validateLoginForm()){
                throw Exception("Please fill in all the fields")
            }
            loginUiState.value = loginUiState.value.copy(isLoading = true)
            loginUiState.value = loginUiState.value.copy(loginError = null)
            repository.login(
                loginUiState.value.eMail + "@uha.fr",
                loginUiState.value.password
            ){ isSuccessful->
                if(isSuccessful){
                    Toast.makeText(context, "Success Login", Toast.LENGTH_SHORT).show()
                    loginUiState.value = loginUiState.value.copy(isSuccessLogin = true)
                    _isLoggedIn.value = true

                }else{
                    Toast.makeText(context, "Failed Login", Toast.LENGTH_SHORT).show()
                    loginUiState.value = loginUiState.value.copy(isSuccessLogin = false)
                }
            }

        } catch (e:Exception){
            loginUiState.value = loginUiState.value.copy(loginError = e.localizedMessage)
        } finally {
            loginUiState.value = loginUiState.value.copy(isLoading = false)
        }
    }

    fun sendUser(context: Context) = viewModelScope.launch{
        Log.d("sendUser", "User sent to repository.")
        User(
            id = repository.getUserId(),
            username = loginUiState.value.userName,
            email = loginUiState.value.eMail,
            role = Role.USER
        )
    }





}

data class LoginUiState(
    var userName:String = "",
    var eMail:String = "",
    val password:String = "",
    var eMailSignUp:String = "",
    val passwordSignUp:String = "",
    val confirmPasswordSignUp:String = "",
    val isLoading:Boolean = false,
    val isSuccessLogin:Boolean = false,
    val signUpError:String? = null,
    val loginError:String? = null,
    val resetPasswordError:String? = null
)