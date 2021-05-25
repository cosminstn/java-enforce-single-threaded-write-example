## About
The purpose of this app is to illustrate achieving concurrency on data by restricting the write operations to a 
single thread.  
In this case, [ThreadSafeList](src/tech/sharply/experimental/single_threaded_write/data/ThreadSafeList.java) represents 
a concurrent implementation of a list, that uses ArrayList as the underlying collection. The class exposes methods to 
**add**, **remove** and **get** all stored data.  
The class internally creates a single threaded ExecutorService on which all **add** and **remove** operations are
performed.

Everything in the package ***tracking*** is purely meant to track active threads.  

## Testing
In [Main](src/tech/sharply/experimental/single_threaded_write/Main.java) we instantiate a
[ThreadSafeList](src/tech/sharply/experimental/single_threaded_write/data/ThreadSafeList.java) to test if every **add**
and **remove** operation are isolated to one single thread.

Running Main shows us that the iterations defined in main are performed on each thread defined in the **executor**
thread pool. 
```
Executed add iteration: 0 on ThreadInfo{id=22, name='pool-2-thread-1'}
Executed add iteration: 2 on ThreadInfo{id=24, name='pool-2-thread-3'}
Executed add iteration: 1 on ThreadInfo{id=23, name='pool-2-thread-2'}
Executed add iteration: 3 on ThreadInfo{id=25, name='pool-2-thread-4'}
...
Executed remove iteration: 21 on ThreadInfo{id=23, name='pool-2-thread-2'}
Executed remove iteration: 22 on ThreadInfo{id=24, name='pool-2-thread-3'}
Executed remove iteration: 24 on ThreadInfo{id=22, name='pool-2-thread-1'}
Executed remove iteration: 23 on ThreadInfo{id=25, name='pool-2-thread-4'}
```
And at the end the **ThreadTracker** displays the threads on which every operation in **ThreadSafeList** is executed.
```aidl
-----> Key: add
ThreadInfo{id=26, name='pool-1-thread-1'}


-----> Key: getData
ThreadInfo{id=1, name='main'}


-----> Key: remove
ThreadInfo{id=26, name='pool-1-thread-1'}
```