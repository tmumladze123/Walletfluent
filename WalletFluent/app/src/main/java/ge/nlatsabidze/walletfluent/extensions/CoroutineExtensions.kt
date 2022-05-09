package ge.nlatsabidze.walletfluent.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


fun CoroutineScope.launchPeriodicAsync(repeatMillis: Long, action: () -> Unit) = this.async {
    while (isActive) {
        action()
        delay(repeatMillis)
    }
}

fun <T> Fragment.collectFlow(flow: Flow<T>, onCollect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        flow.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collectLatest(onCollect)
    }
}

fun <T> ViewModel.collectInViewModel(flow: Flow<T>, onCollect: suspend (T) -> Unit) {
    viewModelScope.launch {
        flow.collectLatest(onCollect)
    }
}

suspend fun <T> collect(flow: Flow<T>, onCollect: suspend (T) -> Unit) {
    flow.collectLatest(onCollect)
}