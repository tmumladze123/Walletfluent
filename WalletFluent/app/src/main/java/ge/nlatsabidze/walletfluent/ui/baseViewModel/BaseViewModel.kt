package ge.nlatsabidze.walletfluent.ui.baseViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ge.nlatsabidze.walletfluent.util.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

open class BaseViewModel @Inject constructor(private val dispatchers: Dispatchers): ViewModel() {

    private var _showLoadingViewModelState = MutableStateFlow<Boolean>(false)
    val showLoadingViewModel: MutableStateFlow<Boolean> get() = _showLoadingViewModelState

    fun <T> coroutineIoLauncher(block: suspend () -> T) {
        dispatchers.launchBackground(viewModelScope) {
            block.invoke()
        }
    }

    fun <T> coroutineMainLauncher(block: suspend () -> T) {
        dispatchers.launchUI(viewModelScope) {
            block.invoke()
        }
    }

    fun <T> coroutineDefaultLauncher(block: suspend () -> T) {
        dispatchers.launchDefault(viewModelScope) {
            block.invoke()
        }
    }
}