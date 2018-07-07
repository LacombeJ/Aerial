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
    
    /**
     * Modified this class to match wildcard Pattern after seeing:
     * 
     * Exception in thread "Basic L&F File Loading Thread" Exception in thread "Basic L&F File Loading Thread" java.lang.NoClassDefFoundError: jonl/jutils/io/Pattern
     * 
     */
    static class ExtFilter extends FileFilter {

        String description;
        String[] patterns;
        
        //Used instead of creating a class because of error described above
        String[] values;
        boolean[] ignoreCases;
        
        public ExtFilter(String description, String[] patterns) {
            super();
            this.description = description;
            this.patterns = patterns;
            
            values = new String[patterns.length];
            ignoreCases = new boolean[patterns.length];
            
            for (int i=0; i<patterns.length; i++) {
                String pattern = patterns[i];
                boolean ignoreCase = true;
                
                pattern = pattern.replace('\\', '/');
                pattern = pattern.replaceAll("\\*\\*[^/]", "**/*");
                pattern = pattern.replaceAll("[^/]\\*\\*", "*/**");
                if (ignoreCase) pattern = pattern.toLowerCase();
                
                values[i] = pattern.split("/")[0];
                ignoreCases[i] = ignoreCase;
            }
        }
        
        @Override
        public boolean accept(File f) {
            for (int i=0; i<patterns.length; i++) {
                if (f.isDirectory()) {
                    return true;
                }
                String pattern = patterns[i];
                String value = values[i];
                boolean ignoreCase = ignoreCases[i];
                if (matches(f.getName(),pattern,value,ignoreCase)) {
                    return true;
                }
            }
            return false;
        }
        
        private boolean matches(String fileName, String pattern, String value, boolean ignoreCase) {
            if (value.equals("**")) return true;

            if (ignoreCase) fileName = fileName.toLowerCase();

            // Shortcut if no wildcards.
            if (value.indexOf('*') == -1 && value.indexOf('?') == -1) return fileName.equals(value);

            int i = 0, j = 0;
            while (i < fileName.length() && j < value.length() && value.charAt(j) != '*') {
                if (value.charAt(j) != fileName.charAt(i) && value.charAt(j) != '?') return false;
                i++;
                j++;
            }

            // If reached end of pattern without finding a * wildcard, the match has to fail if not same length.
            if (j == value.length()) return fileName.length() == value.length();

            int cp = 0;
            int mp = 0;
            while (i < fileName.length()) {
                if (j < value.length() && value.charAt(j) == '*') {
                    if (j++ >= value.length()) return true;
                    mp = j;
                    cp = i + 1;
                } else if (j < value.length() && (value.charAt(j) == fileName.charAt(i) || value.charAt(j) == '?')) {
                    j++;
                    i++;
                } else {
                    j = mp;
                    i = cp++;
                }
            }

            // Handle trailing asterisks.
            while (j < value.length() && value.charAt(j) == '*')
                j++;

            return j >= value.length();
        }

        @Override
        public String getDescription() {
            return description;
        }
        
    }

}
