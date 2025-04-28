/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.utils;

import java.awt.Desktop;
import java.net.URLEncoder;
import java.net.URI;

/**
 *
 * @author EMTI
 */
public class ErrorResLover {

    public static void howToFix(String er) {
        try {
            String query = "https://chatgpt.com/?q=" + URLEncoder.encode(er, "UTF-8");
            Desktop.getDesktop().browse(new URI(query));
        } catch (Exception e) {

        }
    }
}
