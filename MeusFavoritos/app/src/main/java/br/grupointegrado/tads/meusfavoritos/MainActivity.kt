package br.grupointegrado.tads.meusfavoritos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for  (favorito in Favotiros.getLista()){
            Lista_favoritos.append("$favorito \n\n\n");
        }
    }


}
