package org.mariusconstantin.magicoapi;

import android.view.View;

/**
 * Created by MConstantin on 12/20/2015.
 */
public interface IMagicoUIDelegate<T> extends View.OnClickListener {

    void registerTarget(T target, View rootView);

    void unregisterTarget(T target, View rootView);

    @Override
    void onClick(View v);
}
