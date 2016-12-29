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
import services.CisternaService;

public class AddActivity extends AppCompatActivity {
    EditText edtId;
    EditText edtDistancia;
    EditText edtAltura;
    EditText edtAreaBase;
    Button btnSalvar;
    TextView txtResposta;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //estou mudando para
        edtId=(EditText)findViewById(R.id.edtId);
        edtAltura=(EditText)findViewById(R.id.edtAltura);
        edtDistancia=(EditText)findViewById(R.id.edtDistancia);
        //jhhjhhj
        edtAreaBase=(EditText)findViewById(R.id.edtAreaBase);
        txtResposta=(TextView)findViewById(R.id.txtResposta);
        btnSalvar=(Button)findViewById(R.id.btSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

    }
    private void salvar(){
        String id=edtId.getText().toString();
        double distancia=Double.parseDouble(edtDistancia.getText().toString());
        double altura=Double.parseDouble(edtAltura.getText().toString());
        double areaBase=Double.parseDouble(edtAreaBase.getText().toString());
        Cisterna cisterna=new Cisterna(id,distancia,altura,areaBase);
        new SalvarTask().execute(cisterna);
    }

    private class SalvarTask extends AsyncTask<Cisterna, String, Boolean> {
        protected void onPreExecute() {
            progress= ProgressDialog.show(AddActivity.this, "Salvando...","Espere");//show progress
        }
        protected Boolean  doInBackground(Cisterna... params) {
            CisternaService cisternaService=new CisternaService();
            boolean resp=cisternaService.save(params[0]);
            Log.d("was->","Volume: "+resp);
            return resp;
        }
        protected void onPostExecute(Boolean args) {
            progress.dismiss();
            if (args==true){
                txtResposta.setText("Cisterna Salva");
                Toast.makeText(AddActivity.this,"Cisterna Salva",Toast.LENGTH_LONG).show();
            }else{
                txtResposta.setText("Erro ao salvar");
                Toast.makeText(AddActivity.this,"Ocorreu algum erro",Toast.LENGTH_LONG).show();
            }


        }
    }
}
