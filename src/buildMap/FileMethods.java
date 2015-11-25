/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buildMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author jcarlos2289
 */
public class FileMethods {

    public static List<String> processFile(String file) {
        //List<String> lista = new ArrayList<>();
        String filepath = file;

        File archivo0 = null;
        FileReader fr = null;
        BufferedReader br = null;

        List<String> paths = new ArrayList<>();
        String path = filepath;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo0 = new File(path);
            fr = new FileReader(archivo0);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;

            while ((linea = br.readLine()) != null) {
                // System.out.println(linea);
                paths.add(linea.trim()); // path de cada archivo de texto
            }
            //System.out.println(String.valueOf("Cantidad de Lineas: " + paths.size()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }

        return paths;

    }

    public static void saveFile(String cadTxt, String txtname, boolean append) {
        //This method save a file with the results of the processes of the file with tags

        //obtener la ruta de donde esta ubicado el proyecto
        // String txtname = "tagList";
        File miDir = new File(".");
        String c = miDir.getAbsolutePath();

        //elimino el punto (.) nombre del archivo(virtual) que cree para obtener la ruta de la carpeta del proyecto
        String ruta = c.substring(0, c.length() - 1);

        ruta += "resultados/" + txtname.trim() + ".txt";

        //JOptionPane.showMessageDialog(rootPane, ruta);
        File archivo = new File(ruta);

        BufferedWriter bw = null;

        if (archivo.exists()) {
            try {
                bw = new BufferedWriter(new FileWriter(archivo, append)); // con true añade al final del archivo
                try {
                    //bw.write(cadTxt);  //archivo existe se escribe en él
                    bw.append(cadTxt);
                   // System.out.println("File Saved.");
                } catch (IOException ex) {
                   // System.out.println("File Not Saved.");
                    System.err.println(ex.toString());
                }
            } catch (IOException ex) {
                System.err.println(ex.toString());
            }
        } else {
            try {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(cadTxt); //archivo no existe se crea y se escribe en él
                System.out.println("File: "+txtname +" Saved.");
            } catch (IOException ex) {
                System.out.println("File Not Saved.");
                System.err.println(ex.toString());
            }
        }
        try {
            bw.close(); // se cierra el archivo
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }

    }

}
