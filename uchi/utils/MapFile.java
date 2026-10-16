package uchi.utils;

public class MapFile {
    private String nomFichier;
    private String extensionFichier;
    private byte[] contenuFichier;

    public MapFile(String nomFichier,
                   String extensionFichier,
                   byte[] contenuFichier) {
        this.nomFichier = nomFichier;
        this.extensionFichier = extensionFichier;
        this.contenuFichier = contenuFichier;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public String getExtensionFichier() {
        return extensionFichier;
    }

    public byte[] getContenuFichier() {
        return contenuFichier;
    }
}