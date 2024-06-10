package com.dostonbek.tictactoeonline

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dostonbek.tictactoeonline.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playOfflineBtn.setOnClickListener {
            createOfflineGame()
        }
        binding.createOnlineGame.setOnClickListener {
            createOnlineGame()
        }
        binding.joinGameBtn.setOnClickListener {
            joinOnlineGame()
        }

    }

   private fun joinOnlineGame(){
        var gameId = binding.onlineGameId.text.toString()
        if(gameId.isEmpty()){
            binding.onlineGameId.setError("Please enter game ID")
            return
        }
        GameData.myId = "O"
        Firebase.firestore.collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener {
                val model = it?.toObject(GameModel::class.java)
                if(model==null){
                    binding.onlineGameId.setError("Please enter valid game ID")
                }else{
                    model.gameStatus = GameStatus.JOINED
                    GameData.saveGameModel(model)
                    startGame()
                }
            }

    }

    fun createOnlineGame(){
        GameData.myId = "X"
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.CREATED,
                gameId = Random.nextInt(1000..9999).toString()
            )
        )
        startGame()
    }



    private fun createOfflineGame() {
        GameData.saveGameModel(
            GameModel(gameStatus = GameStatus.JOINED)
        )
        startGame()
    }

    private fun startGame() {
        startActivity(Intent(this, GameActivity::class.java))
    }

}