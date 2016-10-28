package game.sbin.com.notetakingapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class NotesProvider extends ContentProvider {

    private static final String AUTHORITY = "game.sbin.com.notetakingapp.notesprovider";
    private static final String BASE_PATH = "notes"; // name of the all table. here only 1 table
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    //Parse the uri, and find out which operation needs to be done.
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    public static  final String CONTENT_ITEM_TYPE="Note";

    //this static will be executed the first time anything is called from this class
    static{
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", + NOTES_ID);
    }

    private  SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String selection, String[] strings1, String s1) {

        //Added if to return the single row from everyraw.
        if (uriMatcher.match(uri) == NOTES_ID){
            selection = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();
        }
        return database.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.ALL_COLUMNS,
                selection, null, null,null,
                DBOpenHelper.NOTE_CREATED + " DESC");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = database.insert(DBOpenHelper.TABLE_NOTES,
                null, contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_NOTES, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_NOTES,contentValues,
                selection, selectionArgs );
    }
}
