package com.clint.flags.core.exception

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
}