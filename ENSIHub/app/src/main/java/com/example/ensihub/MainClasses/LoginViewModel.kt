package com.example.ensihub.MainClasses

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ensihub.MainClasses.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
): ViewModel() {
    val loginUiState: MutableState<LoginUiState> = mutableStateOf(LoginUiState())
    val currentUser = repository.currentUser
    val userName: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")
    val hasUser:Boolean
        get() = repository.hasUser()

    fun onUserNameChange(newUserName: String) {
        Log.d("LoginViewModel", "onUserNameChange called with: $newUserName")
        loginUiState.value = loginUiState.value.copy(userName = newUserName)
    }

    fun onPasswordChange(password:String){
        Log.d("LoginViewModel", "onPasswordChange called with: $password")
        loginUiState.value = loginUiState.value.copy(password = password)
    }

    fun onUserNameSignUpChange(userNameSignUp:String){
        Log.d("LoginViewModel", "onUserNameSignUpChange called with: $userNameSignUp")
        loginUiState.value = loginUiState.value.copy(userNameSignUp = userNameSignUp)
    }

    fun onPasswordSignUpChange(passwordSignUp:String){
        Log.d("LoginViewModel", "onPasswordSignUpChange called with: $passwordSignUp")
        loginUiState.value = loginUiState.value.copy(passwordSignUp = passwordSignUp)
    }

    fun onConfirmPasswordSignUpChange(confirmPasswordSignUp:String){
        Log.d("LoginViewModel", "onConfirmPasswordSignUpChange called with: $confirmPasswordSignUp")
        loginUiState.value = loginUiState.value.copy(confirmPasswordSignUp = confirmPasswordSignUp)
    }

    private fun validateLoginForm(): Boolean {
        Log.d("LoginViewModel", "validateLoginForm called.")
        return loginUiState.value.userName.isNotBlank() &&
                loginUiState.value.password.isNotBlank()
    }

    private fun validateSignUpForm(): Boolean {
        Log.d("LoginViewModel", "validateSignUpForm called.")
        return loginUiState.value.userNameSignUp.isBlank() &&
                loginUiState.value.passwordSignUp.isBlank() &&
                loginUiState.value.confirmPasswordSignUp.isBlank()
    }

    fun createUser(context: Context) = viewModelScope.launch{
            Log.d("LoginViewModel", "createUser called.")
        try{
            if(!validateSignUpForm()){
                throw Exception("Please fill in all the fields")
            }
            loginUiState.value = loginUiState.value.copy(isLoading = true)
            if(loginUiState.value.passwordSignUp != loginUiState.value.confirmPasswordSignUp){
                throw Exception("Passwords do not match")
            }
            loginUiState.value = loginUiState.value.copy(signUpError = null)
            repository.createUser(
                loginUiState.value.userNameSignUp,
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
                loginUiState.value.userName,
                loginUiState.value.password
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
            loginUiState.value = loginUiState.value.copy(loginError = e.localizedMessage)
        } finally {
            loginUiState.value = loginUiState.value.copy(isLoading = false)
        }
    }



}

data class LoginUiState(
    val userName:String = "",
    val password:String = "",
    val userNameSignUp:String = "",
    val passwordSignUp:String = "",
    val confirmPasswordSignUp:String = "",
    val isLoading:Boolean = false,
    val isSuccessLogin:Boolean = false,
    val signUpError:String? = null,
    val loginError:String? = null,
)