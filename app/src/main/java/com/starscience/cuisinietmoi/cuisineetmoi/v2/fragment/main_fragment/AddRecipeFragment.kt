package com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.main_fragment


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.starscience.cuisinietmoi.cuisineetmoi.v2.MainActivity
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.component.ViewModelFactory
import com.starscience.cuisinietmoi.cuisineetmoi.v2.databinding.FragmentAddRecipeBinding
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe.AddRecipeViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.DividerView
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.PICK_IMAGE_REQUEST
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.getFileExtensions
import kotlinx.android.synthetic.main.item_recipe.*


class AddRecipeFragment : AppCompatActivity(), TextView.OnEditorActionListener {
    // Variables
    lateinit var binding: FragmentAddRecipeBinding
    lateinit var viewModel: AddRecipeViewModel

    private var snackbar: Snackbar? = null

    private lateinit var recipe_infos_toggle_off: ConstraintLayout
    private lateinit var recipe_infos_toggle_on: ConstraintLayout

    private lateinit var recipe_title_text: EditText
    private lateinit var recipe_time_text: EditText
    private lateinit var recipe_cook_text: EditText
    private lateinit var recipe_person_text: EditText

    private lateinit var recipe_ingredients_list: LinearLayout
    private lateinit var recipe_ingredients_toggle: ToggleButton
    private lateinit var recipe_add_ingredients_button: Button
    private lateinit var recipe_ingredients_toggle_shadow: View
    private lateinit var ingredient_array: ArrayList<String>

    private lateinit var recipe_instruction_list: LinearLayout
    private lateinit var recipe_instruction_toggle: ToggleButton
    private lateinit var recipe_add_instruction_button: Button
    private lateinit var recipe_instruction_toggle_shadow: View
    private lateinit var instruction_array: ArrayList<String>

