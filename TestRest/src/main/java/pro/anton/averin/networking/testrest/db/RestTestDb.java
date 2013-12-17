package pro.anton.averin.networking.testrest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceActivity;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pro.anton.averin.networking.testrest.models.Headers;
import pro.anton.averin.networking.testrest.models.Request;
import pro.anton.averin.networking.testrest.models.RequestHeader;

/**
 * Created by AAverin on 12.11.13.
 */
public class RestTestDb {

    private final static String TAG = RestTestDb.class.getName();
    public static final String DATABASE_NAME = "resttest.db";
    private static final int DATABASE_VERSION = 1;

    private final DatabaseHelper helper;

    public RestTestDb(Context context) {
        helper = HelperManager.getHelper(context);
    }

    public void addRequest(Request request) {
        SQLiteDatabase db = helper.getWritableDatabase();

        long requestId = db.insert(Request.SQLITE.TABLE_NAME, null, request.asContentValues());
        if (requestId == -1) {
            throw new android.database.SQLException("Could not add Request");
        }

        for (RequestHeader header : request.headers) {
            header.requestId = requestId;
            long id = db.insert(RequestHeader.SQLITE.TABLE_NAME, null, header.asContentValues());
            if (id == -1) {
                throw new android.database.SQLException("Could not add RequestHeader");
            }
        }


    }

    public ArrayList<Headers.Header> getSupportedHeaders() {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor c = db.query(Headers.SQLITE.TABLE_NAME, Headers.SQLITE.table.getColumns(), null, null, null, null, Headers.SQLITE.COL_POPULARITY);
        try {
            if (c.getCount() > 1) {
                return (new Headers()).fromCursor(c);
            }
            return null;
        } finally {
            c.close();
        }
    }

    public void updateHeader(Headers.Header header) throws SQLException {
        ContentValues values = header.asContentValues();
        SQLiteDatabase db = helper.getWritableDatabase();
        String whereClause = Headers.SQLITE.COL_ID + "=?";
        String[] whereArgs = { String.valueOf(header.id) };
        int rows = db.update(Headers.SQLITE.TABLE_NAME, values, whereClause, whereArgs);
        if (rows != 1)
            throw new SQLException("Couldn't update header " + header);
    }

    public void close() {
        HelperManager.releaseHelper();
    }

    private static final class HelperManager {
        private static DatabaseHelper helper = null;
        private static int counter = 0;

        public static synchronized DatabaseHelper getHelper(Context context) {
            if (++counter == 1) {
                helper = new DatabaseHelper(context);
            }

            return helper;
        }

        public static synchronized void releaseHelper() {
            if (--counter == 0) {
                helper.close();
                helper = null;
            }

            if (counter < 0)
                throw new IllegalStateException();
        }
    }

    private static final class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Request.SQLITE.table.getCreateSQL());
            db.execSQL(Headers.SQLITE.table.getCreateSQL());
            Headers.SQLITE.prePopulate(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL(Request.SQLITE.table.getDropSQL());
            onCreate(db);
        }
    }

}
