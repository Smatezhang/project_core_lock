package com.itek.library_core.repository.boundResource;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import android.util.Log;

import com.itek.library_core.api.ApiResponse;
import com.itek.library_core.api.BaseResponse;
import com.itek.library_core.util.AppExecutors;
import com.itek.library_core.vo.Resource;



/**
 * Author:：simon
 * Date：2019-12-14:13:14
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        setZoneValue(Resource.loading(null,null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> NetworkBoundResource.this.setZoneValue(Resource.success(newData)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        LiveData<ApiResponse<BaseResponse<RequestType>>> apiResponse = createCall();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource, newData -> setZoneValue(Resource.loading(null,newData)));
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                if ("-1".equals(response.body.getErrorCode())) { // bussiness error
                    result.addSource(dbSource,
                            newData -> setZoneValue(Resource.error(response.body.getErrorMsg(), newData)));
                } else if ("0".equals(response.body.getErrorCode())) { // bussiness error
                    result.addSource(dbSource,
                            newData -> setZoneValue(Resource.error(response.body.getErrorMsg(), newData)));
                } else {
                    appExecutors.diskIO().execute(() -> {
                        saveCallResult(processResponse(response));
                        appExecutors.mainThread().execute(() -> {
                                    LiveData<ResultType> dbSource1 = loadFromDb();
                                    result.addSource(dbSource1,
                                            newData -> {
                                                result.removeSource(dbSource1);
                                                NetworkBoundResource.this.setZoneValue(Resource.success(newData));
                                            });
                                }
                        );
                    });
                }
            } else {
                onFetchFailed();
                result.addSource(dbSource,
                        newData -> setZoneValue(Resource.error(response.errorMessage, newData)));
            }
        });
    }


    @MainThread
    private void setZoneValue(Resource<ResultType> newValue) {
        Log.d("ZoneValue", ": -----------setZoneValue----------" + newValue.status);
        result.setValue(newValue);
    }

    protected void onFetchFailed() {
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected RequestType processResponse(ApiResponse<BaseResponse<RequestType>> response) {
        return response.body.getData();
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<BaseResponse<RequestType>>> createCall();
}


