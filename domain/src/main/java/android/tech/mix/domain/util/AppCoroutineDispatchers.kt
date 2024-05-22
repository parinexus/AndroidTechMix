package android.tech.mix.domain.util

import kotlinx.coroutines.CoroutineDispatcher

class AppCoroutineDispatchers(
    val mainDispatcher: CoroutineDispatcher,
    val ioDispatcher: CoroutineDispatcher,
    val defaultDispatcher: CoroutineDispatcher
)