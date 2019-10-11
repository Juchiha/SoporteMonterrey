package cf.josedavidgiron.soportemonterrey.clases;

public class FirebaseTickets {
    private String strId____;
    private String strMotivo;
    private String strDesc__;
    private String strFoto__;
    private String strPrio__;
    private String strIdUs__;
    private String strEstado;
    private String strFechaC;
    private String strFechaS;
    private String strDireccion;

    public FirebaseTickets(String strId____, String strMotivo, String strDesc__, String strFoto__, String strPrio__, String strIdUs__, String strEstado, String strFechaC, String strFechaS, String strDireccion) {
        this.strId____ = strId____;
        this.strMotivo = strMotivo;
        this.strDesc__ = strDesc__;
        this.strFoto__ = strFoto__;
        this.strPrio__ = strPrio__;
        this.strIdUs__ = strIdUs__;
        this.strEstado = strEstado;
        this.strFechaC = strFechaC;
        this.strFechaS = strFechaS;
        this.strDireccion = strDireccion;
    }

    public  FirebaseTickets(){
        /*Constructor Vacio*/
    }

    public String getStrDireccion() {
        return strDireccion;
    }

    public void setStrDireccion(String strDireccion) {
        this.strDireccion = strDireccion;
    }

    public String getStrId____() {
        return strId____;
    }

    public void setStrId____(String strId____) {
        this.strId____ = strId____;
    }

    public String getStrMotivo() {
        return strMotivo;
    }

    public void setStrMotivo(String strMotivo) {
        this.strMotivo = strMotivo;
    }

    public String getStrDesc__() {
        return strDesc__;
    }

    public void setStrDesc__(String strDesc__) {
        this.strDesc__ = strDesc__;
    }

    public String getStrFoto__() {
        return strFoto__;
    }

    public void setStrFoto__(String strFoto__) {
        this.strFoto__ = strFoto__;
    }

    public String getStrPrio__() {
        return strPrio__;
    }

    public void setStrPrio__(String strPrio__) {
        this.strPrio__ = strPrio__;
    }

    public String getStrIdUs__() {
        return strIdUs__;
    }

    public void setStrIdUs__(String strIdUs__) {
        this.strIdUs__ = strIdUs__;
    }

    public String getStrEstado() {
        return strEstado;
    }

    public void setStrEstado(String strEstado) {
        this.strEstado = strEstado;
    }

    public String getStrFechaC() {
        return strFechaC;
    }

    public void setStrFechaC(String strFechaC) {
        this.strFechaC = strFechaC;
    }

    public String getStrFechaS() {
        return strFechaS;
    }

    public void setStrFechaS(String strFechaS) {
        this.strFechaS = strFechaS;
    }
}
