package com.example.ensihub.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ensihub.data.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
): ViewModel() {
    val currentUser = repository.currentUser

    val hasUser:Boolean
        get() = repository.hasUser()

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun onUserNameChange(userName:String){
        loginUiState = loginUiState.copy(userName = userName)
    }

    fun onPasswordChange(password:String){
        loginUiState = loginUiState.copy(password = password)
    }

    fun onUserNameSignUpChange(userNameSignUp:String){
        loginUiState = loginUiState.copy(userNameSignUp = userNameSignUp)
    }

    fun onPasswordSignUpChange(passwordSignUp:String){
        loginUiState = loginUiState.copy(passwordSignUp = passwordSignUp)
    }

    fun onConfirmPasswordSignUpChange(confirmPasswordSignUp:String){
        loginUiState = loginUiState.copy(confirmPasswordSignUp = confirmPasswordSignUp)
    }

    private fun validateLoginForm() =
        loginUiState.userName.isBlank() &&
                loginUiState.password.isBlank()

    private fun validateSignUpForm() =
        loginUiState.userNameSignUp.isBlank() &&
                loginUiState.passwordSignUp.isBlank() &&
                loginUiState.confirmPasswordSignUp.isBlank()

    fun createUser(context: Context) = viewModelScope.launch{
        try{
            if(!validateSignUpForm()){
                throw Exception("Please fill in all the fields")
            }
            loginUiState = loginUiState.copy(isLoading = true)
            if(loginUiState.passwordSignUp != loginUiState.confirmPasswordSignUp){
                throw Exception("Passwords do not match")
            }
            loginUiState = loginUiState.copy(signUpError = null)
            repository.createUser(
                loginUiState.userNameSignUp,
                loginUiState.passwordSignUp
            ){ isSuccessful ->
                if(isSuccessful){
                    Toast.makeText(context, "Success Login", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)

                }else{
                    Toast.makeText(context, "Failed Login", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }

        } catch (e:Exception){
            loginUiState = loginUiState.copy(signUpError = e.localizedMessage)
        } finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    fun loginUser(context: Context) = viewModelScope.launch{
        try{
            if(!validateLoginForm()){
                throw Exception("Please fill in all the fields")
            }
            loginUiState = loginUiState.copy(isLoading = true)
            loginUiState = loginUiState.copy(loginError = null)
            repository.login(
                loginUiState.userName,
                loginUiState.password
            ){ isSuccessful ->
                if(isSuccessful){
                    Toast.makeText(context, "Success Login", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = true)

                }else{
                    Toast.makeText(context, "Failed Login", Toast.LENGTH_SHORT).show()
                    loginUiState = loginUiState.copy(isSuccessLogin = false)
                }
            }

        } catch (e:Exception){
            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
        } finally {
            loginUiState = loginUiState.copy(isLoading = false)
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