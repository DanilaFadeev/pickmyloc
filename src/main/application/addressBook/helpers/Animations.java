package addressBook.helpers;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animations {
    public static void shakeAnimation(Node node) {
        TranslateTransition ft = new TranslateTransition(Duration.millis(70), node);
        ft.setFromX(0f);
        ft.setByX(10f);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.playFromStart();
    }
}
