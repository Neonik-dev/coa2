package lisval.service1.utils

import lisval.service1.exceptions.ValidationException


object ParamValidation {

    fun <T : Comparable<T>> validate(eq: T?, lt: T?, gt: T?) {
        if (eq != null && (lt != null || gt != null)) {
            throw ValidationException()
        }
        if (gt != null && lt != null && gt >= lt) {
            throw ValidationException()
        }
    }
}