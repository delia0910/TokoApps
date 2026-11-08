package kelas.controller;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import kelas.Navigator;
import kelas.data.DataProduk;

public class FormController {

    @FXML
    private TextField namaField, hargaField;
    @FXML
    private ComboBox<String> kategoriBox;
    @FXML
    private Label pesanLabel;
    @FXML
    private ImageView gambarView;
    private Navigator navigator;
    private DataProduk data;
    private String gambarUri;

    @FXML
    private void initialize() {
        kategoriBox.getItems().addAll("Makanan", "Minuman");
        kategoriBox.setValue("Makanan");
    }

    public void setKonteks(Navigator navigator, DataProduk data) {
        this.navigator = navigator;
        this.data = data;
    }

    @FXML
    private void pilihGambar() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih foto produk");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gambar PNG/JPEG", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(namaField.getScene().getWindow());
        if (file == null) {
            return; // Batal: pertahankan pilihan sebelumnya.
        }
        if (file.length() > 5L * 1024 * 1024) {
            pesanLabel.setText("Maksimum foto 5 MB.");
            return;
        }
        Image image = new Image(file.toURI().toString(), 640, 480, true, true);
        if (image.isError()) {
            pesanLabel.setText("Foto tidak dapat dibaca.");
            return;
        }
        gambarUri = file.toURI().toString();
        gambarView.setImage(image);
        pesanLabel.setText("");
    }

    @FXML
    private void hapusGambar() {
        gambarUri = null;
        gambarView.setImage(null);
        pesanLabel.setText("");
    }

    @FXML
    private void simpan() {
        try {
            long harga = Long.parseLong(hargaField.getText().trim());
            data.tambah(namaField.getText(), kategoriBox.getValue(), harga, gambarUri);
        } catch (NumberFormatException e) {
            pesanLabel.setText("Harga harus berupa bilangan bulat.");
            return;
        } catch (IllegalArgumentException e) {
            pesanLabel.setText(e.getMessage());
            return;
        }
        try {
            navigator.bukaDaftar();
        } catch (IOException e) {
            pesanLabel.setText("Daftar gagal dibuka. Periksa FXML.");
            e.printStackTrace();
        }
    }

    @FXML
    private void bukaDaftar() {
        try {
            navigator.bukaDaftar();
        } catch (IOException e) {
            pesanLabel.setText("Gagal membuka daftar.");
            e.printStackTrace();
        }
    }
}
