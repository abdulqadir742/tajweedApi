package media.uqab.apidemo

import android.graphics.Color
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import media.uqab.apidemo.databinding.ItemAyahBinding
import media.uqab.quranapi.QuranApi
import media.uqab.quranapi.TajweedApi
import media.uqab.quranapi.Verse

class VerseAdapter: RecyclerView.Adapter<VerseAdapter.AyahHolder>() {
    private val TAG = "Adapter"
    private var verses: List<Verse> = listOf()
    private var spannedVerse: MutableList<Spanned> = mutableListOf()
    @Volatile private var cached = false

    class AyahHolder(private val binding: ItemAyahBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(verse: Verse) {
            if (adapterPosition % 2 == 0) {
                val color = ContextCompat.getColor(binding.root.context, R.color.light_color)
                binding.root.setBackgroundColor(color)
            } else binding.root.setBackgroundColor(Color.WHITE)

            binding.surahInfoLayout.visibility = if (verse.verseNo == 1) {
                when (verse.surahNo){
                    1 -> {
                        binding.taAudh.visibility = View.VISIBLE
                        binding.basmalah.visibility = View.GONE
                    }
                    9 -> {
                        binding.taAudh.visibility = View.GONE
                        binding.basmalah.visibility = View.GONE
                    }
                    else -> {
                        binding.taAudh.visibility = View.GONE
                        binding.basmalah.visibility = View.VISIBLE
                    }
                }
                View.VISIBLE
            } else View.GONE


            binding.surahName.text = QuranApi.getSurahInfo(verse.surahNo).nameAr
            binding.verseNo.text = verse.verseNo.toString()
            binding.textView.text = TajweedApi.getTajweedColored(verse.verseIndo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAyahBinding.inflate(inflater, parent, false)
        return AyahHolder(binding)
    }

    override fun onBindViewHolder(holder: AyahHolder, position: Int) {
        holder.bind(verses[position])
        /*
        if (cached) {
            holder.textView.text = spannedVerse[position]
            Log.d(TAG, "onBindViewHolder: using cached")
        } else {
            Log.d(TAG, "onBindViewHolder: using raw")
            val verse = verses[position].verseIndo
            holder.textView.text = TajweedApi.getTajweedColored(verse)
        }*/
    }

    private fun getItem(position: Int): Spanned {
        return spannedVerse[position]
    }
    override fun getItemCount(): Int {
        return verses.size
    }

    fun submitList(verses: List<Verse>) {
        this.verses = verses
        notifyDataSetChanged()
//        Thread {
//            // make a cache for spannedString.. It will make UI load faster
//            for (verse in verses) { spannedVerse.add(TajweedApi.getTajweedColored(verse.verseIndo)) }
//            cached = true
//        }.start()
    }
}