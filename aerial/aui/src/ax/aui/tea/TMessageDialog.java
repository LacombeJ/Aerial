package ax.aui.tea;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import ax.aui.MessageDialog;

public class TMessageDialog implements MessageDialog {
    
    private OptionPane optionPane;
    
    TMessageDialog() {
        
        optionPane = new OptionPane();
        
    }
    
    @Override
    public void setOption(Option option) {
        int ot = JOptionPane.DEFAULT_OPTION;
        if (option==Option.DEFAULT_OPTION)          ot = JOptionPane.DEFAULT_OPTION;
        if (option==Option.OK_CANCEL_OPTION)        ot = JOptionPane.OK_CANCEL_OPTION;
        if (option==Option.YES_NO_CANCEL_OPTION)    ot = JOptionPane.YES_NO_CANCEL_OPTION;
        if (option==Option.YES_NO_OPTION)           ot = JOptionPane.YES_NO_OPTION;
        optionPane.setOptionType(ot);
    }
    
    @Override
    public void setMessage(Message message) {
        int mt = JOptionPane.PLAIN_MESSAGE;
        if (message==Message.PLAIN)         mt = JOptionPane.PLAIN_MESSAGE;
        if (message==Message.INFORMATION)   mt = JOptionPane.INFORMATION_MESSAGE;
        if (message==Message.QUESTION)      mt = JOptionPane.QUESTION_MESSAGE;
        if (message==Message.WARNING)       mt = JOptionPane.WARNING_MESSAGE;
        if (message==Message.ERROR)         mt = JOptionPane.ERROR_MESSAGE;
        optionPane.setMessageType(mt);
    }
    
    @Override
    public State showDialog(Message message, String title, String text, Option option) {
        
        setMessage(message);
        setOption(option);
        optionPane.setMessage(text);
        JDialog dialog = optionPane.createDialog(title);
        dialog.setVisible(true);
        
        Object val = optionPane.getValue();
        
        // Not calling dispose might cause the jvm to continue running after the glfw window is closed
        dialog.dispose();
        
        if (val==null) {
            return State.NONE;
        }
        
        if (val instanceof Integer) {
            int ival = (int)val;
            if (ival == JOptionPane.YES_OPTION) return State.APPROVE;
            if (ival == JOptionPane.NO_OPTION) return State.REJECT;
            if (ival == JOptionPane.OK_OPTION) return State.APPROVE;
            if (ival == JOptionPane.CANCEL_OPTION) return State.CANCEL;
            if (ival == JOptionPane.CLOSED_OPTION) return State.NONE;
        }
        
        return State.NONE;
    }
    
    @Override
    public State plain(String title, String text) {
        return showDialog(Message.PLAIN, title, text, Option.DEFAULT_OPTION);
    }
    
    @Override
    public State information(String title, String text) {
        return showDialog(Message.INFORMATION, title, text, Option.DEFAULT_OPTION);
    }
    
    @Override
    public State question(String title, String text) {
        return showDialog(Message.QUESTION, title, text, Option.DEFAULT_OPTION);
    }
    
    @Override
    public State warning(String title, String text) {
        return showDialog(Message.WARNING, title, text, Option.DEFAULT_OPTION);
    }
    
    @Override
    public State error(String title, String text) {
        return showDialog(Message.ERROR, title, text, Option.DEFAULT_OPTION);
    }

    /**
     * Custom dialog in order to push the file chooser to front above glfw
     *
     */
    @SuppressWarnings("serial")
    static class OptionPane extends JOptionPane {
        
        @Override
        public JDialog createDialog(String title) {
            JDialog dialog = super.createDialog(null, title);
            
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            dialog.addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent e) {
                    dialog.toFront();
                    dialog.requestFocus();
                }
                public void windowClosing(WindowEvent e) {
                    
                }
            });
            
            dialog.setAlwaysOnTop(true);
            dialog.toFront();
            dialog.requestFocus();
            
            return dialog;
        }
        
    }

}
