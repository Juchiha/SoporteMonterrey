package cf.josedavidgiron.soportemonterrey.clases;

public class FirebaseUsers {

    private String strCorreo;
    private String strNombre;
    private String strPassw_;
    private String strRol___;
    private String strId____;

    public FirebaseUsers(String strCorreo, String strNombre, String strPassw_, String strRol___, String strId____) {
        this.strCorreo = strCorreo;
        this.strNombre = strNombre;
        this.strPassw_ = strPassw_;
        this.strRol___ = strRol___;
        this.strId____ = strId____;
    }

    public FirebaseUsers() {
        /* Constructor vacio */
    }

    public String getStrCorreo() {
        return strCorreo;
    }

    public void setStrCorreo(String strCorreo) {
        this.strCorreo = strCorreo;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrPassw_() {
        return strPassw_;
    }

    public void setStrPassw_(String strPassw_) {
        this.strPassw_ = strPassw_;
    }

    public String getStrRol___() {
        return strRol___;
    }

    public void setStrRol___(String strRol___) {
        this.strRol___ = strRol___;
    }

    public String getStrId____() {
        return strId____;
    }

    public void setStrId____(String strId____) {
        this.strId____ = strId____;
    }
}
