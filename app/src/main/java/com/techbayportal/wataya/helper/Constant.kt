package com.techbayportal.wataya.helper

object Constant {

    @JvmField
    var headerName = "Content-Type"
    @JvmField
    var headerValue = "application/json"

    @JvmField
    var PACKAGE_NAME: String = "com.techbayportal.wataya"

    const val MAX_RETRIES: Int = 5

    const val RETRY_DELAY: Int = 5000

    const val CONN_TIMEOUT: Long = 60

    const val READ_TIMEOUT: Long = 40
}
