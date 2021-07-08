package dev.alkhalaf.api_app

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.graphics.toColorInt
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import dev.alkhalaf.api_app.models.Quote
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // runs once the app launches the activity.
        makeRequest(findViewById<Button>(R.id.makeRequestButton))
    }

    private var lastQuote: Quote? = null

    fun makeRequest(view: View) {
        val responseStatusTextView: TextView = findViewById<TextView>(R.id.responseStatusTextView)
        val authorTextView: TextView = findViewById<TextView>(R.id.authorTextView);

        responseStatusTextView.text = "Loading..."
        authorTextView.text = ""

        val queue: RequestQueue = Volley.newRequestQueue(this)
        val url: String = "https://api.quotable.io/random"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonResponse: JSONObject = JSONObject(response)
                val quote: Quote = Quote(
                    id = jsonResponse["_id"].toString(),
                    quote = jsonResponse["content"].toString(),
                    author = jsonResponse["author"].toString(),
                )
                lastQuote = quote
                responseStatusTextView.text = "${quote.quote}"
                authorTextView.text = "- ${quote.author}"
            },
            {
                responseStatusTextView.text = "That didn't work!"
            },
        )

        queue.add(stringRequest)
    }

    fun shareQuote(view: View) {
        if (lastQuote == null) {
            val snackbar: Snackbar =
                Snackbar.make(
                    view,
                    "There seems to be no quote! 🤷‍♂️",
                    Snackbar.LENGTH_SHORT
                )
            snackbar.setAction("Action", null)
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.setBackgroundTint("#6200EE".toColorInt())
            snackbar.show()
            return;
        }

        val shareIntent: Intent = Intent();
        shareIntent.action = Intent.ACTION_SEND;

        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        val shareMessage: String = """${lastQuote!!.quote}
- by ${lastQuote!!.author}"""
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)

        startActivity(
            Intent.createChooser(
                shareIntent,
                "Share this awesome quote with your friends!"
            )
        )
    }
}