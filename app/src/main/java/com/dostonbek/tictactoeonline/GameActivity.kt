package com.dostonbek.tictactoeonline

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dostonbek.tictactoeonline.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGameBinding
    private var gameModel: GameModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GameData.fetchGameModel()

        binding.grid0.setOnClickListener(this)
        binding.grid1.setOnClickListener(this)
        binding.grid2.setOnClickListener(this)
        binding.grid3.setOnClickListener(this)
        binding.grid4.setOnClickListener(this)
        binding.grid5.setOnClickListener(this)
        binding.grid6.setOnClickListener(this)
        binding.grid7.setOnClickListener(this)
        binding.grid8.setOnClickListener(this)

        binding.startGameBtn.setOnClickListener {


            startGame()

            binding.startGameBtn.visibility = View.INVISIBLE

        }
        GameData.gameModel.observe(this) {
            gameModel = it
            setUi()
        }

    }

    private fun setUi() {
        gameModel?.apply {

            binding.btn0.text = filledPos[0]
            binding.btn1.text = filledPos[1]
            binding.btn2.text = filledPos[2]
            binding.btn3.text = filledPos[3]
            binding.btn4.text = filledPos[4]
            binding.btn5.text = filledPos[5]
            binding.btn6.text = filledPos[6]
            binding.btn7.text = filledPos[7]
            binding.btn8.text = filledPos[8]

            binding.startGameBtn.visibility = View.VISIBLE

            binding.gameStatusText.text =
                when (gameStatus) {
                    GameStatus.CREATED -> {
                        binding.startGameBtn.visibility = View.INVISIBLE
                        "Game Id:" + gameId

                    }

                    GameStatus.JOINED -> {

                        "Click on start game"
                    }

                    GameStatus.INPROGRESS -> {

                        binding.startGameBtn.visibility = View.INVISIBLE
                        when (GameData.myId) {
                            currentPlayer -> "Your Turn"


                            else -> "$currentPlayer Turn"

                        }
                    }

                    GameStatus.FINISHED -> {
                        val alertDialogBuilder = AlertDialog.Builder(this@GameActivity)
                        alertDialogBuilder.apply {
                            if (winner.isNotEmpty()) {
                                setTitle("$winner Won")
                            } else
                                setTitle("No Winner")
                            setMessage("Very Good, keep doing it")
                            setPositiveButton("Reset") { dialog, _ ->

                                dialog.dismiss()
                                startGame()
                            }
                            setNegativeButton("No thanks") { dialog, _ ->
                                dialog.dismiss()
                                finish()
                            }
                            setCancelable(false)
                        }
                        val alertDialog = alertDialogBuilder.create()
                        alertDialog.show()


                        if (winner.isNotEmpty()) {
                            when (GameData.myId) {


                                winner -> "You won"
                                else -> winner + "Won"
                            }
                        }
                        else "No Winner"
                    }
                }


        }

    }

    fun startGame() {
        gameModel?.apply {
            updateGameData(GameModel(gameId = gameId, gameStatus = GameStatus.INPROGRESS))


        }

    }

    fun updateGameData(model: GameModel) {
        GameData.saveGameModel(model)
    }

    fun checkForWinner() {
        val winnerPos = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6),
        )
        gameModel?.apply {
            for (i in winnerPos) {
                if (filledPos[i[0]] == filledPos[i[1]] && filledPos[i[1]] == filledPos[i[2]] && filledPos[i[0]].isNotEmpty()) {
                    gameStatus = GameStatus.FINISHED
                    winner = filledPos[i[0]]

                }
            }
            if (filledPos.none() { it.isEmpty() }) {
                gameStatus = GameStatus.FINISHED
            }
            updateGameData(this)
        }
    }


    override fun onClick(v: View?) {
        gameModel?.apply {
            if (gameStatus != GameStatus.INPROGRESS) {
                Toast.makeText(applicationContext, "Game not started", Toast.LENGTH_SHORT).show()
                return
            }
            if (gameId!= "-1" && currentPlayer != GameData.myId) {
                Toast.makeText(applicationContext, "Not Your Turn", Toast.LENGTH_SHORT).show()
                return


            }
            // game started
            val clickedPas = (v?.tag as String).toInt()
            if (filledPos[clickedPas].isEmpty()) {
                filledPos[clickedPas] = currentPlayer
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                checkForWinner()
                updateGameData(this)

            }

        }
    }
}