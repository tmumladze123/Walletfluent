package ge.nlatsabidze.walletfluent

import android.content.Context
import android.os.Bundle
import android.util.Log.d
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ge.nlatsabidze.walletfluent.checkConnectivity.CheckInternetConnection
import ge.nlatsabidze.walletfluent.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var nav_Menu: Menu

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var checkInternetConnection: CheckInternetConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.run {
            findNavController(R.id.nav_host_fragment_content_main).addOnDestinationChangedListener(
                this@MainActivity
            )
        }

        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar?.hide();

        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        nav_Menu = navView.getMenu()

        val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.languageFragment,
                R.id.cryptoFragment,
                R.id.currencyFragment,
                R.id.loginFragment
            ), drawerLayout
        )

        nav_Menu.findItem(R.id.languageFragment).setVisible(false)

        if (FirebaseAuth.getInstance().currentUser != null && checkInternetConnection.isOnline(
                applicationContext
            ) && FirebaseAuth.getInstance().currentUser?.isEmailVerified == true
        ) {
            navGraph.startDestination = R.id.accountSettings
            setUnVisible()
        } else {
            navGraph.startDestination = R.id.loginFragment
        }
        navController.graph = navGraph

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

    fun setDisableToDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun setEnableToDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun setUnVisible() {
        nav_Menu.findItem(R.id.loginFragment).setVisible(false)
    }

    fun setVisible() {
        nav_Menu.findItem(R.id.loginFragment).setVisible(true)
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.accountSettings -> {
                setEnableToDrawer()
            }
        }
    }


}