package pro.anton.averin.networking.testrest.utils;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.Config;

@Singleton
public class LLogger {

    @Inject
    public LLogger() {
    }

    private static String ensureText(String original) {
        if (original == null || original.isEmpty()) {
            original = "_";
        }
        return original;
    }

    public static void log_e_static(String TAG, Object... message) {
        if (!Config.isLoggingEnabled) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Object msg : message) {
            stringBuilder.append(String.valueOf(msg));
        }
        Log.e(ensureText(TAG), ensureText(stringBuilder.toString()));
    }

    public void log(String TAG, Object... message) {
        if (!Config.isLoggingEnabled) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Object msg : message) {
            stringBuilder.append(String.valueOf(msg));
        }
        Log.d(ensureText(TAG), ensureText(stringBuilder.toString()));
    }

    public void log_e(String TAG, Object... message) {
        if (!Config.isLoggingEnabled) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Object msg : message) {
            stringBuilder.append(String.valueOf(msg));
        }
        Log.e(ensureText(TAG), ensureText(stringBuilder.toString()));
    }

    public void logArray(Object o, String[] array) {
        String tag = o.getClass().getSimpleName();
        StringBuilder stringBuilder = new StringBuilder();
        for (String msg : array) {
            stringBuilder.append(String.valueOf(msg));
            stringBuilder.append(", ");
        }
        Log.d(ensureText(tag), ensureText(stringBuilder.toString()));
    }

    public void log(Object o, Throwable t, Object... message) {
        if (!Config.isLoggingEnabled) {
            return;
        }

        String tag = o.getClass().getSimpleName();
        StringBuilder stringBuilder = new StringBuilder();
        for (Object msg : message) {
            stringBuilder.append(String.valueOf(msg));
        }
        Log.d(ensureText(tag), ensureText(stringBuilder.toString()), t);
    }

    public void log_e(Object o, Throwable t, Object... message) {
        if (!Config.isLoggingEnabled) {
            return;
        }

        String tag = o.getClass().getSimpleName();
        StringBuilder stringBuilder = new StringBuilder();
        for (Object msg : message) {
            stringBuilder.append(String.valueOf(msg));
        }
        Log.e(ensureText(tag), ensureText(stringBuilder.toString()), t);
    }

    public void log(Object o, Object... message) {
        log(o.getClass().getSimpleName(), message);
    }

    public void log_e(Object o, Object... message) {
        log_e(o.getClass().getSimpleName(), message);
    }

}

