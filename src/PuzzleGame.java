import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PuzzleGame extends JFrame {
        JPanel panel;
        private final int WIDTH = 1000;
        private int HEIGHT = 1000;
        private final int NUMROWS = 4, NUMCOLS = 3;
        private List<FancyButton> buttons;
        private BufferedImage image; // adding the image
        private BufferedImage resizedImage; // resizing image
        private List<FancyButton> buttonsSolution;


        public PuzzleGame() {
            super("Puzzle Game!"); // set title

            panel = new JPanel();
            panel.setLayout(new GridLayout(NUMROWS, NUMCOLS)); // hard coded rows and columns to be able to resize easily
            add(panel);
            try {
                image = loadImage(); // loading the image into the program
                // resize the image to for our given width
                int sourceWidth = image.getWidth();
                int sourceHeight = image.getHeight();
                // resize the height so height/width = sourceHeight/sourceWidth
                HEIGHT = (int)((double)sourceHeight / sourceWidth * WIDTH);
                resizedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
                var graphics = resizedImage.createGraphics();
                graphics.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
                graphics.dispose();
            } catch(IOException e){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error loading this title", JOptionPane.ERROR_MESSAGE);

            }

            buttons = new ArrayList<FancyButton>();
            // Instantiate buttons and add them to the list
            for(int i = 0; i < NUMCOLS * NUMROWS; i++)
            {

                int row = i / NUMCOLS, col = i % NUMCOLS; // get row and col from i
                Image imageSlice = createImage(new FilteredImageSource(resizedImage.getSource(),
                        new CropImageFilter(col * WIDTH / NUMCOLS, row * HEIGHT/NUMROWS, WIDTH / NUMCOLS, HEIGHT / NUMROWS)));

                FancyButton btn = new FancyButton(); // instantiate buttons
                if(i == NUMCOLS * NUMROWS -1)
                {
                    btn.setBorderPainted(false);
                    btn.setContentAreaFilled(false);
                }
                else {
                    btn.setIcon(new ImageIcon(imageSlice));
                }

                buttons.add(btn); // add the buttons
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // makes the border of the buttons more defined
                btn.addActionListener(e -> MyClickEventHandler(e)); // adding the event lister for every click of a button
            }
            buttonsSolution = List.copyOf(buttons); // create the solution copy of all buttons
            Collections.shuffle(buttons); // shuffle the buttons

            for(var btn: buttons) {
                panel.add(btn); // add the buttons to the panel
            }




            setSize(WIDTH, HEIGHT); // hard coded width and height to change later easily
            setResizable(false);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }

        private void MyClickEventHandler(ActionEvent e)
        {
            FancyButton btnClicked = (FancyButton)e.getSource(); // this tells you which button was clicked
            int i = buttons.indexOf(btnClicked);

            int row = i / NUMCOLS;
            int col = i % NUMCOLS;

            int iempty = -1;

            for(int j = 0;j < buttons.size(); j++)
            {
                if(buttons.get(j).getIcon() == null) // this tells us if we found the empty button
                {
                    iempty = j;
                    break;
                }
            }
            // where is out empty button at in the grid? - which row and column
            int rowEmpty = iempty / NUMCOLS;
            int colEmpty = iempty % NUMCOLS;

            // check if the clicked button is adjacent (same row + adj column, or same colm and adjacent row

            if((row == rowEmpty && Math.abs(col - colEmpty) == 1) || (col == colEmpty && Math.abs(row - rowEmpty) == 1))
            {
                Collections.swap(buttons, i, iempty);
                updateButton();
            }

            if(buttonsSolution.equals(buttons))
            {
                JOptionPane.showMessageDialog(null, "Well done!");
            }
        }

        public void updateButton()
        {
            panel.removeAll();
            // read all buttons to the panel
            for(var btn: buttons)
            {
                panel.add(btn);

            }
            panel.validate();
        }

        private BufferedImage loadImage() throws IOException {
            return ImageIO.read(new File("src/Nature.jpeg"));
        }

    }