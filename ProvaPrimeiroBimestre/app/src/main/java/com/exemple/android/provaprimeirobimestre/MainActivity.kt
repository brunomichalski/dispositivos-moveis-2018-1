package com.exemple.android.provaprimeirobimestre

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_calcular) {

            calcular()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }



    fun calcular() {

        try {
            val base = et_base.text.toString().toInt()
            val altura = et_altura.text.toString().toInt()
            val profundidade = et_profundidade.text.toString().toInt()

            val area = base * altura

            val volume = base * altura * profundidade

            tv_fArea.text = "Área: $area"

            tv_fVolume.text = "Volume: $volume"
        }catch (ex :Exception){
            ex.printStackTrace()
            Toast.makeText(this, "INFORME TODOS OS CAMPOS PARA REALIZAR OS CALCULOS!", Toast.LENGTH_LONG).show()
        }

    }

}
