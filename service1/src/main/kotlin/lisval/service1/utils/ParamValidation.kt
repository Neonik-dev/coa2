package lisval.service1.utils

import lisval.service1.exceptions.ValidationException


object ParamValidation {

    fun validate(eq: Int?, lt: Int?, gt: Int?) {
        if (eq != null && (lt != null || gt != null)) {
            throw ValidationException()
        }
        if (gt != null && lt != null && gt <= lt) {
            throw ValidationException()
        }
    }
}