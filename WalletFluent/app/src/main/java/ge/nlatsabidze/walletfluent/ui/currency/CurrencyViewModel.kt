package ge.nlatsabidze.walletfluent.ui.currency

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.nlatsabidze.walletfluent.network.NetworkRepository
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(private val networkRepository: NetworkRepository): ViewModel() {


}