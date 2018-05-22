package com.exemple.android.buscadorgithub

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import android.text.Editable
import android.text.TextWatcher
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

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String>, SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_exibir_url)){
            val exibir_url = sharedPreferences.getBoolean(key, resources.getBoolean(R.bool.pref_exibir_url_padrao))
            tv_url.visibility = if(exibir_url) View.VISIBLE else View.GONE
        }else if (key == getString(R.string.pref_cor_fundo)){
            val corFundo = sharedPreferences.getString(getString(R.string.pref_cor_fundo), getString(R.string.pref_cor_fundo_padrao))

            val backgroundColor = selecionaCorDeFundo(corFundo)
            window.decorView.setBackgroundColor(backgroundColor)
        }
    }

    companion object {
        var URL_BUSCA = "URL_BUSCA"
        val BUSCA_GITHUB_LOADER_ID = 1000
        val CONTEUDO_TEXTVIEW = "CONTEUDO_TEXTVIEW"


    }

    var cacheResultado :String? = null

    override fun onCreateLoader(id: Int, parametros: Bundle?): Loader<String> {
        val loader =  object : AsyncTaskLoader<String>(this){

            override fun onStartLoading() {
                super.onStartLoading()
                if (parametros == null){
                    return
                }
                exibirProgressbar()
                if (cacheResultado != null){
                    deliverResult(cacheResultado)
                }
                forceLoad()
            }

            override fun loadInBackground(): String? {
                try {
                    var urlBusca = parametros?.getString(URL_BUSCA)
                    val url = URL(urlBusca)
                    val resultado = NetworkUtils.obterRespostaDaUrlHttp(url!!);
                    return  resultado

                }catch (ex : Exception){
                    ex.printStackTrace();
                }
                return null;
            }

            override fun deliverResult(data: String?) {
                super.deliverResult(data)
                cacheResultado = data
            }

        }
        return loader
    }

    override fun onLoadFinished(loader: Loader<String>?, resultado: String?) {
        if (resultado != null){
            tv_github_resultado.text = resultado
            exibirResultado()
        }else{
            exibirMensagemErro()
        }
    }

    override fun onLoaderReset(loader: Loader<String>?) {

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_buscar){
            buscarNoGithub()
        }
        if (item?.itemId == R.id.action_configuracoes){
            val intent = Intent(this, ConfiguracaoActivity::class.java)
            startActivity(intent)
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


        val sharePreference = PreferenceManager.getDefaultSharedPreferences(this)
        sharePreference.registerOnSharedPreferenceChangeListener(this)
        val exibirUrl = sharePreference.getBoolean(getString(R.string.pref_exibir_url), resources.getBoolean(R.bool.pref_exibir_url_padrao))
        if (exibirUrl == false){
            tv_url.visibility = View.INVISIBLE
        }




        et_busca.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cacheResultado = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        supportLoaderManager.initLoader(BUSCA_GITHUB_LOADER_ID, null, this)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CONTEUDO_TEXTVIEW)) {
                val conteudoURLGit = savedInstanceState.getString(CONTEUDO_TEXTVIEW)
                tv_url.text = conteudoURLGit
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this)
    }

    fun buscarNoGithub(){
        val buscarGithub = et_busca.text.toString()
        val urlBuscaGithub = NetworkUtils.construirUrl(buscarGithub)
        tv_url.text = urlBuscaGithub.toString()

        if(urlBuscaGithub != null) {
//            val task = GithubBuscaTask();
//            task.execute(urlBuscaGithub);

            val parametros = Bundle()
            parametros.putString(URL_BUSCA, urlBuscaGithub.toString())

            supportLoaderManager.restartLoader(BUSCA_GITHUB_LOADER_ID, parametros, this)
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val conteudoUrlGit = tv_url.text.toString()
        outState?.putString(CONTEUDO_TEXTVIEW, conteudoUrlGit)
    }

    fun selecionaCorDeFundo(corFundo: String): Int {
        return when (corFundo) {
            getString(R.string.pref_cor_fundo_branco_valor) -> ContextCompat.getColor(this, R.color.fundoBranco)
            getString(R.string.pref_cor_fundo_verde_valor) -> ContextCompat.getColor(this, R.color.fundoVerde)
            getString(R.string.pref_cor_fundo_azul_valor) -> ContextCompat.getColor(this, R.color.fundoAzul)
            else -> ContextCompat.getColor(this, R.color.fundoBranco)
        }
    }


}
