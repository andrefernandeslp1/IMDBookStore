package com.example.imdbookstore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdbookstore.model.Livro
import com.example.imdbookstore.R

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
        
        try {
            Glide.with(holder.itemView.context)
                .load(livro.imagemUrl)
                .placeholder(R.drawable.placeholder_book)
                .error(R.drawable.error_book)
                .centerCrop()
                //.timeout(6000) // 6 seconds timeout
                .into(holder.capa)
        } catch (e: Exception) {
            holder.capa.setImageResource(R.drawable.error_book)
        }
            
        holder.itemView.setOnClickListener { onItemClick(livro) }
    }

    override fun getItemCount() = livros.size
}