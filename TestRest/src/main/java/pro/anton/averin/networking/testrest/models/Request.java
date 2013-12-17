package pro.anton.averin.networking.testrest.models;

import android.content.ContentValues;
import android.provider.BaseColumns;

import java.util.ArrayList;

import pro.anton.averin.networking.testrest.db.utils.SQLiteTable;

/**
 * Created by AAverin on 12.11.13.
 */
public class Request {

    public int id;

    public String name;
    public String protocol;
    public String baseUrl;
    public String method;
    public String queryString;
    public ArrayList<RequestHeader> headers;

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

    public String asURI() {
        StringBuilder uri = new StringBuilder();
        uri.append(protocol);
        uri.append(baseUrl.trim());
        uri.append(queryString.trim());
        return uri.toString();
    }

    public Request() {
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
}
