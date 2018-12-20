package cl.domito.cliente.dominio;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Usuario {

    private static Usuario instance;

    public static int BUSCAR_PARTIDA = 0;
    public static int BUSCAR_DESTINO = 1;

    public static int SELECCIONAR_UBICACION = 0;
    public static int SELECCIONAR_PLACES = 1;
    public static int SELECCIONAR_MAPA = 2;

    private String id;
    private String nombre;
    private String nick;
    private String password;
    private String cliente;
    private String celular;
    private String direccion;
    private boolean activo;
    private boolean recordarSession;
    private double latitud;
    private double longitud;
    private boolean buscaServicio;
    private String placeIdOrigen;
    private String placeIdOrigenNombre;
    private String placeIdDestino;
    private String placeIdDestinoNombre;
    private boolean conectado;
    private int tipoBusqueda;
    private boolean busquedaRealizada;
    private String idViaje;
    private List<LatLng> latLngs;

    public static synchronized Usuario getInstance(){
        if(instance == null){
            instance = new Usuario();
        }
        return instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isRecordarSession() {
        return recordarSession;
    }

    public void setRecordarSession(boolean recordarSession) {
        this.recordarSession = recordarSession;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public boolean isBuscaServicio() {
        return buscaServicio;
    }

    public void setBuscaServicio(boolean buscaServicio) {
        this.buscaServicio = buscaServicio;
    }

    public String getPlaceIdOrigen() {
        return placeIdOrigen;
    }

    public void setPlaceIdOrigen(String placeIdOrigen) {
        this.placeIdOrigen = placeIdOrigen;
    }

    public String getPlaceIdOrigenNombre() {
        return placeIdOrigenNombre;
    }

    public void setPlaceIdOrigenNombre(String placeIdOrigenNombre) {
        this.placeIdOrigenNombre = placeIdOrigenNombre;
    }

    public String getPlaceIdDestino() {
        return placeIdDestino;
    }

    public void setPlaceIdDestino(String placeIdDestino) {
        this.placeIdDestino = placeIdDestino;
    }

    public String getPlaceIdDestinoNombre() {
        return placeIdDestinoNombre;
    }

    public void setPlaceIdDestinoNombre(String placeIdDestinoNombre) {
        this.placeIdDestinoNombre = placeIdDestinoNombre;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public int getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(int tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public boolean isBusquedaRealizada() {
        return busquedaRealizada;
    }

    public void setBusquedaRealizada(boolean busquedaRealizada) {
        this.busquedaRealizada = busquedaRealizada;
    }

    public String getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(String idViaje) {
        this.idViaje = idViaje;
    }

    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }


}