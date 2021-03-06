/*
 * Copyright (C) 2015 Francisco Gonzalez-Armijo Riádigos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.profile.core.view;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;
import com.profile.core.animation.FadeDrawable;
import com.profile.core.animation.InterfaceBitmapAnimator;

public class GalleryView extends ImageView {

    private InterfaceBitmapAnimator[] mAnimators;

    public GalleryView(Context context) {
        super(context);
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GalleryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        setImageBitmap(bm, false);
    }

    public GalleryView setImageBitmap(Bitmap bm, boolean withFade) {
        if (!withFade)
            super.setImageBitmap(bm);
        else {
            FadeDrawable drawable = new FadeDrawable(getContext(), bm);
            super.setBackgroundColor(Color.WHITE);
            super.setImageDrawable(drawable);
        }
        return this;
    }

    public GalleryView setBitmapAnimators(InterfaceBitmapAnimator... animators) {
        mAnimators = animators;
        return this;
    }

    public void startBitmapAnimation() {
        final List<Animator> animatorList = new ArrayList<>();
        for (InterfaceBitmapAnimator bitmapAnimator : mAnimators) {
            Animator animator = bitmapAnimator.getAnimator(GalleryView.this);
            if (animator != null)
                animatorList.add(animator);
        }
        post(new Runnable() {
            @Override
            public void run() {
                for (Animator animator : animatorList)
                    animator.start();
            }
        });
    }

    public int getDrawableWidth() {
        try {
            return getDrawable().getIntrinsicWidth();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  200;
        }
    }

    public int getDrawableHeight() {
        try {
            return getDrawable().getIntrinsicHeight();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  200;
        }

    }

    public int getInsetWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    public int getInsetHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }
}
