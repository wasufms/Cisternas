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

public class DeleteActivity extends AppCompatActivity {
    Button btnExcluir;
    EditText edtId;
    TextView txtResposta;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        edtId=(EditText)findViewById(R.id.edtId);
        btnExcluir=(Button)findViewById(R.id.btDelete);
        txtResposta=(TextView)findViewById(R.id.txtResposta);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluir(edtId.getText().toString());
            }
        });
    }
    private void excluir(String id){
        new ExcluirTask().execute(id);
    }

    private class ExcluirTask extends AsyncTask<String, String, Boolean> {
        protected void onPreExecute() {
            progress= ProgressDialog.show(DeleteActivity.this, "Exclindo...","Espere");//show progress
        }
        protected Boolean  doInBackground(String... params) {
            CisternaService cisternaService=new CisternaService();
            boolean resp=false;
            resp= cisternaService.delete(params[0]);
            Log.d("was->","Resposta Exclusao: "+resp);
            return resp;
           /* if(cisterna.getId()!=null){


                return true;
                //Toast.makeText(ledControl.this,"Cisterna Salva",Toast.LENGTH_LONG).show();
                //Log.d("was->","Volume: "+cisterna.getVolume());
            }else{
                Log.d("was->","Sisterna não existe");
                return false;
            }*/
        }
        protected void onPostExecute(Boolean args) {
            //progressBar.setVisibility(View.GONE);
            progress.dismiss();
            if (args==true){
                txtResposta.setText("Cisterna Excluída");
                Toast.makeText(DeleteActivity.this,"Cisterna Excluída",Toast.LENGTH_LONG).show();
            }else{

                txtResposta.setText("Erro na Exclusão");
                Toast.makeText(DeleteActivity.this,"Ocorreu algum erro",Toast.LENGTH_LONG).show();
            }


        }
    }
}
