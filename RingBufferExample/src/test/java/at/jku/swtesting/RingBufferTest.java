package at.jku.swtesting;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RingBufferTest<Item> {

	RingBuffer<String> a;
	@BeforeEach
	void setUp() {
		a = new RingBuffer<>(3);
	}

	@Test
	void testRingBufferConstructor() {
		assertNotNull(a);
		assertThrows(IllegalArgumentException.class, () -> { new RingBuffer<>(0);});
	}

	@Test
	void testCapacity() {
		assertEquals(3, a.capacity());
		a.enqueue("1");
		a.enqueue("2");
		a.enqueue("3");
		assertEquals(a.size(), a.capacity());
	}

	@Test
	void testSize() {
		a.enqueue("S");
		assertEquals(1, a.size());
		a.dequeue();
		assertEquals(0, a.size());
	}

	@Test
	void testIsEmpty() {
		assertTrue(a.isEmpty());
		a.enqueue("a");
		assertFalse(a.isEmpty());
	}

	@Test
	void testIsFull() {
		a.enqueue("1");
		a.enqueue("2");
		a.enqueue("3");
		assertTrue(a.isFull());
		a.dequeue();
		assertFalse(a.isFull());
	}

	@Test
	void testEnqueue() {
		a.enqueue("a");
		assertEquals("a", a.peek());
		a.enqueue("b");
		a.enqueue("c");
		a.enqueue("d");
		assertEquals("b", a.peek());
		a.dequeue();
		a.dequeue();
		assertEquals("d", a.peek());
	}

	@Test
	void testDequeue() {
		a = null;
		assertThrows(RuntimeException.class , () -> {a.dequeue();});

		a = new RingBuffer<>(3);
		a.enqueue("1");
		assertEquals("1" , a.dequeue());
		a.enqueue("2");
		a.enqueue("3");
		a.dequeue();
		assertEquals("3" , a.dequeue());
	}

	@Test
	void testPeek() {
		a = null;
		assertThrows(RuntimeException.class , () -> {a.peek();});

		a = new RingBuffer<>(3);
		a.enqueue("a");
		assertEquals("a", a.peek());
		a.enqueue("b");
		assertNotEquals("b", a.peek());
	}

	@Test
	void testIterator() {
	}
}
