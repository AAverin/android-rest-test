package pro.anton.averin.networking.testrest.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

import pro.anton.averin.networking.testrest.db.utils.SQLiteTable;

/**
 * Created by AAverin on 13.11.13.
 */
public class Headers {

    public static class ViewHeader {
        public String name;
        public String value;
        public ViewHeader(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    public static class Header {
        public long id;
        public int popularity;
        public String name;

        public Header(long id, String name, int popularity) {
            this.id = id;
            this.name = name;
            this.popularity = popularity;
        }

        public ContentValues asContentValues() {
            ContentValues values = new ContentValues();
            values.put(SQLITE.COL_NAME, name);
            values.put(SQLITE.COL_POPULARITY, popularity);
            return values;
        }
    }

    private final static String[] RESPONSE_HEADERS = new String[] {
            "Custom",
            "Access-Control-Allow-Origin",
            "Accept-Ranges",
            "Age",
            "Allow",
            "Cache-Control",
            "Connection",
            "Content-Encoding",
            "Content-Language",
            "Content-Length",
            "Content-Location",
            "Content-MD5",
            "Content-Disposition",
            "Content-Range",
            "Content-Type",
            "Date",
            "ETag",
            "Expires",
            "Last-Modified",
            "Link",
            "Location",
            "P3P",
            "Pragma",
            "Proxy-Authenticate",
            "Refresh",
            "Retry-After",
            "Server",
            "Set-Cookie",
            "Status",
            "Strict-Transport-Security",
            "Trailer",
            "Transfer-Encoding",
            "Vary",
            "Via",
            "Warning",
            "WWW-Authenticate"
    };

    public static class SQLITE {
        public final static String TABLE_NAME = "headers";
        public final static String COL_ID = BaseColumns._ID;
        public final static String COL_NAME = "header_name";
        public final static String COL_POPULARITY = "header_popularity";

        public static SQLiteTable table = new SQLiteTable.Builder(TABLE_NAME)
                .addIntegerColumn(COL_ID, "PRIMARY KEY")
                .addTextColumn(COL_NAME)
                .addIntegerColumn(COL_POPULARITY).build();

        public static void prePopulate(SQLiteDatabase db) {
            for (int i = 0; i < RESPONSE_HEADERS.length; i++) {
                ContentValues values = new ContentValues();
                values.put(COL_NAME, RESPONSE_HEADERS[i]);
                values.put(COL_POPULARITY, 0);
                db.insert(TABLE_NAME, null, values);
            }
        }
    }

    public ArrayList<Header> fromCursor(Cursor c) {
        SQLiteTable.TableCursor cursor = new SQLiteTable.TableCursor(SQLITE.table, c);
        ArrayList<Header> headers = new ArrayList<Header>();
        while (cursor.moveToNext()) {
            headers.add(new Header(cursor.getLong(SQLITE.COL_ID), cursor.getString(SQLITE.COL_NAME), cursor.getInt(SQLITE.COL_POPULARITY)));
        }
        return headers;
    }

    public static String[] getNamesFromList(ArrayList<Header> headers) {
        String[] headerNames = new String[headers.size()];
        int i = 0;
        for (Header header: headers) {
            headerNames[i] = header.name;
            i++;
        }
        return headerNames;
    }

}
