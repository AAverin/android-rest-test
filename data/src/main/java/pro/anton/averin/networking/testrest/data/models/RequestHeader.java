package pro.anton.averin.networking.testrest.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import pro.anton.averin.networking.testrest.data.db.utils.SQLiteTable;

/**
 * Created by AAverin on 17.12.13.
 */
public class RequestHeader {
    public long id;
    public long requestId;
    public String name;
    public String value;

    public RequestHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public RequestHeader() {

    }

    @Override
    public String toString() {
        return "RequestHeader :: " + name + " : " + value + ";";
    }

    public ContentValues asContentValues() {
        ContentValues values = new ContentValues();
        values.put(SQLITE.COL_REQUESTID, requestId);
        values.put(SQLITE.COL_NAME, name);
        values.put(SQLITE.COL_VALUE, value);
        return values;
    }

    public RequestHeader fromCursor(Cursor cursor) {
        SQLiteTable.TableCursor c = new SQLiteTable.TableCursor(SQLITE.table, cursor);
        this.id = c.getInt(SQLITE.COL_ID);
        this.name = c.getString(SQLITE.COL_NAME);
        this.requestId = c.getInt(SQLITE.COL_REQUESTID);
        this.value = c.getString(SQLITE.COL_VALUE);
        return this;
    }

    public static class SQLITE {
        public final static String TABLE_NAME = "request_headers";
        public final static String COL_ID = BaseColumns._ID;
        public final static String COL_REQUESTID = "requestId";
        public final static String COL_NAME = "name";
        public final static String COL_VALUE = "value";

        public static SQLiteTable table = new SQLiteTable.Builder(TABLE_NAME)
                .addIntegerColumn(COL_ID, "PRIMARY KEY")
                .addIntegerColumn(COL_REQUESTID)
                .addTextColumn(COL_NAME)
                .addTextColumn(COL_VALUE).build();
    }
}