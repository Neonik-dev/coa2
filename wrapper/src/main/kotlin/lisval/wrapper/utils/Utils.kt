package lisval.wrapper.utils

import org.springframework.http.HttpHeaders

fun copyHeaders(originalHeaders: HttpHeaders): HttpHeaders {
    val headers = HttpHeaders()
    headers.addAll(originalHeaders)

    headers.remove(HttpHeaders.HOST)
    headers.remove(HttpHeaders.CONNECTION)
    headers.remove("Accept-Encoding")

    headers.set(HttpHeaders.ACCEPT, "application/xml")
    return headers
}