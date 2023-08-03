import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FancyButton extends JButton {

    public FancyButton(){

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }
            @Override
            public void mouseExited(MouseEvent e){
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }



        });


    }





}