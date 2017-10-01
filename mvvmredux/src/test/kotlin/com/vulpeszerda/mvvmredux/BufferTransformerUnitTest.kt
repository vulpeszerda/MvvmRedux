package com.vulpeszerda.mvvmredux

import com.vulpeszerda.mvvmredux.addon.BufferUntilTransformer
import io.reactivex.BackpressureStrategy
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class BufferTransformerUnitTest {

    @Test
    @Throws(Exception::class)
    fun bufferObservable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        assert(eventSubject.hasObservers())

        streamSubject.onNext("a")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("b")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(1)

        streamSubject.onNext("c")

        observer.assertEmpty()
        observer.assertValueCount(0)
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onComplete()
        observer.assertNotComplete()
        observer.assertEmpty()

        eventSubject.onComplete()
        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun cancelObservable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        assert(eventSubject.hasObservers())

        observer.dispose()
        assert(observer.isCancelled)
        assert(observer.isDisposed)

        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun bufferFlushObservable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("a")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("b")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(1)

        streamSubject.onNext("c")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(2)

        observer.assertValueCount(3)
        observer.assertValues("a", "b", "c")
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("d")
        observer.assertValueCount(4)
        observer.assertValueAt(3, "d")

        streamSubject.onNext("e")
        observer.assertValueCount(5)
        observer.assertValueAt(4, "e")

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(3)

        streamSubject.onNext("f")
        observer.assertValueCount(6)
        observer.assertValueAt(5, "f")

        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onComplete()
        observer.assertValueCount(6)
        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun bufferFlushBufferObservable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("a")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("b")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(1)

        streamSubject.onNext("c")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(2)

        observer.assertValueCount(3)
        observer.assertValues("a", "b", "c")
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("d")
        observer.assertValueCount(4)
        observer.assertValueAt(3, "d")

        streamSubject.onNext("e")
        observer.assertValueCount(5)
        observer.assertValueAt(4, "e")

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(3)

        streamSubject.onNext("f")
        observer.assertValueCount(6)
        observer.assertValueAt(5, "f")

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(4)

        streamSubject.onNext("g")

        observer.assertValueCount(6)
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(2)
        observer.assertValueCount(7)
        observer.assertValueAt(6, "g")

        streamSubject.onNext("h")
        observer.assertValueCount(8)
        observer.assertValueAt(7, "h")

        streamSubject.onComplete()
        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun bufferFlowable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val subscriber = TestSubscriber<String>()

        streamSubject
                .toFlowable(BackpressureStrategy.BUFFER)
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(subscriber)

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        assert(eventSubject.hasObservers())

        streamSubject.onNext("a")

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onNext("b")

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(1)

        streamSubject.onNext("c")

        subscriber.assertEmpty()
        subscriber.assertValueCount(0)
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onComplete()
        subscriber.assertNotComplete()
        subscriber.assertEmpty()

        eventSubject.onComplete()
        subscriber.assertComplete()
        subscriber.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun cancelFlowable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val subscriber = TestSubscriber<String>()

        streamSubject
                .toFlowable(BackpressureStrategy.BUFFER)
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(subscriber)

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        assert(eventSubject.hasObservers())

        subscriber.dispose()
        assert(subscriber.isCancelled)
        assert(subscriber.isDisposed)

        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun bufferFlushFlowable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val subscriber = TestSubscriber<String>()

        streamSubject
                .toFlowable(BackpressureStrategy.BUFFER)
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(subscriber)

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onNext("a")

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onNext("b")

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(1)

        streamSubject.onNext("c")

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(2)

        subscriber.assertValueCount(3)
        subscriber.assertValues("a", "b", "c")
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onNext("d")
        subscriber.assertValueCount(4)
        subscriber.assertValueAt(3, "d")

        streamSubject.onNext("e")
        subscriber.assertValueCount(5)
        subscriber.assertValueAt(4, "e")

        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(3)

        streamSubject.onNext("f")
        subscriber.assertValueCount(6)
        subscriber.assertValueAt(5, "f")

        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onComplete()
        subscriber.assertValueCount(6)
        subscriber.assertComplete()
        subscriber.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun bufferFlushBufferFlowable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val subscriber = TestSubscriber<String>()

        streamSubject
                .toFlowable(BackpressureStrategy.BUFFER)
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(subscriber)

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onNext("a")

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onNext("b")

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(1)

        streamSubject.onNext("c")

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(2)

        subscriber.assertValueCount(3)
        subscriber.assertValues("a", "b", "c")
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onNext("d")
        subscriber.assertValueCount(4)
        subscriber.assertValueAt(3, "d")

        streamSubject.onNext("e")
        subscriber.assertValueCount(5)
        subscriber.assertValueAt(4, "e")

        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(3)

        streamSubject.onNext("f")
        subscriber.assertValueCount(6)
        subscriber.assertValueAt(5, "f")

        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(4)

        streamSubject.onNext("g")

        subscriber.assertValueCount(6)
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(2)
        subscriber.assertValueCount(7)
        subscriber.assertValueAt(6, "g")

        streamSubject.onNext("h")
        subscriber.assertValueCount(8)
        subscriber.assertValueAt(7, "h")

        streamSubject.onComplete()
        subscriber.assertComplete()
        subscriber.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun bufferMaybe() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .firstElement()
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        assert(eventSubject.hasObservers())

        streamSubject.onNext("a")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(1)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onComplete()
        observer.assertNotComplete()
        observer.assertEmpty()

        eventSubject.onComplete()
        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun cancelMaybe() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .firstElement()
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        assert(eventSubject.hasObservers())

        observer.dispose()
        assert(observer.isCancelled)
        assert(observer.isDisposed)

        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun bufferFlushMaybe() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .firstElement()
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("a")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(1)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(2)

        observer.assertValueCount(1)
        observer.assertValues("a")
        observer.assertComplete()
        observer.assertTerminated()

        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun flushMaybe() {
        val eventSubject = BehaviorSubject.createDefault(2)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .firstElement()
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("a")
        observer.assertValueCount(1)
        observer.assertValues("a")

        streamSubject.onComplete()
        observer.assertComplete()
        observer.assertTerminated()

        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun delayCompletionCompletable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .ignoreElements()
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        assert(eventSubject.hasObservers())

        streamSubject.onComplete()

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(1)

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onComplete()
        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun cancelCompletable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .ignoreElements()
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        assert(eventSubject.hasObservers())

        observer.dispose()
        assert(observer.isCancelled)
        assert(observer.isDisposed)

        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun delayCompletionUntilEventCompletable() {

        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .ignoreElements()
                .compose(BufferUntilTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        assert(eventSubject.hasObservers())

        streamSubject.onComplete()

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(1)

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(2)
        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }
}