package id.ac.unpas.blogging.ui.navigation

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"

    const val POST_DETAIL_PREFIX = "post_detail"
    const val POST_DETAIL = "$POST_DETAIL_PREFIX/{postId}"
    fun postDetailRoute(postId: String) = "$POST_DETAIL_PREFIX/$postId"

    const val CREATE_POST = "create_post"

    const val EDIT_POST_PREFIX = "edit_post"
    const val EDIT_POST = "$EDIT_POST_PREFIX/{postId}"
    fun editPostRoute(postId: String) = "$EDIT_POST_PREFIX/$postId"
}