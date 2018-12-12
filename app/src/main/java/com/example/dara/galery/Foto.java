package com.example.dara.galery;

public class Foto {

    int id;
    String nama;
    String path_foto;
    String deskripsi;
    Double lat;
    Double lng;

    public Foto(int i, String string, String cursorString, String s, double aDouble) {
    }

    public Foto(int id, String nama, String path_foto, String deskripsi, Double lat, Double lng) {
        this.id = id;
        this.nama = nama;
        this.path_foto = path_foto;
        this.deskripsi = deskripsi;
        this.lat = lat;
        this.lng = lng;
    }

    public Foto() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPath_foto() {
        return path_foto;
    }

    public void setPath_foto(String path_foto) {
        this.path_foto = path_foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Foto{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", path_foto='" + path_foto + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }
}
