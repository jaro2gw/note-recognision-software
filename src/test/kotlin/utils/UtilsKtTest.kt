package utils

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class UtilsKtTest {
    @Test
    fun `should not find intersection`() {
        val xs1 = 0 until 10
        val xs2 =  15 until 30

        assertFalse(xs1 intersects xs2)
        assertFalse(xs2 intersects xs1)
    }

    @Test
    fun `boundary intersection case`() {
        val xs1 = 0..10
        val xs2 = 10..15

        assertTrue(xs1 intersects xs2)
        assertTrue(xs2 intersects xs1)
    }

    @Test
    fun `partial intersection`() {
        val xs1 = 0 until 15
        val xs2 = 7 until 30

        assertTrue(xs1 intersects xs2)
        assertTrue(xs2 intersects xs1)
    }

    @Test
    fun `range includes another`() {
        val xs1 = 0 until 30
        val xs2 = 7 until 15

        assertTrue(xs1 intersects xs2)
        assertTrue(xs2 intersects xs1)
    }
}