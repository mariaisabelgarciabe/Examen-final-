package model;

public class Predio {
    private String npn;
    private String municipio;
    private String direccion;
    private String numeroFicha;

    public Predio(String npn, String municipio, String direccion, String numeroFicha) {
        this.npn = npn;
        this.municipio = municipio;
        this.direccion = direccion;
        this.numeroFicha = numeroFicha;
    }

    public String getNpn() { return npn; }
    public void setNpn(String npn) { this.npn = npn; }
    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getNumeroFicha() { return numeroFicha; }
    public void setNumeroFicha(String numeroFicha) { this.numeroFicha = numeroFicha; }

    public String getValorColumna(String columna) {
        switch (columna.toLowerCase()) {
            case "npn": return npn == null ? "" : npn;
            case "municipio": return municipio == null ? "" : municipio;
            case "direccion": return direccion == null ? "" : direccion;
            case "numeroficha": return numeroFicha == null ? "" : numeroFicha;
            default: return "";
        }
    }

    public String toString() {
        return npn + "," + municipio + "," + direccion + "," + numeroFicha;
    }
}
