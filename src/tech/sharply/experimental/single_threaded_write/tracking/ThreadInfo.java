package tech.sharply.experimental.single_threaded_write.tracking;

import java.util.Objects;

public class ThreadInfo {

	private final Long   id;
	private final String name;

	public ThreadInfo(Long id, String name) {
		this.id   = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ThreadInfo)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		var other = (ThreadInfo) obj;
		return this.getId().equals(other.getId()) &&
				this.getName().equals(other.getName());
	}

	@Override
	public String toString() {
		return "ThreadInfo{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}