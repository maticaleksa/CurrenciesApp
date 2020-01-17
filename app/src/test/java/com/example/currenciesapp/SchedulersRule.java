package com.example.currenciesapp;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

import org.junit.rules.ExternalResource;

import java.util.concurrent.Callable;

public class SchedulersRule extends ExternalResource {

    private TestScheduler testScheduler = new TestScheduler();
    private Function<Scheduler, Scheduler> testSchedulerFunction = scheduler -> testScheduler;
    private Function<Callable<Scheduler>, Scheduler> callableTrampolineScheduler = scheduler -> Schedulers.trampoline();

    private Function<? super Scheduler, ? extends Scheduler> oldIoSchedulerHandler;
    private Function<? super Scheduler, ? extends Scheduler> oldComputationHandler;
    private Function<Callable<Scheduler>, Scheduler> oldInitMainThreadHandler;
    private Function<Scheduler, Scheduler> oldMainThreadHandler;

    public TestScheduler getTestScheduler() {
        return testScheduler;
    }

    @Override
    protected void before() {
        oldIoSchedulerHandler =  RxJavaPlugins.getIoSchedulerHandler();
        oldComputationHandler = RxJavaPlugins.getComputationSchedulerHandler();
        oldMainThreadHandler = RxAndroidPlugins.getOnMainThreadSchedulerHandler();
        oldInitMainThreadHandler = RxAndroidPlugins.getInitMainThreadSchedulerHandler();


        RxJavaPlugins.setIoSchedulerHandler(testSchedulerFunction);
        RxJavaPlugins.setComputationSchedulerHandler(testSchedulerFunction);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(callableTrampolineScheduler);
        RxAndroidPlugins.setMainThreadSchedulerHandler(testSchedulerFunction);
    }


    @Override
    protected void after() {
        RxJavaPlugins.setIoSchedulerHandler(oldIoSchedulerHandler);
        RxJavaPlugins.setComputationSchedulerHandler(oldComputationHandler);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(oldInitMainThreadHandler);
        RxAndroidPlugins.setMainThreadSchedulerHandler(oldMainThreadHandler);
    }
}
