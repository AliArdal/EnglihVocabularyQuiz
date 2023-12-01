package com.example.engilishvocabularyquiz

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var totalQuestionsTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var ansA: Button
    private lateinit var ansB: Button
    private lateinit var ansC: Button
    private lateinit var ansD: Button
    private lateinit var submitBtn: Button
    private lateinit var geriDonBtn: ImageView

    private var score = 0
    private val totalQuestion = QuestionAnswer.question.size
    private var currentQuestionIndex = 0
    private var selectedAnswer = ""
    private val wrongAnswersList = mutableListOf<Int>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        totalQuestionsTextView = findViewById(R.id.total_question)
        questionTextView = findViewById(R.id.question)
        ansA = findViewById(R.id.ans_A)
        ansB = findViewById(R.id.ans_B)
        ansC = findViewById(R.id.ans_C)
        ansD = findViewById(R.id.ans_D)
        submitBtn = findViewById(R.id.submit_btn)
        geriDonBtn = findViewById(R.id.geri_don_btn)

        ansA.setOnClickListener(this)
        ansB.setOnClickListener(this)
        ansC.setOnClickListener(this)
        ansD.setOnClickListener(this)
        submitBtn.setOnClickListener(this)
        geriDonBtn.setOnClickListener { goBack() }

        totalQuestionsTextView.text = "Soru: 1"

        loadNewQuestion()
    }

    override fun onClick(view: View) {
        ansA.setBackgroundColor(Color.WHITE)
        ansB.setBackgroundColor(Color.WHITE)
        ansC.setBackgroundColor(Color.WHITE)
        ansD.setBackgroundColor(Color.WHITE)

        val clickedButton = view as Button
        if (clickedButton.id == R.id.submit_btn) {
            if (selectedAnswer == QuestionAnswer.correctAnswers[currentQuestionIndex]) {
                score++
            } else {
                wrongAnswersList.add(currentQuestionIndex)
            }
            currentQuestionIndex++
            loadNewQuestion()
        } else {
            selectedAnswer = clickedButton.text.toString()
            clickedButton.setBackgroundColor(Color.parseColor("#FFA500"))
        }
    }

    private fun loadNewQuestion() {
        if (currentQuestionIndex == totalQuestion) {
            finishQuiz()
            return
        }

        val currentQuestionNumber = currentQuestionIndex + 1
        totalQuestionsTextView.text = "$currentQuestionNumber / $totalQuestion"

        questionTextView.text = QuestionAnswer.question[currentQuestionIndex]
        ansA.text = QuestionAnswer.choices[currentQuestionIndex][0]
        ansB.text = QuestionAnswer.choices[currentQuestionIndex][1]
        ansC.text = QuestionAnswer.choices[currentQuestionIndex][2]
        ansD.text = QuestionAnswer.choices[currentQuestionIndex][3]
    }

    private fun finishQuiz() {
        val passStatus: String = if (score > totalQuestion * 0.60) {
            "Geçtiniz"
        } else {
            "Geçemediniz"
        }

        val wrongAnswersMessage = buildWrongAnswersMessage()

        val finalMessage = "$passStatus - $score/$totalQuestion\n\n$wrongAnswersMessage"

        AlertDialog.Builder(this)
            .setTitle("Sonuç")
            .setMessage(finalMessage)
            .setPositiveButton("Tekrar") { _, _ -> restartQuiz() }
            .setCancelable(false)
            .show()
    }

    private fun buildWrongAnswersMessage(): String {
        if (wrongAnswersList.isEmpty()) {
            return "Yanlış cevaplanan soru yok."
        }

        val sb = StringBuilder("Yanlış cevaplanan sorular:\n")

        for (index in wrongAnswersList) {
            val correctAnswer = QuestionAnswer.correctAnswers[index]
            sb.append("${index + 1}. Soru - Doğru Cevap: $correctAnswer\n")
            // İstediğiniz ek bilgileri buraya ekleyebilirsiniz.
        }

        return sb.toString()
    }

    private fun restartQuiz() {
        score = 0
        currentQuestionIndex = 0
        wrongAnswersList.clear()
        loadNewQuestion()
    }

    private fun goBack() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
            loadNewQuestion()
        }
    }
}
