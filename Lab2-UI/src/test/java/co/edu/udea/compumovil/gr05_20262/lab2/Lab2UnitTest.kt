package co.edu.udea.compumovil.gr05_20262.lab2

import co.edu.udea.compumovil.gr05_20262.lab2.data.PostEntity
import co.edu.udea.compumovil.gr05_20262.lab2.ui.Routes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas unitarias minimas del laboratorio.
 */
class Lab2UnitTest {

    @Test
    fun `postEntity_conservaLosCamposDelServicioRest`() {
        val post = PostEntity(
            id = 1,
            userId = 7,
            title = "sunt aut facere",
            body = "quia et sus",
            syncedAt = 1000L
        )
        assertEquals(1, post.id)
        assertEquals(7, post.userId)
        assertEquals("sunt aut facere", post.title)
        assertEquals(1000L, post.syncedAt)
    }

    @Test
    fun `rutaDeDetalleIncluyeElIdentificadorDelPost`() {
        val ruta = Routes.detail(42)
        assertEquals("post_detail/42", ruta)
        // La plantilla de la ruta declara el argumento entre llaves.
        assertTrue(Routes.DETAIL.contains("{${Routes.ARG_POST_ID}}"))
    }
}
