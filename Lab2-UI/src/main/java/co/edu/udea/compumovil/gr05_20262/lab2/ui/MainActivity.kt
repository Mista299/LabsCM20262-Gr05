package co.edu.udea.compumovil.gr05_20262.lab2.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import co.edu.udea.compumovil.gr05_20262.lab2.ui.theme.Lab2Theme

/**
 * Actividad unica de la aplicacion.
 *
 * Extiende de AppCompatActivity para que el cambio de idioma con
 * AppCompatDelegate funcione tambien en versiones anteriores a Android 13.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}
