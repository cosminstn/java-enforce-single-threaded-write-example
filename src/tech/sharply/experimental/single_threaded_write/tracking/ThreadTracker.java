package tech.sharply.experimental.single_threaded_write.tracking;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Can be used to track active threads in an app.
 */
public class ThreadTracker {

	private final Map<String, Set<ThreadInfo>> threadsByKey = new ConcurrentHashMap<>();

	public Set<ThreadInfo> getThreads() {
		return threadsByKey.values().stream().reduce(new HashSet<>(), (allThreads, keyThreads) -> {
			allThreads.addAll(keyThreads);
			return allThreads;
		});
	}

	/**
	 * @return Returns the set of registered threads for the specified key.
	 */
	public Set<ThreadInfo> getThreads(String key) {
		if (!threadsByKey.containsKey(key)) {
			threadsByKey.put(key, ConcurrentHashMap.newKeySet());
		}
		return threadsByKey.get(key);
	}

	public String getThreadsDescription(String key) {
		var info = new StringBuilder();
		for (var thread : getThreads(key)) {
			info.append(thread.toString()).append("\n");
		}
		return info.toString();
	}

	public String getThreadsDescription() {
		var info = new StringBuilder();
		for (var key : threadsByKey.keySet()) {
			info.append("\n\n-----> Key: ").append(key).append("\n").append(getThreadsDescription(key));
		}

		return info.toString();
	}

	/**
	 * Regiters specified thread info to the specified key.
	 */
	public ThreadInfo track(String key, Thread thread) {
		var threadInfo = getThreadInfo(thread);
		getThreads(key).add(threadInfo);
		return threadInfo;
	}

	/**
	 * Registers current thread info to the specified key.
	 */
	public ThreadInfo track(String key) {
		return track(key, Thread.currentThread());
	}

	public static ThreadInfo getThreadInfo(Thread thread) {
		return new ThreadInfo(thread.getId(), thread.getName());
	}

	public static ThreadInfo getThreadInfo() {
		return new ThreadInfo(Thread.currentThread().getId(), Thread.currentThread().getName());
	}
}
