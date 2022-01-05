package ge.nlatsabidze.walletfluent

import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var settingsManager: SettingsManager
    private var isDarkMode = true

    private val Context.dataStore by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.appBarMain.toolbar)

        settingsManager = SettingsManager(applicationContext)
        observeUiPreferences()

        binding.switchView.setOnClickListener {
            setUpUi()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.languageFragment,
                R.id.cryptoFragment,
                R.id.currencyFragment,
                R.id.loginFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setUpUi() {
        lifecycleScope.launch {
            when (isDarkMode) {
                true -> settingsManager.setUpUiMode(UiMode.LIGHT)
                false -> settingsManager.setUpUiMode(UiMode.DARK)
            }
        }
    }

    private fun observeUiPreferences() {
        lifecycleScope.launch {
            settingsManager.uiModeFlow.collect {
                it.let {
                    when (it) {
                        UiMode.LIGHT -> onLightMode()
                        UiMode.DARK -> onDarkMode()
                    }
                }
            }
        }
    }

    private fun onLightMode() {
        isDarkMode = false
        binding.switchView.isChecked = false
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun onDarkMode() {
        isDarkMode = true
        binding.switchView.isChecked = true
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}