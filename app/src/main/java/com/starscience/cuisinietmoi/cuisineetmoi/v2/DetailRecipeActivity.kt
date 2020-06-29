package com.starscience.cuisinietmoi.cuisineetmoi.v2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Recipe
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.DRAWABLE_ICON_LIST
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.EXTRA_RECIPE
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.EXTRA_STEPS
import kotlin.random.Random

class DetailRecipeActivity : AppCompatActivity() {

    private lateinit var recipe_image : ImageView
    private lateinit var recipe_name : TextView
    private lateinit var recipe_postDate : TextView
    private lateinit var recipe_preparation : TextView
    private lateinit var recipe_cooking : TextView
    private lateinit var recipe_person : TextView
    private lateinit var recipe_ingredients : List<String>
    private lateinit var recipe_steps : List<String>

    private lateinit var recipe_ingredients_container: LinearLayoutCompat

    @SuppressLint("SetTextI18n")
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Details)
        setContentView(R.layout.activity_detail_recipe)

        val recipe = intent.getParcelableExtra<Recipe>(EXTRA_RECIPE)
        recipe_ingredients_container = findViewById(R.id.detail_recipe_ingredient_list)

        recipe_image = findViewById(R.id.recipe_image)
        recipe_name = findViewById(R.id.detail_recipe_name)
        recipe_preparation = findViewById(R.id.recipe_preparation)
        recipe_cooking = findViewById(R.id.recipe_cooking)
        recipe_person = findViewById(R.id.recipe_person)

        recipe_name.text = recipe.name
        recipe_preparation.text = recipe.preparation
        recipe_cooking.text = recipe.cooking
        recipe_person.text = recipe.person.toString()
        recipe_ingredients = recipe.ingredients ?: listOf()
        recipe_steps = recipe.steps ?: listOf()

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))
        ImageLoader.getInstance().displayImage(recipe.imageUrl, recipe_image)
        ingredientListView(recipe_ingredients)
    }

    fun ingredientListView(ingredientsList: List<String>){
        for( i in ingredientsList ){
            val view = layoutInflater.inflate(R.layout.detail_recipe_icon_item, null)
            val textView: TextView = view.findViewById(R.id.item_de_merde)
            val d = ContextCompat.getDrawable(this,  DRAWABLE_ICON_LIST[Random.nextInt(0, 4)])
            textView.text = i
            textView.setCompoundDrawablesWithIntrinsicBounds(d , null, null, null)
            recipe_ingredients_container.addView(view)
        }
    }

    fun onBackButton(v: View){
        finish()
    }

    fun onStartCooking(v: View){
        val intent = Intent(v.context, RecipeStepsActivity::class.java)
            .putExtra(EXTRA_STEPS, ArrayList(recipe_steps))
        startActivity(intent)
    }
}
