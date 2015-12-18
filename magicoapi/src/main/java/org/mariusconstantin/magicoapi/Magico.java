package org.mariusconstantin.magicoapi;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by MConstantin on 1/17/2016.
 */
public class Magico {
    public static final String TAG = Magico.class.getSimpleName();

    public static final String ANDROID_PREFIX = "android.";
    public static final String MAGICO_SUFFIX = "$$MAGICO";
    public static final String JAVA_PREFIX = "java.";

    private static final Map<String, IMagicoUIDelegate<Activity>> mMagicoUiInstances = new Hashtable<>();

    public static boolean registerForUIDelegate(@NonNull Activity target, @NonNull View rootView) {
        final String targetClassName = target.getClass().getCanonicalName();
        // check first into the existent classes
        final IMagicoUIDelegate<Activity> mDelegateMagicoInstance = mMagicoUiInstances.get(targetClassName);
        if (mDelegateMagicoInstance != null) {
            mDelegateMagicoInstance.registerTarget(target, rootView);
            return true;
        }

        // check for Magico generated class here
        final Class<IMagicoUIDelegate<Activity>> generatedClass = getMagicoUIDelegateClassForTargetClass(target.getClass());
        if (generatedClass != null) {
            try {
                final IMagicoUIDelegate<Activity> instance = generatedClass.newInstance();
                instance.registerTarget(target, rootView);
                mMagicoUiInstances.put(targetClassName, instance);
                return true;
            } catch (IllegalAccessException | InstantiationException e) {
                Log.e(TAG, "registerForUIDelegate: exception instantiating the delegate class", e);
            }
        }
        return false;
    }

    public static boolean unregisterForUIDelegate(@NonNull Activity target, @NonNull View rootView) {
        final String targetClassName = target.getClass().getCanonicalName();
        // check for an existing IMagicoUIDelegate instance for this target
        final IMagicoUIDelegate<Activity> mDelegateMagicoInstance = mMagicoUiInstances.get(targetClassName);
        if (mDelegateMagicoInstance != null) {
            mDelegateMagicoInstance.unregisterTarget(target, rootView);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static Class<IMagicoUIDelegate<Activity>> getMagicoUIDelegateClassForTargetClass(Class<?> targetClass) {
        final String targetClassName = targetClass.getCanonicalName();
        if (targetClassName.startsWith(ANDROID_PREFIX) || targetClassName.startsWith(JAVA_PREFIX)) {
            Log.e(TAG, "registerForAnnotation: we could not find a Magico class for this target");
            return null;
        }

        // check if the current target is already the activity class or AppCompatActivity
        final String magicoClassName = getMagicoDelegateClassName(targetClassName);
        try {
            return (Class<IMagicoUIDelegate<Activity>>) Class
                    .forName(magicoClassName);

        } catch (ClassNotFoundException e) {
            // try for the super
            return getMagicoUIDelegateClassForTargetClass(targetClass.getSuperclass());
        }
    }

    private static String getMagicoDelegateClassName(String targetClassName) {
        return targetClassName + MAGICO_SUFFIX;
    }
}
