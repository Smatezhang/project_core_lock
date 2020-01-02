package com.itek.library_core.util;


import android.os.Build;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Author:：simon
 * Date：2019-12-14:13:25
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public class AutoClearedValue<T> {

    private T value;

    public AutoClearedValue(final Fragment fragment, T value) {
        final FragmentManager fragmentManager = fragment.getFragmentManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                    if(f == fragment) {
                        AutoClearedValue.this.value = null;
                        fragmentManager.unregisterFragmentLifecycleCallbacks(this);
                    }
                }
            }, false);
        }
        this.value = value;
    }

    public T get() {
        return this.value;
    }
}