    // Android Init
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AddRecipe)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_add_recipe)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(AddRecipeViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer { value -> if(value == null) hideError() else showError(value) })
        viewModel.recipeIngredients.observe(this, Observer { value -> ingredient_array = value })
        viewModel.recipeInstruction.observe(this, Observer { value -> instruction_array = value })
        viewModel.publishedRecipe.observe(this, Observer { result -> if (result == true) publish()})
        binding.viewModel = viewModel

        recipe_infos_toggle_on = findViewById(R.id.recipe_infos_toggle_on)
        recipe_infos_toggle_off = findViewById(R.id.recipe_infos_toggle_off)

        recipe_title_text = findViewById(R.id.recipe_title_text)
        recipe_time_text = findViewById(R.id.recipe_time_text)
        recipe_cook_text = findViewById(R.id.recipe_cook_text)
        recipe_person_text = findViewById(R.id.recipe_person_text)

        recipe_ingredients_list = findViewById(R.id.recipe_ingredient_list)
        recipe_ingredients_toggle = findViewById(R.id.recipe_ingredient_toggle)
        recipe_add_ingredients_button = findViewById(R.id.recipe_add_ingredients_button)
        recipe_ingredients_toggle_shadow = findViewById(R.id.recipe_ingredient_toggle_shadow)


        recipe_instruction_list = findViewById(R.id.recipe_instruction_list)
        recipe_instruction_toggle = findViewById(R.id.recipe_instruction_toggle)
        recipe_add_instruction_button = findViewById(R.id.recipe_add_instruction_button)
        recipe_instruction_toggle_shadow = findViewById(R.id.recipe_instruction_toggle_shadow)
    }

    private fun publish() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun hideError() {
        snackbar?.dismiss()
    }

    private fun showError(value: String) {
        snackbar = Snackbar.make(binding.root, value, Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }

    // Top toolbar option
    fun home_button(v: View) {
        finish()
    }

    fun editRecipe(v: View){
        recipe_infos_toggle_off.visibility = View.GONE
        recipe_infos_toggle_on.visibility = View.VISIBLE
    }

    fun finishEdit(v: View){
        viewModel.setTitle(recipe_title_text.text.toString())
        viewModel.setTime(recipe_time_text.text.toString())
        viewModel.setCook(recipe_cook_text.text.toString())
        viewModel.setPerson(recipe_person_text.text.toString())
        recipe_infos_toggle_on.visibility = View.GONE
        recipe_infos_toggle_off.visibility = View.VISIBLE
    }

    fun choose_picture(v: View){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image from here"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            Log.w("Test", "$uri")
            viewModel.uploadImage(uri, getFileExtensions(this, uri))
        }
    }

    fun check_toggle(v: View){
        val status = (v as ToggleButton).isChecked
        if (status) {
            if((v.parent as View).tag as String == "Ingredients"){
                show_view(recipe_ingredients_toggle, recipe_add_ingredients_button, recipe_ingredients_list, recipe_ingredients_toggle_shadow, ingredient_array)
            }
            else if((v.parent as View).tag as String == "Instructions"){
                show_view(recipe_instruction_toggle, recipe_add_instruction_button, recipe_instruction_list, recipe_instruction_toggle_shadow, instruction_array)
            }

        }
        else {
            if ((v.parent as View).tag as String == "Ingredients"){
                hide_view(recipe_ingredients_toggle, recipe_add_ingredients_button, recipe_ingredients_list, recipe_ingredients_toggle_shadow, ingredient_array)
            }
            else if((v.parent as View).tag as String == "Instructions"){
                hide_view(recipe_instruction_toggle, recipe_add_instruction_button, recipe_instruction_list, recipe_instruction_toggle_shadow, instruction_array)
            }
        }
    }

    private fun show_view(recipe_toggle: ToggleButton, recipe_add_button: Button, list: LinearLayout, shadow: View, array: ArrayList<String>) {
        recipe_toggle.setCompoundDrawables(null, null, null, null)
        recipe_add_button.visibility = View.VISIBLE
        list.visibility = View.VISIBLE
        shadow.visibility = View.VISIBLE
        for( i in 0 until  array.size){
            val itemView = layoutInflater.inflate(R.layout.item_ingredient, null)
            itemView.tag = i
            val divider = layoutInflater.inflate(R.layout.divider_layout, null)
            val textView = itemView.findViewById<TextView>(R.id.item_text)
            textView.text = array[i]
            list.addView(itemView)
            if(i != array.size - 1){
                list.addView(divider)
            }
        }
    }

    private fun hide_view(recipe_toggle: ToggleButton, recipe_add_button: Button, list: LinearLayout, shadow: View, array: ArrayList<String>) {
        val right: Drawable? = ContextCompat.getDrawable(this, R.drawable.chevron_down)
        right?.setBounds(0, 0, 60, 60)
        recipe_add_button.visibility = View.GONE
        list.visibility = View.GONE
        shadow.visibility = View.GONE
        list.removeAllViews()
        recipe_toggle.setCompoundDrawables(null, null, right, null)
    }

    fun add_item(v: View){
        if ((v.parent as View).tag == "Ingredients"){
            val view = layoutInflater.inflate(R.layout.ingredient_hover_item, null)
            val editText: EditText = view.findViewById(R.id.add_element_edit_text)
            editText.setOnEditorActionListener(this)
            recipe_ingredients_list.addView(view)
        }
        else if ((v.parent as View).tag == "Instructions"){
            val view = layoutInflater.inflate(R.layout.ingredient_hover_item, null)
            val editText: EditText = view.findViewById(R.id.add_element_edit_text)
            editText.setOnEditorActionListener(this)
            recipe_instruction_list.addView(view)
        }
    }

    fun delete_item(v: View){
        val name = (v.parent.parent.parent as View).tag
        if(name == "Ingredients"){
            viewModel.deleteIngredients((v.parent as View).tag as Int)
            hide_view(recipe_ingredients_toggle, recipe_add_ingredients_button, recipe_ingredients_list, recipe_ingredients_toggle_shadow, ingredient_array)
            show_view(recipe_ingredients_toggle, recipe_add_ingredients_button, recipe_ingredients_list, recipe_ingredients_toggle_shadow, ingredient_array)
        }
        else if (name == "Instructions"){
            viewModel.deleteInstruction((v.parent as View).tag as Int)
            hide_view(recipe_instruction_toggle, recipe_add_instruction_button, recipe_instruction_list, recipe_instruction_toggle_shadow, instruction_array)
            show_view(recipe_instruction_toggle, recipe_add_instruction_button, recipe_instruction_list, recipe_instruction_toggle_shadow, instruction_array)
        }
    }



    // Keyboard Listener
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when (actionId) {
            EditorInfo.IME_ACTION_GO -> {
                val imm: InputMethodManager = baseContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(recipe_ingredients_list.windowToken, 0)

                val nam = (v?.parent?.parent?.parent as View).tag as String

                val view = layoutInflater.inflate(R.layout.item_ingredient, null)
                val textView = view.findViewById<TextView>(R.id.item_text)
                val divider = layoutInflater.inflate(R.layout.divider_layout, null)

                val parent = v?.parent as ViewGroup

                textView.text = v?.text

                if( textView.text.replace("//s".toRegex(), "") == ""){
                    return false
                }
                else{
                    if(nam == "Ingredients"){
                        viewModel.addIngredients(v.text.toString())
                        return ValidInput(view, recipe_ingredients_list, ingredient_array, divider, parent)
                    }
                    else if(nam == "Instructions"){
                        viewModel.addInstruction(v.text.toString())
                        ValidInput(view, recipe_instruction_list, instruction_array, divider, parent)
                    }
                }
            }
        }
        return false
    }

    private fun ValidInput(view: View, recipeList: LinearLayout, array: ArrayList<String>, dividerView: View, parent: ViewGroup) : Boolean{
        view.tag = array.size - 1
        dividerView.id = view.tag as Int

        recipeList.removeView(parent)

        return if(view.tag == 0){
            recipeList.addView(view)
            true
        }
        else{
            recipeList.addView(dividerView)
            recipeList.addView(view)
            true
        }
    }
}