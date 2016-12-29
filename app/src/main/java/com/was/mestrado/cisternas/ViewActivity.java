package com.was.mestrado.cisternas;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import model.Cisterna;
import services.CisternaHttp;
import services.CisternaService;

public class ViewActivity extends AppCompatActivity {
    Button btnBuscar;
    EditText edtId;
    TextView txtResposta;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        edtId=(EditText)findViewById(R.id.edtId);
        btnBuscar=(Button)findViewById(R.id.btBuscar);
        txtResposta=(TextView)findViewById(R.id.txtResposta);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar(edtId.getText().toString());
            }
        });
    }
    private void buscar(String id){
        //this.txtResposta.setText(id);
        new BuscaTask().execute(id);
    }

    private class BuscaTask extends AsyncTask<String, String, Boolean>{
        protected void onPreExecute() {
            progress= ProgressDialog.show(ViewActivity.this, "Buscando...","Espere");//show progress
        }
        protected Boolean  doInBackground(String... params) {
            CisternaService cisternaService=new CisternaService();
            Cisterna cisterna=new Cisterna();
            cisterna=cisternaService.getCisterna(params[0]);
            if(cisterna.getId()!=null){
                Log.d("was->","Volume: "+cisterna.getVolume());
                return true;
            }else{
                Log.d("was->","Sisterna não existe");
                return false;
            }
        }
        protected void onPostExecute(Boolean args) {
            progress.dismiss();
            if (args==true){
                txtResposta.setText("Ok");
                Toast.makeText(ViewActivity.this,"Cisterna Buscada",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ViewActivity.this,"Ocorreu algum erro",Toast.LENGTH_LONG).show();
            }


        }
    }
}
