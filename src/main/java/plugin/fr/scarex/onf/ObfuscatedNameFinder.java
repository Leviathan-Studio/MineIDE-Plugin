package plugin.fr.scarex.onf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * @author SCAREX
 */
public class ObfuscatedNameFinder extends JFrame implements ActionListener
{
    private static final long serialVersionUID = -2491818253733758141L;
    public static final String VERSION = "=onf_version=";
    public ArrayList<File> forgeVersions = new ArrayList<File>();
    public JComboBox<File> versionList;
    public JTextField fieldName = new JTextField(16);
    public JCheckBox fieldObfuscatedType = new JCheckBox("Obfuscated");
    public JCheckBox fieldDeobfuscatedType = new JCheckBox("De-obfuscated");
    public JCheckBox fieldDocType = new JCheckBox("Documentation");
    public JComboBox<String> typeList = new JComboBox<String>(new String[] {"fields", "methods"});
    public ArrayList<String[]> csvData = new ArrayList<String[]>();
    public JTable table = new JTable(new CSVTableModel());
    
    public ObfuscatedNameFinder()
    {
        this.setTitle("Obfuscated Name Finder " + VERSION);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        this.findForgeVersions();
        this.init();
        
        this.setMinimumSize(new Dimension(600, 300));
        this.setLocationRelativeTo(null);
        this.pack();
    }
    
    private void findForgeVersions()
    {
        String gradleLocation = this.getGradleLocation();
        System.out.println(gradleLocation + " - " + System.getProperty("user.home"));
        File versionLocation = new File(gradleLocation, "caches/minecraft/net/minecraftforge/forge");
        for(File f : versionLocation.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return dir.isDirectory();
            }
        }))
        {
            this.forgeVersions.add(f);
        }
    }
    
    private String getGradleLocation()
    {
        return System.getenv("GRADLE_USER_HOME") != null ? System.getenv("GRADLE_USER_HOME") : System.getProperty("user.home") + "\\.gradle";
    }
    
    private void init()
    {
        this.setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout());
        top.setLayout(new FlowLayout());
        
        top.add(new JLabel("Forge version : "));
        
        top.add(this.versionList = new JComboBox<>(new FileComboBoxModel()));
        this.versionList.setRenderer(new FileComboBoxView());
        
        top.add(this.fieldName);
        top.add(this.fieldObfuscatedType);
        top.add(this.fieldDeobfuscatedType);
        top.add(this.fieldDocType);
        
        top.add(this.typeList);
        this.typeList.addActionListener(this);
        this.add(top, BorderLayout.NORTH);
        
        JPanel bot = new JPanel(new BorderLayout());
        bot.add(new JScrollPane(this.table), BorderLayout.CENTER);
        this.add(bot, BorderLayout.SOUTH);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == this.typeList)
        {
            try
            {
                ArrayList<String[]> data = new ArrayList<String[]>();
                if(this.forgeVersions.get(this.versionList.getSelectedIndex()).getName().startsWith("1.8"))
                    data = parseCSV(new File(this.getGradleLocation(), "caches/minecraft/de/oceanlabs/mcp/mcp_snapshot/" + (new File(this.forgeVersions.get(this.versionList.getSelectedIndex()), "snapshot")).list()[0] + "/" + ((String)this.typeList.getSelectedItem()) + ".csv"));
                else
                    data = parseCSV(new File(this.forgeVersions.get(this.versionList.getSelectedIndex()), "unpacked/conf/" + ((String)this.typeList.getSelectedItem()) + ".csv"));
                this.csvData.clear();
                for(String[] s : data)
                {
                    boolean flag = false;
                    if(this.fieldObfuscatedType.isSelected() && s[0].contains(this.fieldName.getText()))
                        flag = true;
                    if(this.fieldDeobfuscatedType.isSelected() && s[1].contains(this.fieldName.getText()))
                        flag = true;
                    if(this.fieldDocType.isSelected() && s[3].contains(this.fieldName.getText()))
                        flag = true;
                    if(flag)
                        this.csvData.add(new String[] {s[0], s[1], s[3]});
                }
                this.table.updateUI();
            }
            catch(IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }
    
    public static ArrayList<String[]> parseCSV(File f) throws IOException
    {
        ArrayList<String[]> data = new ArrayList<String[]>();
        BufferedReader r = new BufferedReader(new FileReader(f));
        r.readLine();
        String s;
        while((s = r.readLine()) != null)
        {
            data.add(charSplit(s, ','));
        }
        r.close();
        return data;
    }
    
    public static String[] charSplit(String s, char c)
    {
        ArrayList<String> as = new ArrayList<String>();
        int i = 0;
        while((i = s.indexOf(c)) > 0)
        {
            as.add(s.substring(0, i));
            s = s.substring(i + 1);
        }
        as.add(s.substring(i + 1));
        return as.toArray(new String[0]);
    }
    
    public class CSVTableModel extends DefaultTableModel
    {
        private static final long serialVersionUID = -7206142211367700637L;
        public final String[] headers = new String[] {"obfuscated name", "de-obfuscated name", "doc"};
        
        @Override
        public int getRowCount()
        {
            return csvData.size();
        }
        
        @Override
        public int getColumnCount()
        {
            return headers.length;
        }
        
        @Override
        public String getColumnName(int column)
        {
            return headers[column];
        }
        
        @Override
        public Object getValueAt(int row, int column)
        {
            return csvData.get(row)[column];
        }
    }
    
    public class FileComboBoxModel extends DefaultComboBoxModel<File>
    {
        private static final long serialVersionUID = 7076136856054542932L;
        
        public FileComboBoxModel()
        {
            super();
            if(forgeVersions.size() > 0)
                this.setSelectedItem(forgeVersions.get(0));
        }
        
        @Override
        public int getSize()
        {
            return forgeVersions.size();
        }
        
        @Override
        public File getElementAt(int index)
        {
            return forgeVersions.get(index);
        }
        
        @Override
        public int getIndexOf(Object anObject)
        {
            return forgeVersions.indexOf(anObject);
        }
        
        @Override
        public void addElement(File anObject)
        {
            forgeVersions.add(anObject);
        }
        
        @Override
        public void insertElementAt(File anObject, int index)
        {
            forgeVersions.add(index, anObject);;
        }
        
        @Override
        public void removeElementAt(int index)
        {
            forgeVersions.remove(index);
        }
        
        @Override
        public void removeElement(Object anObject)
        {
            forgeVersions.remove(anObject);
        }
        
        @Override
        public void removeAllElements()
        {
            forgeVersions.clear();
        }
    }
    
    public class FileComboBoxView extends DefaultListCellRenderer
    {
        private static final long serialVersionUID = -2813459178537604531L;
        
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setText(((File)value).getName());
            return c;
        }
    }
    
}
