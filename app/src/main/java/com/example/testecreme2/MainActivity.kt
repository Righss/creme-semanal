package com.example.testecreme2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.DayOfWeek
import java.time.LocalDate

@SuppressLint("SetTextI18n")
@Suppress("SpellCheckingInspection")
class MainActivity : AppCompatActivity() {

    //Inicializa as variaveis (UI)
    private lateinit var buttonalt: Button
    private lateinit var butDelete: ImageButton
    private lateinit var tvdom: TextView
    private lateinit var tvseg: TextView
    private lateinit var tvter: TextView
    private lateinit var tvqua: TextView
    private lateinit var tvqui: TextView
    private lateinit var tvsex: TextView
    private lateinit var tvsab: TextView
    private lateinit var tvcreme: TextView
    private lateinit var butsalv: Button
    private lateinit var circle_dom: View
    private lateinit var circle_seg: View
    private lateinit var circle_ter: View
    private lateinit var circle_qua: View
    private lateinit var circle_qui: View
    private lateinit var circle_sex: View
    private lateinit var circle_sab: View
    private lateinit var tvProxCreme: TextView
    private lateinit var switchOntem: SwitchCompat


    private var flagList = mutableListOf(
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    ) //Lista para salvar qual creme foi utilzado em qual dia da semana

    private lateinit var dayViews: List<TextView>
    var ontemFlag: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("firstRun", false)

        val flagListString = sharedPref.getString(
            "flagList",
            ""
        )   //Carrega a lista de flags salvas no SharedPreferences
        if (flagListString != null) {                               //Se a lista não estiver vazia, converte para uma lista mutável
            flagList = if (flagListString.isNotEmpty()) {
                flagListString.split(",").toMutableList()
            } else {                                                //Se a lista estiver vazia, inicializa com uma lista vazia
                mutableListOf("", "", "", "", "", "", "")
            }
        }

        editor.apply()

        //Inicializa as variaveiss

        buttonalt = findViewById(R.id.buttonalt)
        butsalv = findViewById(R.id.butsalv)
        butDelete = findViewById(R.id.butDelete)
        switchOntem = findViewById(R.id.switchOntem)
        tvdom = findViewById(R.id.tvdom)
        tvseg = findViewById(R.id.tvseg)
        tvter = findViewById(R.id.tvter)
        tvqua = findViewById(R.id.tvqua)
        tvqui = findViewById(R.id.tvqui)
        tvsex = findViewById(R.id.tvsex)
        tvsab = findViewById(R.id.tvsab)
        tvcreme = findViewById(R.id.tvcreme)
        circle_dom = findViewById(R.id.circle_dom)
        circle_seg = findViewById(R.id.circle_seg)
        circle_ter = findViewById(R.id.circle_ter)
        circle_qua = findViewById(R.id.circle_qua)
        circle_qui = findViewById(R.id.circle_qui)
        circle_sex = findViewById(R.id.circle_sex)
        circle_sab = findViewById(R.id.circle_sab)
        tvProxCreme = findViewById(R.id.tvProxCreme)

        dayViews = listOf(tvseg, tvter, tvqua, tvqui, tvsex, tvsab, tvdom)

        fundoDiaSemana()


        val estadotextocreme = sharedPref.getString(
            "estadotextocreme",
            "Nenhum"
        ) //Carrega o texto salvo no SharedPreferences
        tvcreme.text = estadotextocreme

        val tvProxCremeString = sharedPref.getString(
            "tvProxCreme",
            "Não sei"
        ) //Carrega o texto salvo no SharedPreferences
        tvProxCreme.text = tvProxCremeString

        buttonalt.setOnLongClickListener { //Funçao para limpar o texto
            tvcreme.text = "Nenhum"
            true
        }

        buttonalt.setOnClickListener {  //Funçao para mudar o texto do creme
            if (tvcreme.text == "Nenhum" || tvcreme.text == "Costas") {
                tvcreme.text = "Bolinhas"
            } else if (tvcreme.text == "Bolinhas") {
                tvcreme.text = "Costas"
            }

        }

