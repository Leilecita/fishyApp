package com.example.android.fishy.network;

public interface GenericCallback<T> {
    void onSuccess(T data);
    void onError(Error error);
}
