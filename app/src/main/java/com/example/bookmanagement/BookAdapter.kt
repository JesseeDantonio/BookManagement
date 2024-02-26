package com.example.bookmanagement

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class BookAdapter(var books: List<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val context: Context = itemView.context

        fun bind(book: Book, position: Int) {
            titleTextView.text = book.titre
            authorTextView.text = book.auteur ?: "Auteur inconnu"

            titleTextView.setOnClickListener {
                val clickedBook = books[position]
                val intent = Intent(context, VisualizationActivity::class.java)
                // Ajouter des données supplémentaires à l'Intent si nécessaire
                intent.putExtra("BOOK_TITLE", clickedBook.titre)
                intent.putExtra("BOOK_AUTHOR", clickedBook.auteur)
                intent.putExtra("BOOK_RANG", clickedBook.rang)
                intent.putExtra("BOOK_RESERVATION", clickedBook.reservations)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item_layout, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position], position)
    }

    override fun getItemCount(): Int {
        return books.size
    }

}

