package co.edu.udea.compumovil.gr05_20262.lab2.ui

/**
 * Rutas de navegacion de la app.
 */
object Routes {
    const val LIST = "posts_list"
    const val ARG_POST_ID = "postId"
    const val DETAIL = "post_detail/{$ARG_POST_ID}"

    fun detail(postId: Int) = "post_detail/$postId"
}
