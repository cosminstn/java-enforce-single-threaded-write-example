package tech.sharply.experimental.single_threaded_write;

import tech.sharply.experimental.single_threaded_write.data.ThreadSafeList;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static tech.sharply.experimental.single_threaded_write.tracking.ThreadTracker.getThreadInfo;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		final var list = new ThreadSafeList();

		final var executor = Executors.newFixedThreadPool(4);

		// add multiple entries from different threads
		final var addIterations = 50;
		final var addCountdown  = new CountDownLatch(addIterations);
		for (var i = 0; i < addIterations; i++) {
			var finalI = i;
			executor.submit(() -> {
				try {
					list.add(finalI).get();
				} catch (Exception e) {
					e.printStackTrace();
				}
				addCountdown.countDown();
				System.out.println("Executed add iteration: " + finalI + " on " + getThreadInfo());
			});
		}
		addCountdown.await();

		// remove multiple entries from different threads
		final var removeIterations = 25;
		final var toRemove = list.getData().stream().limit(removeIterations).collect(Collectors.toList());

		final var removeCountdown  = new CountDownLatch(removeIterations);
		var i = new AtomicInteger(0);
		for (final var el : toRemove) {
			executor.submit(() -> {
				// remove the i-th element
				try {
					list.remove(el).get();
				} catch (Exception e) {
					e.printStackTrace();
				}
				removeCountdown.countDown();
				System.out.println("Executed remove iteration: " + i.getAndIncrement() + " on " + getThreadInfo());
			});
		}
		removeCountdown.await();

		// Both executors must be shutdown, otherwise the program will not finish execution
		executor.shutdown();
		list.shutdown();

		System.out.println(list.getThreadTracker().getThreadsDescription());
		System.out.println("Execution finished!");
	}
}
