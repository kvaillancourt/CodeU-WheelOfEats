package me.karavaillancourt.wheelofeats;

import android.content.ContentProvider;
import java.net.URI;
import java.util.HashMap;
import android.content.UriMatcher;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.database.Cursor;
import java.util.ArrayList;
import android.app.SearchManager;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.prefs.Preferences;

//import com.android.internal.database.ArrayListCursor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import android.support.v4.content.FileProvider;

/**
 * Created by faye on 2015-08-11.
 */
public class PreferencesProvider extends FileProvider {


    //public static final Uri CONTENT_URI = Uri.parse(
           // "content://com.android.googlesearch.SuggestionProvider");
    private static final String USER_AGENT = "Android/1.0";
    private String mSuggestUri;
    private static final int HTTP_TIMEOUT_MS = 1000;

    // TODO: this should be defined somewhere
    private static final String HTTP_TIMEOUT = "http.connection-manager.timeout";
    private static final String LOG_TAG = "GoogleSearch.SuggestionProvider";

    /* The suggestion columns used */
    private static final String[] COLUMNS = new String[] {
            "_id",
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_TEXT_2,
            SearchManager.SUGGEST_COLUMN_QUERY};
    private HttpClient mHttpClient;

    static final String PROVIDER_NAME = "com.example.contentproviderexample.MyProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/cte";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String id = "id";
    static final String name = "name";
    static final int uriCode = 1;
    static final UriMatcher uriMatcher;
    private static HashMap<String, String> values;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "cte", uriCode);
        uriMatcher.addURI(PROVIDER_NAME, "cte/*", uriCode);
    }

    @Override
    public boolean onCreate() {
        //File imagePath = new File(Context.getFilesDir(), "images");

        //BufferedWriter infoWriter = new BufferedWriter(new OutputStreamWriter(
          //      new FileOutputStream(this.getApplicationContext().getFilesDir().toString() + "/patient_records.txt"), "UTF-8"));
        //File newFile = new File(imagePath, "saved_restaurants.txt");
        //Uri contentUri = getUriForFile(getContext(), "com.mydomain.fileprovider", newFile);
        // NOTE:  Do not look up the resource here;  Localization changes may not have completed
        // yet (e.g. we may still be reading the SIM card).
        //mSuggestUri = null;
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case uriCode:
                return "vnd.android.cursor.dir/cte";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        //Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                //null, sortOrder);
        return null;
    }


//    private static ArrayListCursor makeEmptyCursor() {
//        return new ArrayListCursor(COLUMNS, new ArrayList<ArrayList>());
//    }



}
