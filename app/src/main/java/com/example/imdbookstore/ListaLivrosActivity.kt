package com.example.imdbookstore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdbookstore.R
import com.example.imdbookstore.db.*

class ListaLivrosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_livros)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewLivros)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = LivrariaDatabaseHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            LivrariaDatabaseHelper.TABLE_LIVROS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val livros = mutableListOf<Livro>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(LivrariaDatabaseHelper.COLUMN_ID))
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow(LivrariaDatabaseHelper.COLUMN_TITULO))
            val autor = cursor.getString(cursor.getColumnIndexOrThrow(LivrariaDatabaseHelper.COLUMN_AUTOR))
            val editora = cursor.getString(cursor.getColumnIndexOrThrow(LivrariaDatabaseHelper.COLUMN_EDITORA))
            val isbn = cursor.getString(cursor.getColumnIndexOrThrow(LivrariaDatabaseHelper.COLUMN_ISBN))
            val descricao = cursor.getString(cursor.getColumnIndexOrThrow(LivrariaDatabaseHelper.COLUMN_DESCRICAO))
            val imagemUrl = cursor.getString(cursor.getColumnIndexOrThrow(LivrariaDatabaseHelper.COLUMN_IMAGEM_URL))

            livros.add(Livro(id, titulo, autor, editora, isbn, descricao, imagemUrl))
        }

        cursor.close()
        db.close()

        val adapter = LivroAdapter(livros) { livro ->
            val intent = Intent(this, DetalhesLivroActivity::class.java)
            intent.putExtra("livro_id", livro.id)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}

// Adapter para RecyclerView
class LivroAdapter(
    private val livros: List<Livro>,
    private val onItemClick: (Livro) -> Unit
) : RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {

    inner class LivroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo = view.findViewById<TextView>(R.id.textViewTitulo)
        val autor = view.findViewById<TextView>(R.id.textViewAutor)
        val capa = view.findViewById<ImageView>(R.id.imageViewCapa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_livro, parent, false)
        return LivroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = livros[position]
        holder.titulo.text = livro.titulo
        holder.autor.text = livro.autor
        Glide.with(holder.itemView.context).load(livro.imagemUrl).into(holder.capa)
        holder.itemView.setOnClickListener { onItemClick(livro) }
    }

    override fun getItemCount() = livros.size
}

