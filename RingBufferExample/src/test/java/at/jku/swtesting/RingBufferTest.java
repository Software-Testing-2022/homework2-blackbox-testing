package at.jku.swtesting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

class RingBufferTest<I> {

	private static final int RING_BUFFER_CAPACITY = 3;
	private RingBuffer<String> ringBuffer;

	@BeforeEach
	void setUp() {
		ringBuffer = new RingBuffer<>(RING_BUFFER_CAPACITY);
	}

	@Test
	void testRingBufferConstructor() {
		assertNotNull(ringBuffer);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			new RingBuffer<>(0);
		});
		assertEquals("Initial capacity is less than one", exception.getMessage());
	}

	@ParameterizedTest
	@MethodSource("provideDifferentStringCapacties")
	void testCapacity(String... bufferElements) {
		setUpBuffer(bufferElements);
		assertEquals(RING_BUFFER_CAPACITY, ringBuffer.capacity());
	}

	@ParameterizedTest
	@MethodSource("provideDifferentSizes")
	void testSize(int referenceSize, String... bufferElements) {
		setUpBuffer(bufferElements);
		assertEquals(ringBuffer.size(), referenceSize);

		ringBuffer.dequeue();
		assertEquals(ringBuffer.size(), --referenceSize);
	}

	@Test
	void testIsEmpty() {
		assertTrue(ringBuffer.isEmpty());
		ringBuffer.enqueue("a");
		assertFalse(ringBuffer.isEmpty());
	}

	@Test
	void testIsFull() {
		setUpBuffer(new String[] { "1", "2", "3" });
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
		ringBuffer.dequeue();
		ringBuffer.dequeue();
		assertEquals("d", ringBuffer.peek());
	}

	@Test
	void testDequeue() {
		final RingBuffer<String> ringBuffer = new RingBuffer<>(RING_BUFFER_CAPACITY);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			ringBuffer.dequeue();
		});
		assertEquals("Empty ring buffer.", exception.getMessage());

		ringBuffer.enqueue("1");
		assertEquals("1", ringBuffer.dequeue());
		ringBuffer.enqueue("2");
		ringBuffer.enqueue("3");
		ringBuffer.dequeue();
		assertEquals("3", ringBuffer.dequeue());
	}

	@Test
	void testPeek() {
		final RingBuffer<String> ringBuffer = new RingBuffer<>(RING_BUFFER_CAPACITY);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			ringBuffer.peek();
		});
		assertEquals("Empty ring buffer.", exception.getMessage());

		ringBuffer.enqueue("a");
		assertEquals("a", ringBuffer.peek());
		ringBuffer.enqueue("b");
		assertNotEquals("b", ringBuffer.peek());
	}

	@Test
	void nextIteratorTest() {
		Iterator<String> referenceIterator = List.of("1", "2", "3").iterator();
		setUpBuffer(new String[] { "1", "2", "3" });
		Iterator<String> actualIterator = ringBuffer.iterator();

		for (int i = 0; i < ringBuffer.size(); i++) {
			assertEquals(referenceIterator.next(), actualIterator.next());
		}
	}

	@Test
	void testHasNextIterator() {
		setUpBuffer(new String[] { "1", "2", "3" });
		Iterator<String> actualIterator = ringBuffer.iterator();
		// check that hasNext is idempotent
		for (int i = 0; i < 10; i++) {
			assertTrue(actualIterator.hasNext());

		}
	}

	@Test
	void testRemoveIterator() {
		/*
		 * method is not implemented, check for correct exception
		 */
		setUpBuffer(new String[] { "1", "2", "3" });
		Iterator<String> actualIterator = ringBuffer.iterator();
		assertThrows(UnsupportedOperationException.class, () -> {
			actualIterator.remove();
		});
	}

	private static Stream<Arguments> provideDifferentStringCapacties() {
		//@formatter:off
		return Stream.of(
				Arguments.of((Object) new String[] { null }),
				Arguments.of((Object) new String[] { "1", "2" }),
				Arguments.of((Object) new String[] { "1", "2", "3" }),
				Arguments.of((Object) new String[] { "1", "2", "3", "4" }),
				Arguments.of((Object) new String[] { "1", "2", "5" }), 
				Arguments.of((Object) new String[] { "" })
			);
	}

	private static Stream<Arguments> provideDifferentSizes() {
		return Stream.of(
				Arguments.of(1, (Object) new String[] { "1" }),
				Arguments.of(2, (Object) new String[] { "1", "2" }),
				Arguments.of(RING_BUFFER_CAPACITY, (Object) new String[] { "1", "2", "3" }),
				Arguments.of(RING_BUFFER_CAPACITY, (Object) new String[] { "1", "2", "3", "4" }),
				Arguments.of(RING_BUFFER_CAPACITY, (Object) new String[] { "1", "2", "5" }) 
			);
	}
		//@formatter:on

	private void setUpBuffer(String... bufferElements) {
		for (String element : bufferElements) {
			ringBuffer.enqueue(element);
		}
	}
}