package co.edu.udea.compumovil.gr05_20262.lab2.ui

/**
 * Rutas de navegacion de la app.
 *
 * Maria Camila Berrio Pavas es la responsable de la navegacion de Compose:
 * este archivo es la version base y debe ser reemplazado por su version final.
 */
object Routes {
    const val LIST = "posts_list"
    const val ARG_POST_ID = "postId"
    const val DETAIL = "post_detail/{$ARG_POST_ID}"

    fun detail(postId: Int) = "post_detail/$postId"
}
