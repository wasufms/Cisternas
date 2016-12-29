package services;

import java.util.List;

import model.Cisterna;

public class CisternaService {
    private CisternaHttp cisternaHttp;

    public CisternaService(){
        cisternaHttp=new CisternaHttp();
    }

    public List<Cisterna> getCisternas(){
        List<Cisterna>cisternas=cisternaHttp.getCisternas();
        return cisternas;
    }

    public Cisterna getCisterna(String id){
        return cisternaHttp.getCisterna(id);
    }

    public boolean delete(String id){
        boolean resposta=false;
        try {
            Cisterna cisterna=cisternaHttp.getCisterna(id);
            if(cisterna.getId()!=null){
                cisternaHttp.delete(id);
                resposta=true;
            }else {
                resposta=false;
            }
            return resposta;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resposta;
    }

    public boolean save(Cisterna cisterna){
        cisternaHttp.save(cisterna);
        return true;

    }
}
