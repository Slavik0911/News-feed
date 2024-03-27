// InfoActivity.kt
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newsfeed.data.Article
import com.example.newsfeed.databinding.InfoBinding

class InfoActivity : AppCompatActivity() {
    private lateinit var infoBinding: InfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        infoBinding = InfoBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)

        val article = intent.getSerializableExtra("article") as Article
        infoBinding.tvTitle.text = article.title
        infoBinding.tvInsider.text = article.author
        infoBinding.tvContent.text = article.content
    }
}
