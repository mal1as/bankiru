package com.itmo.blps.bankiru.utils

class RequestHeadersUtils {
    companion object {

        fun getAuthTokenWithoutPrefix(token: String): String {
            return token.substring(token.indexOf(" ") + 1)
        }
    }
}