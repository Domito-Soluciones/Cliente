package cl.domito.cliente.usuario;

public class Usuario {

    private static Usuario instance;

    private String id;
    private String nombre;
    private boolean activo;

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


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
