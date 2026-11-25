package co.edu.udea.compumovil.gr05_20262.lab2.ui

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.edu.udea.compumovil.gr05_20262.lab2.R
import java.util.Locale

/**
 * Idiomas disponibles en la aplicacion.
 */
enum class AppLanguage(val tag: String) {
    SYSTEM(""),
    SPANISH("es"),
    ENGLISH("en");

    companion object {
        /** Idioma que la aplicacion tiene activo en este momento. */
        fun current(context: Context): AppLanguage {
            val locale = AppCompatDelegate.getApplicationLocales()
                .takeIf { !it.isEmpty }
                ?.get(0)
                ?: return SYSTEM
            return when (locale.language) {
                "es" -> SPANISH
                "en" -> ENGLISH
                else -> SYSTEM
            }
        }
    }
}

/**
 * Boton que abre el selector de idioma de la aplicacion.
 *
 * En Android 13 y superiores se usa la API de localisation por aplicacion del
 * sistema. En versiones anteriores se recurre a AppCompat, que aplica el idioma
 * mediante el contexto de la actividad.
 */
@Composable
fun LanguageSelectorButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }, modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.Language,
            contentDescription = stringResource(R.string.action_language)
        )
    }

    if (showDialog) {
        LanguageDialog(
            current = AppLanguage.current(context),
            onDismiss = { showDialog = false },
            onSelect = { language ->
                showDialog = false
                applyLanguage(context, language)
            }
        )
    }
}

@Composable
private fun LanguageDialog(
    current: AppLanguage,
    onDismiss: () -> Unit,
    onSelect: (AppLanguage) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.language_dialog_title)) },
        text = {
            Column {
                LanguageOption(
                    label = stringResource(R.string.language_system),
                    selected = current == AppLanguage.SYSTEM,
                    onClick = { onSelect(AppLanguage.SYSTEM) }
                )
                LanguageOption(
                    label = stringResource(R.string.language_es),
                    selected = current == AppLanguage.SPANISH,
                    onClick = { onSelect(AppLanguage.SPANISH) }
                )
                LanguageOption(
                    label = stringResource(R.string.language_en),
                    selected = current == AppLanguage.ENGLISH,
                    onClick = { onSelect(AppLanguage.ENGLISH) }
                )
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.action_back),
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
private fun LanguageOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Aplica el idioma elegido a la aplicacion.
 */
fun applyLanguage(context: Context, language: AppLanguage) {
    val locales = if (language == AppLanguage.SYSTEM) {
        LocaleListCompat.getEmptyLocaleList()
    } else {
        LocaleListCompat.forLanguageTags(language.tag)
    }
    AppCompatDelegate.setApplicationLocales(locales)
}
