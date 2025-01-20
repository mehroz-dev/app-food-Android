package com.donnfelker.fooapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Subscription mAddToCartSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddToCartSubscription = addToCart(this, "1", 2 * 3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HashMap<String, String>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("RxJava", "Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RxJava", e.getMessage(), e);
                    }

                    @Override
                    public void onNext(HashMap<String, String> result) {
                        Log.i("RxJava", "onNext()! Got the callback with the result");
                    }
                });

    }

    public Observable<HashMap<String, String>> addToCart(final Context context, final String skuId, final int quantity) {
        return Observable.defer(new Func0<Observable<HashMap<String, String>>>() {
            @Override
            public Observable<HashMap<String, String>> call() {
                try {
                    return Observable.just(add(skuId, context, quantity));
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private HashMap<String, String> add(String skuId, Context context, int quantity) throws InterruptedException {

        HashMap<String, String> params = new HashMap<>(4);
        params.put("foo", "foo");
        params.put("foo2", "foo2");
        params.put("foo3", "foo3");
        params.put("foo4", "foo4");

        // Simulate some long running network call.
        Thread.sleep(3000);

        return params;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAddToCartSubscription != null && !mAddToCartSubscription.isUnsubscribed()) {
            mAddToCartSubscription.unsubscribe();
        }
    }
}
