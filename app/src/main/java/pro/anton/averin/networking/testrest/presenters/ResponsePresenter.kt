package pro.anton.averin.networking.testrest.presenters

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.text.Html
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import pro.anton.averin.networking.testrest.BaseContext
import pro.anton.averin.networking.testrest.data.Storage
import pro.anton.averin.networking.testrest.data.models.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResponsePresenter
@Inject
constructor() : BasePresenter<ResponseView>() {

    @Inject
    lateinit var storage: Storage
    @Inject
    lateinit var baseContext: BaseContext

    private var presentedResponse: Response? = null

    private var htmlHeaders: StringBuilder? = null

    override fun onVisible() {
        super.onVisible()

        if (storage.currentRequest != null && storage.currentResponse == null) {
            presentedResponse = null
            view.hideJson()
            view.turnOffJsonSwitch()
            view.hideNoDataLayout()
            view.showProgressBar()
            view.hideResponseLayout()
        } else if (storage.currentResponse != null && storage.currentResponse != presentedResponse) {
            presentedResponse = storage.currentResponse

            htmlHeaders = getFormatterHeaders(presentedResponse!!)
            view.setHeaders(htmlHeaders!!.toString())
            if (presentedResponse!!.body == null) {
                view.setEmptyBody()
            } else {
                view.setResponseBody(presentedResponse!!.body)

                val jsonObject = parseJson()
                if (jsonObject != null) {
                    view.enableJson()
                    view.setJson(jsonObject)
                } else {
                    view.disableJson()
                }
            }

            view.setShareIntent(buildShareIntent())
            view.hideNoDataLayout()
            view.hideProgressBar()
            view.showResponseLayout()
        }
    }

    private fun parseJson(): JsonElement? {
        try {
            return JsonParser().parse(presentedResponse!!.body)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            return null
        }

    }

    private fun buildShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)

        val shareBody = StringBuilder()
        val subject = getShareSubject(presentedResponse!!)

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject.toString())
        shareIntent.setType("text/html")

        shareBody.append("<h1>Headers</h1>")
        shareBody.append(htmlHeaders!!.toString())
        shareBody.append("<h1>Response body</h1>")

        val isBodyLong = shareBody.length() > 500
        if (isBodyLong) {
            if (!isMediaMounted) {
                view.displayMediaNotMountedMessage()
            } else {
                val bodyFile = getBodyInFile(presentedResponse!!, storage.currentRequest!!.name)

                if (bodyFile == null) {
                    shareBody.append(presentedResponse!!.body)
                } else {
                    shareBody.append("see in attachment")
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(bodyFile))
                }
            }


        } else {
            shareBody.append(presentedResponse!!.body)
        }

        val htmlBody = Html.fromHtml(shareBody.toString()).toString()
        shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, htmlBody)
        shareIntent.putExtra(Intent.EXTRA_TEXT, htmlBody)

        return shareIntent
    }

    private fun getShareSubject(currentResponse: Response): StringBuilder {
        val subject = StringBuilder()
        subject.append("Response to ")
        subject.append(currentResponse.url)
        subject.append(" via TestRest Android")

        return subject
    }

    private fun getFormatterHeaders(currentResponse: Response): StringBuilder {
        val htmlHeaders = StringBuilder()
        htmlHeaders.append("<b>")
        htmlHeaders.append(currentResponse.method)
        htmlHeaders.append("</b>")
        htmlHeaders.append(" ")
        htmlHeaders.append(currentResponse.url)
        htmlHeaders.append("<br/>")
        val headers = currentResponse.headers
        if (headers != null && headers.size > 0) {
            for (key in headers.keys) {
                htmlHeaders.append("<b>")
                htmlHeaders.append(key)
                htmlHeaders.append("</b>")
                val values = headers[key]
                values?.forEach { value ->
                    htmlHeaders.append(" ")
                    htmlHeaders.append(value)
                }
                htmlHeaders.append("<br/>")
            }
        }

        return htmlHeaders
    }

    private val isMediaMounted: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }


    private fun getBodyInFile(currentResponse: Response, name: String?): File? {

        var tempFileForBody: File? = null
        val storageDir = storageDir
        if (storageDir != null) {
            storageDir.mkdirs()
            try {
                tempFileForBody = File.createTempFile("testrest", name, storageDir)
                val fos = FileOutputStream(tempFileForBody)
                fos.write(currentResponse.body.toByteArray())
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return tempFileForBody
    }

    private val storageDir: File?
        get() {
            val externalCacheDir = baseContext.externalCacheDir
            if (externalCacheDir != null) {
                val root = externalCacheDir.absolutePath
                return File(root + File.separator + "TestRest")
            } else {
                return null
            }
        }

    fun onShowJsonRequest() {
        view.showJsonConfirmationDialog()
    }

    fun onShowJson() {
        view.hideRawResponse()
        view.showJson()
    }

    fun onHideJson() {
        view.showRawResponse()
        view.hideJson()
    }

    fun cancelJsonConfirmationDialog() {
        view.turnOffJsonSwitch()
    }
}
