package lisval.service1.dto

data class PageWrapper<T>(
    val data: List<T>,
    val currentPage: Int,
    val totalPage: Int,
)