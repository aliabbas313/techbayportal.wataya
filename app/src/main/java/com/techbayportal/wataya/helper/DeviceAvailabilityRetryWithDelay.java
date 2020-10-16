package com.techbayportal.wataya.helper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class DeviceAvailabilityRetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {
    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public DeviceAvailabilityRetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
    }

    @Override
    public Observable<?> apply(final Observable<? extends Throwable> attempts) {
        return attempts
                .flatMap((Function<Throwable, Observable<?>>) throwable -> {
                    if(AppStatus.Companion.getInstance().isOnline()) {
                        if (++retryCount < maxRetries && (throwable instanceof NoConnectivityException
                                || throwable.getCause() instanceof  NoConnectivityException)) {
//                        Log.d(DeviceAvailabilityRetryWithDelay.this.getClass().getName() , "device avaiability is not ready "+ " and will retry");

                            // When this Observable calls onNext, the original
                            // Observable will be retried (i.e. re-subscribed).
                            return Observable.timer(retryDelayMillis,
                                    TimeUnit.MILLISECONDS);
                        }
                    } else {
                        return Observable.error(throwable);
                    }

                    // Max retries hit. Just pass the error along.
                    return Observable.error(throwable);
                });
    }
}
