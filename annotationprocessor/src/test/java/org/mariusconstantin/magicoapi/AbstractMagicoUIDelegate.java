package org.mariusconstantin.magicoapi;

import android.view.View;

/**
 * Created by Marius on 12/8/2015.
 */
public abstract class AbstractMagicoUIDelegate<T> implements IMagicoUIDelegate<T> {
    protected T mTarget;

    @Override
    public void registerTarget(T target, View rootView) {
        mTarget = target;
    }

    @Override
    public void unregisterTarget(T target, View rootView) {
        mTarget = null;
    }

}
