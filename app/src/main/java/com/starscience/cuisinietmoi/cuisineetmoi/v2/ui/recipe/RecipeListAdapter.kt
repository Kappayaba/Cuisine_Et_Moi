package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.databinding.ItemRecipeBinding
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Recipe
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.database.AppDatabase
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.getParentActivity
import java.util.*
import kotlin.collections.ArrayList

class RecipeListAdapter(private val itemClickListener: (Recipe) -> Unit) : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {
    private lateinit var recipeListFull: List<Recipe>
    private lateinit var recipeListFiltered: ArrayList<Recipe>
    private lateinit var userLikes: List<String>
    private lateinit var context: Fragment


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecipeBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_recipe, parent, false)
        val db = Room.databaseBuilder(parent.getParentActivity()?.applicationContext!!, AppDatabase::class.java, "cuisine_et_moi").build()
        return ViewHolder(binding, db)
    }

    override fun getItemCount(): Int {
        return if(::recipeListFiltered.isInitialized) recipeListFiltered.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recipeListFiltered[position]
        holder.bind(item, userLikes)
        holder.itemView.setOnClickListener{ itemClickListener(item) }
    }

    fun updateRecipeList(recipeList: List<Recipe>, userLikes: List<String>){
        this.recipeListFiltered = ArrayList(recipeList)
        this.recipeListFull = ArrayList(recipeListFiltered)
        this.userLikes = userLikes
        Log.w("TESTHAMZA", "$userLikes")
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemRecipeBinding, private val db: AppDatabase) : RecyclerView.ViewHolder(binding.root){

        private val viewModel = RecipeViewModel(db)

        fun bind(recipe: Recipe, userLikes: List<String>){
            viewModel.bind(recipe, recipe.id in userLikes)
            Log.w("IMAGE", "${recipe.name}, ${recipe.imageUrl}")
            binding.viewModel = viewModel
        }
    }

    fun filterRecipeList() = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            Log.w("TESTHAMZA", "$constraint")
            val filterHolder = arrayListOf<Recipe>()

            if(constraint == null || constraint.isEmpty()){
                filterHolder.addAll(recipeListFull)
            }
            else{
                val PATTERN = constraint.toString().toLowerCase(Locale.ROOT).trim()

                recipeListFull.forEach{recipe ->
                    if(recipe.name?.toLowerCase(Locale.ROOT)?.contains(PATTERN) == true){
                        filterHolder.add(recipe)
                    }
                }
            }
            Log.w("RECIPEHAMZA", "$recipeListFull")
            val results = FilterResults()
            results.values = filterHolder
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            recipeListFiltered.clear()
            @Suppress("UNCHECKED_CAST")
            recipeListFiltered.addAll(results?.values as List<Recipe>)
            notifyDataSetChanged()
        }
    }
}