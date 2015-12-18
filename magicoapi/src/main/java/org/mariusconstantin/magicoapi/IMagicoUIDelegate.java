package org.mariusconstantin.magicoapi;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by MConstantin on 12/20/2015.
 */
public interface IMagicoUIDelegate<T> extends View.OnClickListener {

    void registerTarget(@NonNull T target,@NonNull View rootView);

    void unregisterTarget(@NonNull T target,@NonNull View rootView);

    @Override
    void onClick(View v);
}
