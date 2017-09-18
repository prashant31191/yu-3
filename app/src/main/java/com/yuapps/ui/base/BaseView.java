package com.yuapps.ui.base;

import android.support.annotation.NonNull;

public interface BaseView<P extends BasePresenter<? extends BaseView>> {
    void attachPresenter(@NonNull P presenter);
}