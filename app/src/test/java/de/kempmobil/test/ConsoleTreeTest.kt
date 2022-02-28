package de.kempmobil.test

import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
class ConsoleTreeTest : TestCase() {

    @Test
    fun `validate output format`() {
        val verbose = captureLogMessageOf {
            Timber.v("Verbose message")
        }
        assertThat(verbose).endsWith("Verbose message")
        assertThat(verbose).containsMatch("[0-9]+ V/")


        val debug = captureLogMessageOf {
            Timber.d("Debug message")
        }
        assertThat(debug).endsWith("Debug message")
        assertThat(debug).containsMatch("[0-9]+ D/")


        val info = captureLogMessageOf {
            Timber.i("Info message")
        }
        assertThat(info).endsWith("Info message")
        assertThat(info).containsMatch("[0-9]+ I/")


        val warn = captureLogMessageOf {
            Timber.w("Warn message")
        }
        assertThat(warn).endsWith("Warn message")
        assertThat(warn).containsMatch("[0-9]+ W/")


        val error = captureLogMessageOf {
            Timber.e("Error message")
        }
        assertThat(error).endsWith("Error message")
        assertThat(error).containsMatch("[0-9]+ E/")
    }

    private fun captureLogMessageOf(block: () -> Unit): String {
        val stream = ByteArrayOutputStream().apply {  }
        val tree = ConsoleTree(PrintStream(stream, true))
        Timber.plant(tree)

        block()
        Timber.uproot(tree)
        return stream.toString().lines()[0]
    }
}