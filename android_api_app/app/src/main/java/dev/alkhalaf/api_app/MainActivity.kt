package dev.alkhalaf.api_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import dev.alkhalaf.api_app.models.Quote
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

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
                responseStatusTextView.text = "${quote.quote}"
                authorTextView.text = "- ${quote.author}"
            },
            {
                responseStatusTextView.text = "That didn't work!"
            },
        )

        queue.add(stringRequest)
    }
}