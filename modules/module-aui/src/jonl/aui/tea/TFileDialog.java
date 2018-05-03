package jonl.aui.tea;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import jonl.aui.FileDialog;
import jonl.jutils.io.Pattern;

public class TFileDialog implements FileDialog {

    private FileChooser fileChooser;
    
    TFileDialog() {
        
        fileChooser = new FileChooser();
        
    }
    
    @Override
    public void setMode(Mode mode) {
        int jmode = JFileChooser.FILES_ONLY;
        if (mode==Mode.FILES_ONLY)              jmode = JFileChooser.FILES_ONLY;
        if (mode==Mode.DIRECTORIES_ONLY)        jmode = JFileChooser.DIRECTORIES_ONLY;
        if (mode==Mode.FILES_AND_DIRECTORIES)   jmode = JFileChooser.FILES_AND_DIRECTORIES;
        fileChooser.setFileSelectionMode(jmode);
    }
    
    @Override
    public void setDirectory(String dir) {
        File file = new File(dir);
        fileChooser.setCurrentDirectory(file);
    }
    
    @Override
    public void setFilter(String description, String... pattern) {
        ExtFilter filter = new ExtFilter(description, pattern);
        fileChooser.setFileFilter(filter);
    }
    
    @Override
    public void addFilter(String description, String... pattern) {
        ExtFilter filter = new ExtFilter(description, pattern);
        fileChooser.addChoosableFileFilter(filter);
    }

    @Override
    public State showOpenDialog() {
        int state = fileChooser.showOpenDialog(null);
        if (state==JFileChooser.APPROVE_OPTION) return State.APPROVE;
        if (state==JFileChooser.CANCEL_OPTION)  return State.CANCEL;
        if (state==JFileChooser.ERROR_OPTION)   return State.ERROR;
        return State.ERROR;
    }

    @Override
    public State showSaveDialog() {
        int state = fileChooser.showSaveDialog(null);
        if (state==JFileChooser.APPROVE_OPTION) return State.APPROVE;
        if (state==JFileChooser.CANCEL_OPTION)  return State.CANCEL;
        if (state==JFileChooser.ERROR_OPTION)   return State.ERROR;
        return State.ERROR;
    }
    
    @Override
    public String selected() {
        return fileChooser.getSelectedFile().getPath();
    }
    
    
    

    /**
     * Custom dialog in order to push the file chooser to front above glfw
     *
     */
    @SuppressWarnings("serial")
    static class FileChooser extends JFileChooser {
        
        @Override
        protected JDialog createDialog(Component parent) throws HeadlessException {
            JDialog dialog = super.createDialog(parent);
            
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
        
        @Override
        public void show() {
            
        }
        
    }
    
    static class ExtFilter extends FileFilter {

        String description;
        String[] patterns;
        
        public ExtFilter(String description, String[] patterns) {
            this.description = description;
            this.patterns = patterns;
        }
        
        @Override
        public boolean accept(File f) {
            for (String pattern : patterns) {
                if (f.isDirectory()) {
                    return true;
                }
                Pattern p = new Pattern(pattern, true);
                if (p.matches(f.getName())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return description;
        }
        
        
        
    }

}
