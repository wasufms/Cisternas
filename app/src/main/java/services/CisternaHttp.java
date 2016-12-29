package services;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Cisterna;





//import com.google.android.gms.maps.model.LatLng;

public class CisternaHttp {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	/*
    	 * PESQUISA http://130.206.125.57:1026/v1/contextEntityTypes/Cisterna
    	 *
    	 */
		/*Cisterna cisterna=new Cisterna(
				"00001101-0000-1000-8000-00805F9B34FB",
				220,
				16000,
				"-8.055861, -34.951083",
				700
				);*/


        CisternaHttp cisternaHttp=new CisternaHttp();
        List<Cisterna>cisternas;
        cisternas= cisternaHttp.getCisternas();
        for (Cisterna cisterna : cisternas) {
            System.out.println(cisterna.toString());
        }

        /*Cisterna cisterna1=new Cisterna();
        //cisterna1=cisternaHttp.getCisterna("00001101-0000-1000-8000-00805F9B34FB");
        cisterna1=cisternaHttp.getCisterna("123-321");
        if(cisterna1.getId()!=null){
			System.out.println(cisterna1.getVolume());
			cisterna1.setVolume(10);
			cisternaHttp.salvarCisterna(cisterna1);
			System.out.println(cisterna1.getVolume());
		}else{
			System.out.println("Sisterna n�o existe");
		}*/
    }

    //private static final String SERVIDOR="http://192.168.194.145:1026";
    private static final String SERVIDOR="http://130.206.125.57:1026";
    private Cisterna cisterna;

    public void salvarCisterna(Cisterna cisterna){
        try {
            this.cisterna=cisterna;
            enviarCisternaOrion();
        }catch (Exception e){
            System.out.println("ERRO"+e.toString());
            e.printStackTrace();
        }
    }

    //v2
    private boolean enviarCisternaOrion()throws Exception{

        String url =SERVIDOR+"/v1/updateContext";
        boolean sucesso=false;
        HttpURLConnection conexao=abrirConexao(url,"POST");
        OutputStream os=conexao.getOutputStream();
        os.write(cisternaToJsonBytes());
        os.flush();
        os.close();
        int responseCode=conexao.getResponseCode();
        if (responseCode==HttpURLConnection.HTTP_OK){
            sucesso=true;
        }
        conexao.disconnect();
        return sucesso;
    }


    private HttpURLConnection abrirConexao(String url,String method)throws Exception{
        URL urlCon=new URL(url);
        HttpURLConnection conexao=(HttpURLConnection)urlCon.openConnection();
        conexao.setRequestMethod(method);
        conexao.setReadTimeout(30000);
        conexao.setConnectTimeout(30000);
        conexao.setDoInput(true);
        conexao.setDoOutput(true);
        conexao.addRequestProperty("Content-type","application/json");
        conexao.connect();
        return conexao;
    }

    //v2
    public byte[] cisternaToJsonBytes(){
        String json;
        json="{"
                + " \"contextElements\": ["
                + "{"
                + " \"type\": \"Cisterna\","
                + "\"isPattern\": \"false\","
                + "\"id\": \""+this.cisterna.getId()+"\","
                + "\"attributes\": ["
                + "{"
                + "\"name\": \"altura\","
                + "\"type\": \"float\","
                + "\"value\": \""+this.cisterna.getAltura()+"\""
                + " },"
                + " {"
                + "\"name\": \"areaDaBase\","
                + "\"type\": \"float\","
                + "\"value\": \""+this.cisterna.getAreaBase()+"\""
                + "},"
                + "{"
                + "\"name\": \"capacidade\","
                + "\"type\": \"float\","
                + "\"value\": \""+this.cisterna.getCapacidade()+"\""
                + " },"
                + "{"
                + "\"name\": \"volume\","
                + "\"type\": \"float\","
                + "\"value\": \""+this.cisterna.getVolume()+"\""
                + "},"
                + "{"
                + "\"name\": \"position\","
                + "\"type\": \"geo:point\","
                + "\"value\": \""+this.cisterna.getLocalizacao()+"\""
                + "},"
                + "{"
                + "\"name\": \"distancia\","
                + "\"type\": \"float\","
                + "\"value\": \""+this.cisterna.getDistancia()+"\""
                + "}"
                + "]"
                + " }"
                + "],"
                + "\"updateAction\": \"APPEND\""
                + "}";

        System.out.println(json);
        //Log.d("was",json);
        return json.getBytes();
    }

    private static String bytesParaString(InputStream is)throws IOException {
        byte[]buffer=new byte[1024];
        ByteArrayOutputStream bufferzao=new ByteArrayOutputStream();
        int bytesLidos;
        while ((bytesLidos=is.read(buffer))!=-1){
            bufferzao.write(buffer,0,bytesLidos);
        }
        return new String(bufferzao.toByteArray(),"UTF-8");
    }


    public  Cisterna getCisterna(String id){

        String cisternaId;
        double cisternaAltura;
        double cisternaCapacidade;
        double cisternaDistancia;
        String cisternaPosition;
        double cisternaAreaBase;
        double cisternaVolume;
        Cisterna cisterna=new Cisterna();

        try{
            String urlRota=SERVIDOR+"/v1/contextEntities/type/Cisterna/id/"+id;
            URL url=new URL(urlRota);
            //Log.d("was->",urlRota);
            String result=bytesParaString(url.openConnection().getInputStream());
            JSONObject objResponse=new JSONObject(result);
            //System.out.println(objResponse.toString());
            JSONObject objContextElement=objResponse.getJSONObject("contextElement");
            cisternaId=objContextElement.getString("id");
            // System.out.println("id="+cisternaId);
            JSONArray arrAttributes=objContextElement.getJSONArray("attributes");
            //System.out.println("tam="+arrAttributes.length());

            JSONObject objAltura=arrAttributes.getJSONObject(0);
            cisternaAltura=objAltura.getDouble("value");
            System.out.println("Altura="+cisternaAltura);

            JSONObject objRaio=arrAttributes.getJSONObject(1);
            cisternaAreaBase=objRaio.getDouble("value");
            System.out.println("Área da base="+cisternaAreaBase);


            JSONObject objCapacidade=arrAttributes.getJSONObject(2);
            cisternaCapacidade=objCapacidade.getDouble("value");
            System.out.println("Capacidade="+cisternaCapacidade);

            JSONObject objDistancia=arrAttributes.getJSONObject(3);
            cisternaDistancia=objDistancia.getDouble("value");
            System.out.println("Distancia="+cisternaDistancia);

            JSONObject objPosition=arrAttributes.getJSONObject(4);
            cisternaPosition=objPosition.getString("value");
            System.out.println("Position="+cisternaPosition);



            JSONObject objVolume=arrAttributes.getJSONObject(6);
            cisternaVolume=objVolume.getDouble("value");
            System.out.println("Volume="+cisternaVolume);

            cisterna=new Cisterna(cisternaId,cisternaAltura,cisternaCapacidade,
                    cisternaPosition,cisternaVolume,cisternaDistancia,cisternaAreaBase);
            return cisterna;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro aqui was");
            //Log.d("was->","Cai aqui->"+e.toString());
        }
        return cisterna;
    }


    public boolean delete(String id) throws Exception {
        //http://130.206.125.57:1026/v2/entities/id?type=Cisterna

        String url =SERVIDOR+"/v2/entities/"+id+"?type=Cisterna";
        boolean sucesso=false;
        HttpURLConnection conexao=abrirConexao(url,"DELETE");
        OutputStream os=conexao.getOutputStream();
        // os.write(cisternaToJsonBytes());
        os.flush();
        os.close();
        Log.d("was","Resposta dentro http: "+HttpURLConnection.HTTP_OK);
        int responseCode=conexao.getResponseCode();
        if (responseCode!=HttpURLConnection.HTTP_OK){
            sucesso=false;
        }
        conexao.disconnect();
        return sucesso;
    }
    public Cisterna createSisterna(Cisterna cisterna){
        return null;
    }
    public void save(Cisterna cisterna){
        this.salvarCisterna(cisterna);
    }

    public  List<Cisterna> getCisternas(){

        List<Cisterna>cisternas;
        cisternas=new ArrayList<Cisterna>();
        String cisternaId;
        double cisternaAltura;
        double cisternaCapacidade;
        double cisternaDistancia;
        String cisternaPosition;
        double cisternaAreaBase;
        double cisternaVolume;
        Cisterna cisterna;

        try{
            String urlRota=SERVIDOR+"/v1/contextEntityTypes/Cisterna";
            URL url=new URL(urlRota);
            //Log.d("was->",urlRota);
            String result=bytesParaString(url.openConnection().getInputStream());
            JSONObject objResponse=new JSONObject(result);
            //System.out.println(objResponse.toString());
            JSONArray ArrContextResponses=objResponse.getJSONArray("contextResponses");

            // System.out.println(ArrContextResponses.length());
            for(int i=0;i<ArrContextResponses.length();i++){
                //cisterna=new Cisterna();
                JSONObject objCisterna=ArrContextResponses.getJSONObject(i);
                //JSONObject objContextElement=ArrContextResponses.getJSONObject(i);
                // System.out.println(objCisterna.toString());
                JSONObject objContextElement=objCisterna.getJSONObject("contextElement");
                cisternaId=objContextElement.getString("id");
                // System.out.println("id="+cisternaId);
                JSONArray arrAttributes=objContextElement.getJSONArray("attributes");
                //System.out.println("tam="+arrAttributes.length());

                JSONObject objAltura=arrAttributes.getJSONObject(0);
                cisternaAltura=objAltura.getDouble("value");
                // System.out.println("Altura="+cisternaAltura);

                JSONObject objAreaBase=arrAttributes.getJSONObject(1);
                cisternaAreaBase=objAreaBase.getDouble("value");
                // System.out.println("Área da base="+cisternaAreaBase);


                JSONObject objCapacidade=arrAttributes.getJSONObject(2);
                cisternaCapacidade=objCapacidade.getDouble("value");
                //  System.out.println("Capacidade="+cisternaCapacidade);

                JSONObject objDistancia=arrAttributes.getJSONObject(3);
                cisternaDistancia=objDistancia.getDouble("value");
                //System.out.println("Distancia="+cisternaDistancia);

                JSONObject objPosition=arrAttributes.getJSONObject(4);
                cisternaPosition=objPosition.getString("value");
                //  System.out.println("Position="+cisternaPosition);



                JSONObject objVolume=arrAttributes.getJSONObject(6);
                cisternaVolume=objVolume.getDouble("value");
                // System.out.println("Volume="+cisternaVolume);

                cisterna=new Cisterna(cisternaId,cisternaAltura,cisternaCapacidade,
                        cisternaPosition,cisternaVolume,cisternaDistancia,cisternaAreaBase);
                // return cisterna;
                cisternas.add(cisterna);
            }
            return cisternas;

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro aqui was");
            //Log.d("was->","Cai aqui->"+e.toString());
        }
        return null;
    }


}
