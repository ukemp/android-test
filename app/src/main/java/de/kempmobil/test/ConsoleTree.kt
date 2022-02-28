package de.kempmobil.test

import timber.log.Timber
import java.io.PrintStream
import java.util.regex.Pattern

/**
 * An implementation of [timber.log.Timber.Tree] for logging to `System.out`.
 */
class ConsoleTree(private val out: PrintStream = System.out) : Timber.Tree() {

    private val start = System.currentTimeMillis()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        out.println((System.currentTimeMillis() - start).toString() + " " + priorityText(priority)
                + "/" + safeTag(tag) + ": " + message)
        t?.printStackTrace(out)
    }

    private fun safeTag(tag: String?) : String {
        return if (tag == null) {
            val stackTrace = Thread.currentThread().stackTrace
            check(stackTrace.size > CALL_STACK_INDEX) { "Synthetic stacktrace didn't have enough elements." }
            createStackElementTag(stackTrace[CALL_STACK_INDEX])
        } else {
            tag
        }
    }

    companion object {

        /** Copied from `android.util.Log`.  */
        private const val VERBOSE = 2

        /** Copied from `android.util.Log`.  */
        private const val DEBUG = 3

        /** Copied from `android.util.Log`.  */
        private const val INFO = 4

        /** Copied from `android.util.Log`.  */
        private const val WARN = 5

        /** Copied from `android.util.Log`.  */
        private const val ERROR = 6

        /** Copied from `android.util.Log`.  */
        private const val ASSERT = 7

        private const val CALL_STACK_INDEX = 7

        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

        /**
         * Extract the tag which should be used for the message from the `element`. By default
         * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
         * becomes `Foo`).
         */
        fun createStackElementTag(element: StackTraceElement): String {
            var tag = element.className
            val m = ANONYMOUS_CLASS.matcher(tag)
            if (m.find()) {
                tag = m.replaceAll("")
            }
            return tag.substring(tag.lastIndexOf('.') + 1)
        }

        fun priorityText(priority: Int): Char {
            return when (priority) {
                ERROR -> 'E'
                WARN -> 'W'
                INFO -> 'I'
                ASSERT -> 'A'
                DEBUG -> 'D'
                VERBOSE -> 'V'
                else -> '?'
            }
        }
    }
}