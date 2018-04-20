package com.exemple.android.buscadorgithub

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.security.ProviderInstaller
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext

class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_buscar){
            buscarNoGithub()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ProviderInstaller.installIfNeeded(getApplicationContext());

        val sslContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(null, null, null)

        val engine = sslContext.createSSLEngine()

        exercicioJson()
    }

    fun buscarNoGithub(){
        val buscarGithub = et_busca.text.toString()
        val urlBuscaGithub = NetworkUtils.construirUrl(buscarGithub)
        tv_url.text = urlBuscaGithub.toString()

        if(urlBuscaGithub != null) {
            val task = GithubBuscaTask();
            task.execute(urlBuscaGithub);
        }

    }


    fun exibirResultado(){
        tv_github_resultado.visibility = View.VISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pb_aguarde.visibility = View.INVISIBLE
    }

    fun exibirProgressbar(){
        tv_github_resultado.visibility = View.INVISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pb_aguarde.visibility = View.VISIBLE
    }

    fun exibirMensagemErro(){
        tv_github_resultado.visibility = View.INVISIBLE
        tv_mensagem_erro.visibility = View.VISIBLE
        pb_aguarde.visibility = View.INVISIBLE
    }


    fun exercicioJson(){
        var dadosJson ="""{
	"Temperatura":{

	"minima" : 11.34°C,
	"maxima" : 19.01°C
	},
	"Clima":{
	"id":801,
	"condicao":"nuvens",
	"descricao":"poucas nuvens"
	},
	"pressao":1023.51,
	"umidade":87
}"""

        val objetoPrevisao = JSONObject(dadosJson)
        val clima = objetoPrevisao.getJSONObject("Clima")
        val condicao =clima.getString("condicao")
        Log.d("a condição é :","$condicao")
    }

    inner class GithubBuscaTask : AsyncTask <URL, Void, String>(){

        override fun onPreExecute() {
            exibirProgressbar()
        }

        override fun doInBackground(vararg params: URL?): String? {

            try {
                val url = params[0]
                val resultado = NetworkUtils.obterRespostaDaUrlHttp(url!!);
                return  resultado
            }catch (ex : Exception){
                ex.printStackTrace();
            }
            return null;
        }

        override fun onPostExecute(resultado: String?) {
            if (resultado != null){
            tv_github_resultado.text = resultado
                exibirResultado()
            }else{
                exibirMensagemErro()
            }
        }
    }
}
