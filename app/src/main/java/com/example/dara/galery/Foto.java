package com.example.dara.galery;

public class Foto {

    int id;
    String nama;
    String path_foto;
    String deskripsi;
    String lokasi;



    public Foto(int id, String nama, String path_foto, String deskripsi, String lokasi) {
        this.id = id;
        this.nama = nama;
        this.path_foto = path_foto;
        this.deskripsi = deskripsi;
        this.lokasi = lokasi;
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

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    @Override
    public String toString() {
        return "Foto{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", path_foto='" + path_foto + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", lokasi='" + lokasi + '\'' +
                '}';
    }
}
