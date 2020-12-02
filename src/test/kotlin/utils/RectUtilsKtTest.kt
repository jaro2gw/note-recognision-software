package utils

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.opencv.core.Rect

internal class RectUtilsKtTest {
    @Test
    fun `no intersection at all`() {
        val rect1 = Rect(0, 0, 100, 100)
        val rect2 = Rect(150, 150, 100, 100)

        assertFalse(rect1 intersects rect2)
        assertFalse(rect2 intersects rect1)
    }

    @Test
    fun `only xs intersect`() {
        val rect1 = Rect(0, 0, 100, 100)
        val rect2 = Rect(50, 150, 100, 100)

        assertFalse(rect1 intersects rect2)
        assertFalse(rect2 intersects rect1)
    }

    @Test
    fun `only ys intersect`() {
        val rect1 = Rect(0, 0, 100, 100)
        val rect2 = Rect(50, 150, 100, 100)

        assertFalse(rect1 intersects rect2)
        assertFalse(rect2 intersects rect1)
    }

    @Test
    fun `both intersect`() {
        val rect1 = Rect(0, 0, 100, 100)
        val rect2 = Rect(50, 50, 100, 100)

        assertTrue(rect1 intersects rect2)
        assertTrue(rect2 intersects rect1)
    }

    @Test
    fun `first includes second`() {
        val rect1 = Rect(0, 0, 150, 150)
        val rect2 = Rect(50, 50, 50, 50)

        assertTrue(rect1 intersects rect2)
        assertTrue(rect2 intersects rect1)
    }
}