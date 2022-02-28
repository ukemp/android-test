package de.kempmobil.test

import org.junit.BeforeClass
import timber.log.Timber

/**
 * JUnit tests should inherit from this class which enables Timber logging to `System.out`.
 */
abstract class BaseTest {

    companion object {

        @BeforeClass
        @JvmStatic
        fun initTimber() {
            Timber.plant(ConsoleTree())
        }
    }
}