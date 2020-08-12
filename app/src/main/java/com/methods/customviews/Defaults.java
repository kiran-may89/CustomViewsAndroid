package com.methods.customviews;

import android.os.Build;

import androidx.annotation.RequiresApi;

public interface Defaults {
    String teste = null;
    @RequiresApi(api = Build.VERSION_CODES.N)
    default public void newDefaultMethod() {

        System.out.println("New default method  is added in interface");
    }
}