        switchOntem.isChecked = false   //Botao para selecionar o dia anterior
        switchOntem.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                salvarOntem()
            } else {
                fundoDiaSemana()
                ontemFlag = false
            }
        }


        carregarStados()
    }

    //Funçao para mudar o fundo do dia da semana
    private fun fundoDiaSemana(){
        val dia = DayOfWeek.from(LocalDate.now().dayOfWeek)
        val dianum = dia.value

        // Clear background for all days
        dayViews.forEach { it.setBackgroundResource(0) }

        // Set background for the current day
        dayViews[dianum - 1].setBackgroundResource(R.drawable.round_corner_view)
    }


    //Funçao para salvar qual creme foi utilzado em qual dia
    fun salvarStado(view: View?) {
        val dia = DayOfWeek.from(LocalDate.now().dayOfWeek)
        var dianum = (dia.value - 1)
        if(!ontemFlag){
            when (tvcreme.text) {
                "Costas" -> {
                    val newFlagString = "Cos"
                    flagList[dianum] = newFlagString
                    tvProxCreme.text = "Bolinhas"
                }
                "Bolinhas" -> {
                    val newFlagString = "Bol"
                    flagList[dianum] = newFlagString
                    tvProxCreme.text = "Costas"
                }
                else -> {
                    val newFlagString = "Nada"
                    flagList[dianum] = newFlagString
                    tvProxCreme.text = "Não sei"
                }
            }
        }
        if(ontemFlag){
            if(dianum == 0) {
                dianum = 7
            }
            when (tvcreme.text) {
                    "Costas" -> {
                        val newFlagString = "Cos"
                        flagList[dianum - 1] = newFlagString
                        tvProxCreme.text = "Bolinhas"
                    }
                    "Bolinhas" -> {
                        val newFlagString = "Bol"
                        flagList[dianum - 1] = newFlagString
                        tvProxCreme.text = "Costas"
                    }
                    else -> {
                        val newFlagString = "Nada"
                        flagList[dianum - 1] = newFlagString
                        tvProxCreme.text = "Não sei"
                    }
                }
        }
        carregarStados()
    }


    private fun salvarOntem(){  //Selecia o dia anterior
            val dia = DayOfWeek.from(LocalDate.now().dayOfWeek)
            val dianum = dia.value

            // Clear background for all days
            dayViews.forEach { it.setBackgroundResource(0) }

            // Set background for the current day
        if(dianum == 1) {
            dayViews[6].setBackgroundResource(R.drawable.round_corner_view)
        } else {
            dayViews[dianum - 2].setBackgroundResource(R.drawable.round_corner_view)
        }

            ontemFlag = true

    }


    fun deletarEstados(view: View?) {
        for (i in 0 until 7) {
            flagList[i] = ""
        }
        carregarStados()
    }

    private fun carregarStados() { //Funçao para carregar qual creme foi utilzado em qual dia
        val circleIds = listOf(
            R.id.circle_seg, R.id.circle_ter, R.id.circle_qua,
            R.id.circle_qui, R.id.circle_sex, R.id.circle_sab, R.id.circle_dom
        ) // List of circle ImageViews IDs

        for (i in 0 until 7) { // Iterate through days of the week
            val imageView = findViewById<ImageView>(circleIds[i])
            val flag = flagList[i]

            if (flag == "Cos") {
                imageView.setImageResource(R.drawable.circulo_costas) // Set red circle drawable
            } else if (flag == "Bol") {
                imageView.setImageResource(R.drawable.circulo_bolinhas) // Set blue circle drawable
            } else {
                // Handle cases where the flag is empty or invalid (optional)
                imageView.setImageResource(R.drawable.circulo_nada) // Set a default drawable
            }
        }
    }





    //Funçao para salvar no SharedPreferences
    override fun onPause() {
        super.onPause()

        val editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit()

        editor.apply {
            putString("estadotextocreme", tvcreme.text.toString())
            putString("flagList", flagList.joinToString(","))
            putString("tvProxCreme", tvProxCreme.text.toString())
            apply() // or commit()
        }

    }


}