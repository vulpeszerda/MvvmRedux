package com.github.vulpeszerda.mvvmredux

import com.github.vulpeszerda.mvvmredux.addon.FilterTransformer
import io.reactivex.BackpressureStrategy
import io.reactivex.observers.TestObserver

import org.junit.Test

import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subscribers.TestSubscriber

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FilterTransformerUnitTest {

    @Test
    @Throws(Exception::class)
    fun eventStreamCompletedObservable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        eventSubject.onComplete()
        streamSubject.onNext("e")

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onComplete()
        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun dropObservable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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
    fun filterObservable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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

        observer.assertValueCount(1)
        observer.assertValueAt(0, "c")

        streamSubject.onNext("d")
        observer.assertValueCount(2)
        observer.assertValueAt(1, "d")

        streamSubject.onNext("e")
        observer.assertValueCount(3)
        observer.assertValueAt(2, "e")

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(3)

        streamSubject.onNext("f")
        observer.assertValueCount(4)
        observer.assertValueAt(3, "f")

        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onComplete()
        observer.assertValueCount(4)
        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun filterDropObservable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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

        observer.assertValueCount(1)
        observer.assertValueAt(0, "c")

        streamSubject.onNext("d")
        observer.assertValueCount(2)
        observer.assertValueAt(1, "d")

        streamSubject.onNext("e")
        observer.assertValueCount(3)
        observer.assertValueAt(2, "e")

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(3)

        streamSubject.onNext("f")
        observer.assertValueCount(4)
        observer.assertValueAt(3, "f")

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(4)

        streamSubject.onNext("g")

        observer.assertValueCount(4)
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onComplete()
        observer.assertValueCount(4)
        observer.assertNotComplete()

        eventSubject.onComplete()
        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun filterDropFilterObservable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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

        observer.assertValueCount(1)
        observer.assertValueAt(0, "c")

        streamSubject.onNext("d")
        observer.assertValueCount(2)
        observer.assertValueAt(1, "d")

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(3)

        streamSubject.onNext("e")
        observer.assertValueCount(3)
        observer.assertValueAt(2, "e")

        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(4)

        streamSubject.onNext("f")

        observer.assertValueCount(3)
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(2)
        streamSubject.onNext("g")
        observer.assertValueCount(5)
        observer.assertValueAt(3, "f")
        observer.assertValueAt(4, "g")

        streamSubject.onComplete()
        observer.assertValueCount(5)
        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun dropMaybe() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .firstElement()
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("a")

        observer.assertEmpty()
        observer.assertNotComplete()

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
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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
    fun filterMaybe() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .firstElement()
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(observer)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        eventSubject.onNext(2)

        observer.assertEmpty()
        observer.assertNotComplete()
        observer.assertNotTerminated()

        streamSubject.onNext("a")
        observer.assertValueCount(1)
        observer.assertValueAt(0, "a")

        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun filterLastValueMaybe() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val observer = TestObserver<String>()

        streamSubject
                .firstElement()
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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

        eventSubject.onNext(2)

        observer.assertValueCount(1)
        observer.assertValueAt(0, "a")

        observer.assertComplete()
        observer.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun eventStreamCompletedFlowable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val subscriber = TestSubscriber<String>()

        streamSubject
                .toFlowable(BackpressureStrategy.BUFFER)
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
                    integer in 2..3
                })
                .subscribe(subscriber)

        eventSubject.onComplete()
        streamSubject.onNext("e")

        subscriber.assertEmpty()
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onComplete()
        subscriber.assertComplete()
        subscriber.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun dropFlowable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val subscriber = TestSubscriber<String>()

        streamSubject
                .toFlowable(BackpressureStrategy.BUFFER)
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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
    fun filterFlowable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val subscriber = TestSubscriber<String>()

        streamSubject
                .toFlowable(BackpressureStrategy.BUFFER)
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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

        subscriber.assertValueCount(1)
        subscriber.assertValueAt(0, "c")

        streamSubject.onNext("d")
        subscriber.assertValueCount(2)
        subscriber.assertValueAt(1, "d")

        streamSubject.onNext("e")
        subscriber.assertValueCount(3)
        subscriber.assertValueAt(2, "e")

        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(3)

        streamSubject.onNext("f")
        subscriber.assertValueCount(4)
        subscriber.assertValueAt(3, "f")

        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onComplete()
        subscriber.assertValueCount(4)
        subscriber.assertComplete()
        subscriber.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun filterDropFlowable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val subscriber = TestSubscriber<String>()

        streamSubject
                .toFlowable(BackpressureStrategy.BUFFER)
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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

        subscriber.assertValueCount(1)
        subscriber.assertValueAt(0, "c")

        streamSubject.onNext("d")
        subscriber.assertValueCount(2)
        subscriber.assertValueAt(1, "d")

        streamSubject.onNext("e")
        subscriber.assertValueCount(3)
        subscriber.assertValueAt(2, "e")

        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(3)

        streamSubject.onNext("f")
        subscriber.assertValueCount(4)
        subscriber.assertValueAt(3, "f")

        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(4)

        streamSubject.onNext("g")

        subscriber.assertValueCount(4)
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        streamSubject.onComplete()
        subscriber.assertNotComplete()

        eventSubject.onComplete()
        subscriber.assertValueCount(4)
        subscriber.assertComplete()
        subscriber.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }

    @Test
    @Throws(Exception::class)
    fun filterDropFilterFlowable() {
        val eventSubject = BehaviorSubject.createDefault(0)
        val streamSubject = PublishSubject.create<String>()
        val subscriber = TestSubscriber<String>()

        streamSubject
                .toFlowable(BackpressureStrategy.BUFFER)
                .compose(FilterTransformer.create<String, Int>(eventSubject) { integer ->
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

        subscriber.assertValueCount(1)
        subscriber.assertValueAt(0, "c")

        streamSubject.onNext("d")
        subscriber.assertValueCount(2)
        subscriber.assertValueAt(1, "d")

        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(3)

        streamSubject.onNext("e")
        subscriber.assertValueCount(3)
        subscriber.assertValueAt(2, "e")

        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(4)

        streamSubject.onNext("f")

        subscriber.assertValueCount(3)
        subscriber.assertNotComplete()
        subscriber.assertNotTerminated()

        eventSubject.onNext(2)
        streamSubject.onNext("g")
        subscriber.assertValueCount(5)
        subscriber.assertValueAt(3, "f")
        subscriber.assertValueAt(4, "g")

        streamSubject.onComplete()
        subscriber.assertValueCount(5)
        subscriber.assertComplete()
        subscriber.assertTerminated()
        assert(!eventSubject.hasObservers())
        assert(!streamSubject.hasObservers())
    }
}