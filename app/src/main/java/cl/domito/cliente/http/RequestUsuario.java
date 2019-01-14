package cl.domito.cliente.http;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.dominio.Usuario;

public class RequestUsuario {

    private static JSONObject RESPUESTA;
    private static List<NameValuePair> PARAMS = new ArrayList<NameValuePair>();


    public static boolean loginUsuario(String reqUrl,List<NameValuePair> params)
    {
        try {
            if(Usuario.getInstance().isConectado()) {
                RESPUESTA = Utilidades.enviarPost(reqUrl,params);
                if (!RESPUESTA.getString("id").equals("0")) {
                    return true;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public static JSONObject datosUsuario(String reqUrl,List<NameValuePair> params) throws JSONException {
        try {
            if(Usuario.getInstance().isConectado()) {
                RESPUESTA = Utilidades.enviarPost(reqUrl,params);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return RESPUESTA;
    }
}
