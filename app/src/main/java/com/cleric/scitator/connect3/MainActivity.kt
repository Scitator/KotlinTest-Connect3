
package com.cleric.scitator.connect3

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var numberOfClicks: Int = 0
    var clickedCells: MutableMap<ImageView, Pair<Int, Int>> = hashMapOf()
    val winningPositons: Array<List<Int>> = arrayOf(listOf(0, 1, 2), listOf(3, 4, 5) , listOf(6, 7, 8),
                                                    listOf(0, 3, 6), listOf(1, 4, 7) , listOf(2, 5, 8),
                                                    listOf(0, 4, 8), listOf(2, 4, 6))
    private var _isPlayable: Boolean = true
    var isPlayable: Boolean
        get() {
            return _isPlayable
        }
        set(value) {
            if (value) {            // New game
                clearGamefield()
                reloadGamefieldButton.animate().alpha(0f).rotation(360f).setDuration(1000)
            } else {                // Game finished
                reloadGamefieldButton.animate().alpha(1f).rotation(-360f).setDuration(1000)
            }
            _isPlayable = value
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        reloadGamefieldButton.setOnClickListener { isPlayable = true }
        //prepareGrid()
    }

    fun dropIn(view: View) {
        if (!isPlayable) return
        var selectedImageView: ImageView = view as ImageView
        if (selectedImageView !in clickedCells) {
            clickedCells.put(selectedImageView, Pair(++numberOfClicks%2, gridLayout.indexOfChild(view)))

            selectedImageView.translationY = -1000f;
            if (numberOfClicks%2 == 1) {
                selectedImageView.setImageResource(R.drawable.yellow)
            }
            else {
                selectedImageView.setImageResource(R.drawable.red)
            }
            selectedImageView.alpha = 1f
            selectedImageView.animate().translationYBy(1000f).rotationBy(180f).setDuration(300)
            if (clickedCells.size > 4) checkGameState()
            if (clickedCells.size == 9) isPlayable = false
        }
    }

    fun checkGameState() {
        val firstPlayerCells: List<Int> = clickedCells.filter { it.value.first == 1 }.map { it.value.second }
        val secondPlayerCells: List<Int> = clickedCells.filter { it.value.first == 0 }.map { it.value.second }
        for (row in winningPositons) {
            if (firstPlayerCells.containsAll(row)) {
                isPlayable = false
                Toast.makeText(applicationContext, "Player 1 win!", Toast.LENGTH_LONG).show()
                return
            }
            if (secondPlayerCells.containsAll(row)) {
                isPlayable = false
                Toast.makeText(applicationContext, "Player 2 win!", Toast.LENGTH_LONG).show()
                return
            }
        }
    }

    fun clearGamefield() {
        for (selectedImageView in clickedCells.keys) {
            selectedImageView.setImageResource(android.R.color.transparent)
        }
        clickedCells.clear()
    }

    fun prepareGrid() {
        for (index in 0..gridLayout.childCount-1) {
            var selectedView: View = gridLayout.getChildAt(index)
            if (selectedView is ImageView ){
                //selectedView.requestLayout()
                var params:GridLayout.LayoutParams = GridLayout.LayoutParams()
                params.height = windowManager.defaultDisplay.width/3
                params.width = windowManager.defaultDisplay.height/3
                params.columnSpec = GridLayout.spec(index%3)
                params.rowSpec = GridLayout.spec(index/3)
                params.setGravity(Gravity.CENTER)
                selectedView.layoutParams = params
                //selectedView.requestLayout()
            }

        }
    }
}

