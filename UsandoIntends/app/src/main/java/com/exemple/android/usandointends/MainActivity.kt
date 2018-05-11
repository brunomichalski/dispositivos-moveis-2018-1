package com.exemple.android.usandointends

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_enviar.setOnClickListener{
            val activitiDestino = SegundaActivity::class.java
            val context = this

            val intentSegundaActivity = Intent(context, activitiDestino)

            val mensagem = et_mensagem.text.toString()
            intentSegundaActivity.putExtra(Intent.EXTRA_TEXT, mensagem)
            startActivity(intentSegundaActivity)

        }
        btn_abrir_site.setOnClickListener(){
            val acao = Intent.ACTION_VIEW
            val  dado = Uri.parse("http://www.globo.com.br")

            val intentImplicito = Intent(acao, dado)

            if (intentImplicito.resolveActivity(packageManager) != null){
                startActivity(intentImplicito)
            }
        }

    }

    fun abrir_mapa(view:View){
        val endereco = "Av. Irmãos Pereira, 670 - Centro, Campo Mourão - PR"

        val builder = Uri.Builder()
                .scheme("geo")
                .path("0,0")
                .appendQueryParameter("q",endereco)
        val uriEndereco = builder.build()
        val intent = Intent(Intent.ACTION_VIEW, uriEndereco)

        if (intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }


    fun compartilhar (view : View){
        val tipo = "text/plain"
        val tituto = "Compartilhar mensagem"
        val text = et_mensagem.text.toString()
        val intentCompartilhar = ShareCompat.IntentBuilder
                .from(this)
                .setType(tipo)
                .setChooserTitle(tituto)
                .setType(text)
                .intent

        if (intentCompartilhar.resolveActivity(packageManager) != null)   {
            startActivity(intentCompartilhar)
        }
    }
}
