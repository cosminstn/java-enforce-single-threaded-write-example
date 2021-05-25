package tech.sharply.experimental.single_threaded_write.data;

import tech.sharply.experimental.single_threaded_write.tracking.ThreadTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * This class achieves thread safety by isolating all write operations to a single thread.
 */
public class ThreadSafeList {

	private final ThreadTracker   threadTracker = new ThreadTracker();
	private final ExecutorService writeExecutor = Executors.newSingleThreadExecutor();

	private final List<Integer> data = new ArrayList<>();

	/**
	 * We restrict write to the methods implemented here.
	 */
	public List<Integer> getData() {
		threadTracker.track("getData");
		return data.stream().collect(Collectors.toUnmodifiableList());
	}

	public Future<?> add(int newElement) {
		return writeExecutor.submit(() -> {
			threadTracker.track("add");
			data.add(newElement);
		});
	}

	public Future<?> remove(int newElement) {
		return writeExecutor.submit(() -> {
			threadTracker.track("remove");
			data.remove(newElement);
		});
	}

	public ThreadTracker getThreadTracker() {
		return threadTracker;
	}

	public void shutdown() {
		writeExecutor.shutdown();
	}
}
