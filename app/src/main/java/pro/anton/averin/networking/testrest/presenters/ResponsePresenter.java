package pro.anton.averin.networking.testrest.presenters;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.data.Storage;
import pro.anton.averin.networking.testrest.data.models.Response;

@Singleton
public class ResponsePresenter extends BasePresenterImpl<ResponseView> {

    @Inject
    Storage storage;

    @Inject
    public ResponsePresenter(BaseContext baseContext) {
        super(baseContext);
    }

    @Override
    public void onVisible() {
        super.onVisible();

        if (storage.getCurrentRequest() != null && storage.getCurrentResponse() == null) {
            //send request again
            view.hideNoDataLayout();
            view.showProgressBar();
            view.hideResponseLayout();
        } else if (storage.getCurrentResponse() != null) {
            //show response
            view.update(storage.getCurrentResponse());
            view.updateShareIntent(buildShareIntent());
            view.hideNoDataLayout();
            view.hideProgressBar();
            view.showResponseLayout();
        }
    }

    private Intent buildShareIntent() {
        Response currentResponse = storage.getCurrentResponse();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        StringBuilder shareBody = new StringBuilder();
        StringBuilder subject = getShareSubject(currentResponse);
        StringBuilder htmlHeaders = getShareHeaders(currentResponse);


        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());
        shareIntent.setType("text/html");

        shareBody.append("<h1>Headers</h1>");
        shareBody.append(htmlHeaders.toString());
        shareBody.append("<h1>Response body</h1>");

        boolean isBodyLong = shareBody.length() > 500;
        if (isBodyLong) {
            if (!isMediaMounted()) {
                view.displayMediaNotMountedMessage();
            } else {
                File bodyFile = getBodyInFile(currentResponse, storage.getCurrentRequest().name);

                if (bodyFile == null) {
                    shareBody.append(currentResponse.body);
                } else {
                    shareBody.append("see in attachment");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(bodyFile));
                }
            }


        } else {
            shareBody.append(currentResponse.body);
        }

        String htmlBody = Html.fromHtml(shareBody.toString()).toString();
        shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, htmlBody);
        shareIntent.putExtra(Intent.EXTRA_TEXT, htmlBody);

        return shareIntent;
    }

    private StringBuilder getShareSubject(Response currentResponse) {
        StringBuilder subject = new StringBuilder();
        subject.append("Response to ");
        subject.append(currentResponse.url);
        subject.append(" via TestRest Android");

        return subject;
    }

    private StringBuilder getShareHeaders(Response currentResponse) {
        StringBuilder htmlHeaders = new StringBuilder();
        htmlHeaders.append("<b>");
        htmlHeaders.append(currentResponse.method);
        htmlHeaders.append("</b>");
        htmlHeaders.append(" ");
        htmlHeaders.append(currentResponse.url);
        htmlHeaders.append("<br/>");
        Map<String, List<String>> headers = currentResponse.headers;
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                htmlHeaders.append("<b>");
                htmlHeaders.append(key);
                htmlHeaders.append("</b>");
                List<String> values = headers.get(key);
                for (String value : values) {
                    htmlHeaders.append(" ");
                    htmlHeaders.append(value);
                }
                htmlHeaders.append("<br/>");
            }
        }

        return htmlHeaders;
    }

    private boolean isMediaMounted() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    private File getBodyInFile(Response currentResponse, String name) {

        File tempFileForBody = null;
        File storageDir = getStorageDir();
        if (storageDir != null) {
            storageDir.mkdirs();
            try {
                tempFileForBody = File.createTempFile("testrest", name, storageDir);
                FileOutputStream fos = new FileOutputStream(tempFileForBody);
                fos.write(currentResponse.body.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return tempFileForBody;
    }

    private File getStorageDir() {
        File externalCacheDir = baseContext.getExternalCacheDir();
        if (externalCacheDir != null) {
            String root = externalCacheDir.getAbsolutePath();
            return new File(root + File.separator + "TestRest");
        } else {
            return null;
        }
    }
}
