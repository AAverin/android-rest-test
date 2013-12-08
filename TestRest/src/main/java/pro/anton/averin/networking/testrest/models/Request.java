package pro.anton.averin.networking.testrest.models;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import pro.anton.averin.networking.testrest.db.utils.SQLiteTable;

/**
 * Created by AAverin on 12.11.13.
 */
public class Request {

    public int id;

    public String protocol;
    public String baseUrl;
    public String method;
    public String queryString;
    public ArrayList<Headers.ViewHeader> headers;

    public static class SQLITE {
        public final static String TABLE_NAME = "requests";
        public final static String COL_ID = BaseColumns._ID;
        public final static String COL_PROTOCOL = "protocol";
        public final static String COL_BASEURL = "baseurl";
        public final static String COL_METHOD = "method";
        public final static String COL_QUERY = "query";
        public final static String COL_HEADERS = "headers";

        public static SQLiteTable table = new SQLiteTable.Builder(TABLE_NAME)
                .addIntegerColumn(COL_ID, "PRIMARY KEY")
                .addTextColumn(COL_PROTOCOL)
                .addTextColumn(COL_BASEURL)
                .addTextColumn(COL_METHOD)
                .addTextColumn(COL_QUERY)
                .addTextColumn(COL_HEADERS).build();
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
}
