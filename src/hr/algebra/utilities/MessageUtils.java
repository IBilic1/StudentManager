/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utilities;

import javafx.scene.control.Alert;

/**
 *
 * @author HT-ICT
 */
public class MessageUtils {

    private MessageUtils() {
    }
    
    public static void showMessage(Alert.AlertType type,String message){
        new Alert(type, message).show();
    }
    
}
