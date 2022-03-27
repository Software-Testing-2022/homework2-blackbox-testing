package at.jku.swtesting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RingBufferTest<Item> {

    private static final int DEFAULT_BUFFER_CAPACITY = 3;

    private RingBuffer<String> ringBuffer;

    @BeforeEach
    void setUp() {
        ringBuffer = new RingBuffer<>(DEFAULT_BUFFER_CAPACITY);
    }

    @Test
    void testRingBufferConstructor() {
        assertNotNull(ringBuffer);
        assertThrows(IllegalArgumentException.class, () -> {
            new RingBuffer<>(0);
        });
    }

    @Test
    void testCapacity() {
        assertEquals(DEFAULT_BUFFER_CAPACITY, ringBuffer.capacity());

		/*
		TODO: Testet der folgende Code-Abschnitt nicht die Methode "size"?
		ringBuffer.enqueue("1");
		ringBuffer.enqueue("2");
		ringBuffer.enqueue("3");
		assertEquals(3, ringBuffer.capacity());*/
    }

    @Test
    void testSize() {
        assertEquals(0, ringBuffer.size());

        ringBuffer.enqueue("a");
        assertEquals(1, ringBuffer.size());

        ringBuffer.dequeue();
        assertEquals(0, ringBuffer.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(ringBuffer.isEmpty());

        ringBuffer.enqueue("a");
        assertFalse(ringBuffer.isEmpty());
    }

    @Test
    void testIsFull() {
        assertFalse(ringBuffer.isFull());

        ringBuffer.enqueue("a");
        ringBuffer.enqueue("b");
        ringBuffer.enqueue("c");
        assertTrue(ringBuffer.isFull());

        ringBuffer.dequeue();
        assertFalse(ringBuffer.isFull());
    }

    @Test
    void testEnqueue() {
        ringBuffer.enqueue("a");
        assertEquals("a", ringBuffer.peek());

        ringBuffer.enqueue("b");
        ringBuffer.enqueue("c");
        ringBuffer.enqueue("d");
        assertEquals("b", ringBuffer.peek());

		/*
		TODO: Testet der folgende Code-Abschnitt nicht die Methode "dequeue"?
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		assertEquals("d", ringBuffer.peek());*/
    }

    @Test
    void testDequeue() {
        ringBuffer.enqueue("a");
        assertEquals("a", ringBuffer.dequeue());

        ringBuffer.enqueue("b");
        ringBuffer.enqueue("c");
        ringBuffer.dequeue();
        assertEquals("c", ringBuffer.dequeue());
    }

    @Test
    void testDequeueWhenBufferIsEmpty() {
        assertThrows(RuntimeException.class, () -> {
            ringBuffer.dequeue();
        });
    }

    @Test
    void testPeek() {
        ringBuffer.enqueue("a");
        assertEquals("a", ringBuffer.peek());

        ringBuffer.enqueue("b");
        assertNotEquals("b", ringBuffer.peek());
        assertEquals("a", ringBuffer.peek());
    }

    @Test
    void testPeekWhenBufferIsEmpty() {
        assertThrows(RuntimeException.class, () -> {
            ringBuffer.peek();
        });
    }
}
