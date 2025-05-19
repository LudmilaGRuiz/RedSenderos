package utilidades;

import model.Grafo;

import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void guardarGrafo(Grafo grafo, String archivo) throws Exception {
        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(grafo, writer);
        }
    }

    public static Grafo cargarGrafo(String archivo) throws Exception {
        try (FileReader reader = new FileReader(archivo)) {
            return gson.fromJson(reader, Grafo.class);
        }
    }
}
