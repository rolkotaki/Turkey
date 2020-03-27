
package controller;

import model.ModelLogin;
import model.ModelSeason;
import view.ViewLogin;
import view.ViewMainScreen;

public class Controller {
    
    public static void main(String[] args) {
        ViewLogin vl = new ViewLogin();
        if (ModelLogin.isCorrectDatas()) {
            ModelSeason.isSeasonOn();
            ViewMainScreen vms = new ViewMainScreen();
        }
    }
    
}
