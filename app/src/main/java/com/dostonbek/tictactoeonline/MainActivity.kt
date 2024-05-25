package com.dostonbek.tictactoeonline

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dostonbek.tictactoeonline.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playOfflineBtn.setOnClickListener {
            createOfflineGame()
        }

    }

     fun createOfflineGame() {
        GameData.saveGameModel(
            GameModel(gameStatus = GameStatus.JOINED)
        )
        startGame()
    }

    fun startGame() {
        startActivity(Intent(this, GameActivity::class.java))
    }

}