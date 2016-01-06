package pro.anton.averin.networking.testrest.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.ArrayList;

import pro.anton.averin.networking.testrest.data.ProtocolType;
import pro.anton.averin.networking.testrest.data.db.utils.SQLiteTable;

/**
 * Created by AAverin on 12.11.13.
 */
public class Request {

    public long id;

    public String name;
    public String protocol;
    public String baseUrl;
    public String method;
    public String queryString;
    public ArrayList<RequestHeader> headers;

    public Request() {
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        toString.append("name:");
        toString.append(name);
        toString.append("protocol");
        toString.append(protocol);
        toString.append("baseUrl:");
        toString.append(baseUrl);
        toString.append("method:");
        toString.append(method);
        toString.append("query:");
        toString.append(queryString);

        return toString.toString();
    }

    public int getProtocolType() {
        if (protocol.toLowerCase().contains("https")) {
            return ProtocolType.HTTPS;
        }
        return ProtocolType.HTTP;
    }

    public String asURI() {
        StringBuilder uri = new StringBuilder();
        uri.append(protocol);
        uri.append(baseUrl.trim());
        uri.append(queryString.trim());
        return uri.toString();
    }

    public ContentValues asContentValues() {
        ContentValues values = new ContentValues();
        values.put(SQLITE.COL_NAME, name);
        values.put(SQLITE.COL_PROTOCOL, protocol);
        values.put(SQLITE.COL_BASEURL, baseUrl);
        values.put(SQLITE.COL_METHOD, method);
        values.put(SQLITE.COL_QUERY, queryString);
        return values;
    }

    public Request fromCursor(Cursor cursor) {
        SQLiteTable.TableCursor c = new SQLiteTable.TableCursor(SQLITE.table, cursor);
        this.id = c.getInt(SQLITE.COL_ID);
        this.name = c.getString(SQLITE.COL_NAME);
        this.protocol = c.getString(SQLITE.COL_PROTOCOL);
        this.baseUrl = c.getString(SQLITE.COL_BASEURL);
        this.method = c.getString(SQLITE.COL_METHOD);
        this.queryString = c.getString(SQLITE.COL_QUERY);
        return this;
    }

    public boolean isValid() {
        return baseUrl != null && baseUrl.length() > 0;
    }

    public static class SQLITE {
        public final static String TABLE_NAME = "requests";
        public final static String COL_ID = BaseColumns._ID;
        public final static String COL_NAME = "name";
        public final static String COL_PROTOCOL = "protocol";
        public final static String COL_BASEURL = "baseurl";
        public final static String COL_METHOD = "method";
        public final static String COL_QUERY = "query";

        public static SQLiteTable table = new SQLiteTable.Builder(TABLE_NAME)
                .addIntegerColumn(COL_ID, "PRIMARY KEY")
                .addTextColumn(COL_NAME)
                .addTextColumn(COL_PROTOCOL)
                .addTextColumn(COL_BASEURL)
                .addTextColumn(COL_METHOD)
                .addTextColumn(COL_QUERY).build();
    }
}
