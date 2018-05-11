package com.exemple.android.ciclodevida

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {

        val CONTEUDO_TEXTVIEW = "CONTEUDO_TEXTVIEW"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState!= null ){
            if (savedInstanceState.containsKey(CONTEUDO_TEXTVIEW)){
                val conteudoTextView = savedInstanceState.getString(CONTEUDO_TEXTVIEW)
                tv_mensagem_log.text = conteudoTextView
            }
        }

        imprimir("onCreate")

//        Log.d("inicio","----------")
//
//        Log.d("ciclo de vida", "onCreate")


    }

    override fun onStart() {
        super.onStart()
        imprimir("onStart")
    }

    override fun onResume() {
        super.onResume()
        imprimir("onResume")
    }

    override fun onPause() {
        super.onPause()
        imprimir("onPause")
    }

    override fun onStop() {
        super.onStop()
        imprimir("onStop")
    }

    override fun onRestart() {
        super.onRestart()
        imprimir("onDstroy")
    }

    override fun onDestroy() {
        super.onDestroy()
       imprimir("onDstroy")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        imprimir("onSaveInstanceState")
        val conteudoTextView = tv_mensagem_log.text.toString()
        outState?.putString(CONTEUDO_TEXTVIEW, conteudoTextView)



    }

    fun imprimir (mensagem: String){
        Log.d("ciclo-de-vida",mensagem)
        tv_mensagem_log.append("$mensagem\n\n")
    }
}
