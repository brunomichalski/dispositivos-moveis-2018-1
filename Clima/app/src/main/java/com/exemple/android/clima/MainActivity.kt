package com.exemple.android.clima

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.test.TouchUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View

import android.text.format.DateFormat
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

import java.util.*

class MainActivity : AppCompatActivity() {

  /*  val dadosClimaFicticios = listOf("Hoje - Céu limpo - 17°C / 15°C",

            "Amanhã - Nublado - 19°C / 15°C",

            "Quinta - Chuv  oso - 30°C / 11°C",

            "Sexta - Chuva com raios - 21°C / 9°C",

            "Sábado - Chuva com raios - 16°C / 7°C",

            "Domingo - Chuvoso - 16°C / 8°C",

            "Segunda - Parcialmente nublado - 15°C / 10°C",

            "Ter, Mai 24 - Vai curintia - 16°C / 18°C",

            "Qua, Mai 25 - Nublado - 19°C / 15°C",

            "Qui, Mai 26 - Tempestade - 30°C / 11°C",

            "Sex, Mai 27 - Furacão - 21°C / 9°C",

            "Sáb, Mai 28 - Meteóro - 16°C / 7°C",

            "Dom, Mai 29 - Apocalipse - 16°C / 8°C",

            "Seg, Mai 30 - Pós-apocalipse - 15°C / 10°C")*/



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


       /* for (clima in dadosClimaFicticios){
            dados_clima.append("$clima \n\n\n")
        }*/

         carregarDadosDoClima()
    }

    fun carregarDadosDoClima(){
        val localizacao = ClimaPreferencias.getLocalizacaoSalva(this)
        val url = NetworkUtils.construirUrl(localizacao)
        BuscarClimaTask().execute(url)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clima, menu)
        return true
    }

    fun exibirResultado(){
        dados_clima.visibility = View.VISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pb_aguarde.visibility = View.INVISIBLE
    }

    fun exibirMensagemErro(){
        tv_mensagem_erro.visibility = View.VISIBLE
        dados_clima.visibility = View.INVISIBLE
        pb_aguarde.visibility = View.INVISIBLE
    }

    fun exibirProgressBar(){
        pb_aguarde.visibility = View.VISIBLE
        dados_clima.visibility = View.INVISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.acao_atualizar){
            dados_clima.text = ""
            carregarDadosDoClima()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    inner  class BuscarClimaTask : AsyncTask <URL, Void, String>(){

        override fun onPreExecute() {
            exibirProgressBar()
        }

        override fun doInBackground(vararg params: URL?): String? {
           try {
               val url = params[0]
               val result = NetworkUtils.obterRespostaDaUrlHttp(url!!)
               return result
           }catch (ex : Exception){
               ex.printStackTrace()
           }
            return null
        }

        override fun onPostExecute(result: String?) {
            if (result != null){
                infomacoes(result)
                exibirResultado()
            }else{
                exibirMensagemErro()
            }
        }

        fun infomacoes (resultado : String?){

            val json = JSONObject(resultado)
            val lista = json.getJSONArray("list")
            for (i in 0 until  lista.length()){
                val lista1 = lista.getJSONObject(i)
                val dataLong = lista1.getString("dt")
                val data = converterData(dataLong)
                val main = lista1.getJSONObject("main")
                val temp = main.getString("temp")
                val umidade = main.getString("humidity")
                val clima = lista1.getJSONArray("weather")
                val clima1 = clima.getJSONObject(0)
                val descClima = clima1.getString("description")
                dados_clima.append("Data: $data \n" +
                                    "temperatura: $temp \n" +
                                    "Umidade: $umidade \n" +
                                    "Clima: $descClima \n\n\n")

            }

        }

        fun converterData(data: String):CharSequence?{
            val dataHoraMilissegundo: Long = (java.lang.Long.valueOf(data)) * 1000
            val dataHora = Date(dataHoraMilissegundo)
            return DateFormat.format("dd/MM/yyyy HH:mm", dataHora)
        }

    }

}
