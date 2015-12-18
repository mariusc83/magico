package org.mariusconstantin.magicoapi;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marius on 12/8/2015.
 */
public abstract class AbstractMagicoUIDelegate<T> implements IMagicoUIDelegate<T> {
    protected T mTarget;
    protected List<Integer> mViewsIds = new ArrayList<>();

    protected AbstractMagicoUIDelegate() {
        init();
    }

    protected abstract void init();

    @Override
    public void registerTarget(@NonNull T target, @NonNull View rootView) {
        mTarget = target;
        rootView.setOnClickListener(this);
        registerListeners(rootView);
    }

    @Override
    public void unregisterTarget(@NonNull T target, @NonNull View rootView) {
        mTarget = null;
        unregisterListeners(rootView);
    }

    private void registerListeners(@NonNull final View rootView) {
        for (int id : mViewsIds) {
            final View toAddTo = rootView.findViewById(id);
            if (toAddTo != null)
                toAddTo.setOnClickListener(this);
        }
    }

    private void unregisterListeners(@NonNull final View rootView) {
        for (int id : mViewsIds) {
            final View toAddTo = rootView.findViewById(id);
            if (toAddTo != null)
                toAddTo.setOnClickListener(null);
        }
    }
}
