package station.quality.air.mini.app;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class JavaScriptInterface {
    private static String fileMimeType;
    private final Context context;

    public JavaScriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void getBase64FromBlobData(String base64Data) throws IOException {
        convertBase64StringAndStoreIt(base64Data);
    }

    public static String getBase64StringFromBlobUrl(String blobUrl, String mimeType) {
        if (blobUrl.startsWith("blob")) {
            fileMimeType = mimeType;
            return "javascript: var xhr = new XMLHttpRequest();" +
                    "xhr.open('GET', '" + blobUrl + "', true);" +
                    "xhr.setRequestHeader('Content-type','application/pdf');" +
                    "xhr.responseType = 'blob';" +
                    "xhr.onload = function(e) {" +
                    "    if (this.status == 200) {" +
                    "        var blob = this.response;" +
                    "        var reader = new FileReader();" +
                    "        reader.readAsDataURL(blob);" +
                    "        reader.onloadend = function() {" +
                    "            base64data = reader.result;" +
                    "            Android.getBase64FromBlobData(base64data);" +
                    "        }" +
                    "    }" +
                    "};" +
                    "xhr.send();";
        }
        return "javascript: console.log('This is not a Blob URL');";
    }

    private void convertBase64StringAndStoreIt(String base64Data) throws IOException {
        String regex = "^data:" + fileMimeType + ";base64,";
        byte[] csvContent = Base64.decode(base64Data.replaceFirst(regex, ""), 0);
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, MainActivity.SAVE_FILENAME);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
        Uri collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri path = contentResolver.insert(collection, contentValues);
        OutputStream os = context.getContentResolver().openOutputStream(path);
        os.write(csvContent);
        os.flush();
        os.close();
        Toast.makeText(context, "File downloaded as mini_air_quality_data.csv to Downloads.", Toast.LENGTH_LONG).show();
    }
}
