package com.yuapps.ui.base;


import android.support.annotation.NonNull;

public interface BasePresenter<V extends BaseView> {
    void attachView(@NonNull V view);
}