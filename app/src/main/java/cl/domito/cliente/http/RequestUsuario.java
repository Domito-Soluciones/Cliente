package cl.domito.cliente.http;

import com.google.gson.JsonObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.usuario.Usuario;

public class RequestUsuario {

    private static JSONObject RESPUESTA;
    private static List<NameValuePair> PARAMS = new ArrayList<NameValuePair>();


    public static boolean loginUsuario(String reqUrl)
    {
        try {
            RESPUESTA = Utilidades.obtenerJsonObject(reqUrl);
            if(!RESPUESTA.getString("id").equals("0"))
            {
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public static JSONObject datosUsuario(String reqUrl) throws JSONException {
        try {
            RESPUESTA = Utilidades.obtenerJsonObject(reqUrl);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return RESPUESTA;
    }

    public static JSONObject obtenerServicioAsignado(String reqUrl) {
        try
        {
            RESPUESTA = Utilidades.obtenerJsonObject(reqUrl);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return RESPUESTA;
    }

    public static void desAsignarServicio(String reqUrl,String idServicio) {
        try
        {
            PARAMS.add(new BasicNameValuePair("id", idServicio));
            PARAMS.add(new BasicNameValuePair("conductor",Usuario.getInstance().getId()));
            Utilidades.enviarPost(reqUrl,PARAMS);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
