package com.itek.library_core.util;

import androidx.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * Author:：simon
 * Date：2019-12-14:13:26
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public class ImageBindingAdapter {

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

//    @BindingAdapter({"app:imageUrl", "app:placeHolder", "app:error"})
//    public static void loadImage(ImageView imageView, String url, Drawable holderDrawable, Drawable errorDrawable) {
//        Glide.with(imageView.getContext())
//                .load(url)
//                .placeholder(holderDrawable)
//                .error(errorDrawable)
//                .into(imageView);
//    }

    @BindingAdapter({"imageUrl"})
    public static void loadimage(ImageView imageView,String url){
        Log.e("sss", "loadimage: "+url+"   -走到这里了");
        Glide.with(imageView.getContext()).load(url)
                .into(imageView);
    }


}
