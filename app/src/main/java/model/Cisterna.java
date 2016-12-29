package model;
import java.io.Serializable;
/*import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import services.CisternaService;*/

//import com.google.android.gms.maps.model.LatLng;

public class Cisterna implements Serializable {
    private static final long serialVersionUID = 1L;
    //diametro=3,2m
    //Altura=2,20m
    //capacidade=16 mil litros
    //volume pi*r2*h

    private String id;
    private double altura;//altura real da cisterna
    private double raio;//NÃO USADA
    private double capacidade;//calculado;
    private double volume;//volume do sensor areaDaBase*Altura
    private double distancia;//distancia entre o Sensor e o espelho d�gua CHEIO
    private String position;//latitude longitude da cisterna
    private double areaBase;


    public Cisterna(String id, double distancia, double altura,double areaBase){
        this.id=id;
        this.distancia=distancia;
        this.altura=altura;
        this.areaBase=areaBase;
        this.capacidade=areaBase*altura/1000;
    }

    public double getAreaBase() {
        return areaBase;
    }

    public void setAreaBase(double areaBase) {
        this.areaBase = areaBase;
    }

    //private LatLng localizacao;
    String localizacao;

    public Cisterna(){

    }

    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        this.raio = raio;
    }




    public Cisterna(String id, double altura, double capacidade,
                    String localizacao, double volume, double distancia, double areaBase ) {
        super();
        this.id = id;
        this.altura = altura;
        this.capacidade = capacidade;
        this.volume = volume;
        this.localizacao = localizacao;
        this.distancia=distancia;
        this.areaBase=areaBase;
    }




    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public double getAltura() {
        return altura;
    }
    public void setAltura(double altura) {
        this.altura = altura;
    }
    public double getCapacidade() {
        return capacidade;
    }
    public void setCapacidade(double capacidade) {
        this.capacidade = capacidade;
    }
    public double getVolume() {
        return volume;
    }
    public void setVolume(double volume) {
        this.volume = volume;
    }
    public String getLocalizacao() {
        return localizacao;
    }
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double calculaVolume(double distanciaArduino){

        double alturaAgua;
        double volume=0;
        alturaAgua=(this.altura+this.distancia)-distanciaArduino;
        volume=Math.PI*raio*raio*alturaAgua;
        //this.volume=volume;
        return volume;
        //return distancia;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Cisterna [id=" + id + ", altura=" + altura + "cm, Área da Base=" + areaBase + "cm2, capacidade=" + capacidade+
                " litros, distancia=" + distancia + "cm, volume=" + volume + " litros, localizacao=" + localizacao + "]";
    }

	/*public String toJson() {
		//return "[" + id + "," + altura + "," + areaBase + "," + capacidade+"," + distancia + "," + volume + "," + localizacao + "]";
		//return "{id:" + id + ", altura:" + altura + ", area_base:" + areaBase + ", capacidade:" + capacidade+
		//		", distancia:" + distancia + ", volume:" + volume + ", localizacao:" + localizacao + "}";
		Gson gson = new Gson();
		String jsonInString = gson.toJson(this);
		return jsonInString;

	}*/

	/*public static void main(String[] args){
		//String testeJason="{\"cisternas\":[{\"id\": \"123-321\",\"nome\": \"was\",\"lat\": \"-8.055861, -34.951083\" },{\"id\": \"123-321\",\"nome\": \"was\",\"lat\": \"-8.055861, -34.951083\"}]}";
		//System.out.println(testeJason);
		Cisterna cisterna=new Cisterna();
		CisternaService cisternaService=new CisternaService();
		cisterna=cisternaService.getCisterna("123-321");
		List<Cisterna>cisternas=new ArrayList<Cisterna>();
		cisternas.add(cisternaService.getCisterna("123-321"));
		cisternas.add(cisternaService.getCisterna("00001101-0000-1000-8000-00805F9B34FB"));
		Gson gson=new Gson();
		System.out.println(gson.toJson(cisternas));
	}*/

}
