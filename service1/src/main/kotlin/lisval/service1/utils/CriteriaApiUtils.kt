package lisval.service1.utils

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Order
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

object CriteriaApiUtils {

    fun <T, P> generatePredicate(
        builder: CriteriaBuilder,
        root: Root<P>,
        value: T?,
        fieldName: String
    ): Predicate? {
        return value?.let {
            builder.equal(
                root.get<T>(fieldName),
                value
            )
        }
    }

    fun <T> generateSortPredicates(
        builder: CriteriaBuilder,
        root: Root<T>,
        sort: String?
    ): List<Order> {
        sort ?: return emptyList()

        return sort
            .trim()
            .split(",")
            .mapNotNull {
                val fields = it.split(":")
                val name = root.get<Any>(fields[0])
                when {
                    fields.size == 1 || (fields.size == 2 && fields[1] == "asc") -> {
                        builder.asc(name)
                    }
                    fields.size == 2 && fields[1] == "desc" -> {
                        builder.desc(name)
                    }
                    else -> null
                }
            }
    }
}