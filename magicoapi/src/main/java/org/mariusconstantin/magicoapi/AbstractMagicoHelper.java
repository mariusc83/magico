package org.mariusconstantin.magicoapi;

import android.support.v4.util.ArrayMap;

/**
 * Created by Marius on 12/8/2015.
 */
public abstract class AbstractMagicoHelper<T> {
    private T mTarget;

    protected String get(String url) {
        return null;
    }

    protected String put(String url, ArrayMap<String,String> params){
        return null;
    }

    protected String post(String url, ArrayMap<String,String> params){
        return null;
    }
}
