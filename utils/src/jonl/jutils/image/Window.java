package jonl.jutils.image;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Window {

    private String name;
    private int width;
    private int height;
    
    private JFrame frame;
    private JLabel label;
    
    
    Window(String name, int width, int height) {
        
        this.name = name;
        this.width = width;
        this.height = height;
        
        frame = new JFrame();
        frame.setName(this.name);
        frame.setSize(this.width,this.height);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        label = new JLabel();
        frame.add(label);
        
        frame.setVisible(true);
        
    }
    
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
    
    public void setImage(Image image) {
        ImageIcon icon = new ImageIcon(image.getBufferedImage());
        label.setIcon(icon);
    }
    
    
}
