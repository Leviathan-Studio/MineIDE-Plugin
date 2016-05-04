package plugin.fr.scarex.obfuscatednamefinder.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ObfuscatedNameFinderFrame extends Application
{
    CheckBox checkBoxObfuscated, checkBoxDeObfuscated;
    TextField searchTextField = new TextField();
    
    ComboBox<String> forgeVersionComboBox;
    public ArrayList<File> forgeVersions = new ArrayList<File>();
    ComboBox<String> typeList = new ComboBox<String>(FXCollections.observableArrayList("fields", "methods"));
    
    public ArrayList<String[]> csvData = new ArrayList<String[]>();
    
    @Override
    public void start(Stage stage) throws Exception
    {
        stage.setTitle("Obfuscated Name Finder");
        this.findForgeVersions();
        
        HBox box = new HBox();
        
        ObservableList<String> options = FXCollections.observableArrayList("Option 1", "Option 2", "Option 3");
        
        forgeVersionComboBox = new ComboBox<String>(options);
        
        checkBoxObfuscated = new CheckBox();
        checkBoxDeObfuscated = new CheckBox();
        checkBoxObfuscated.setText("Obfuscated");
        checkBoxDeObfuscated.setText("De-Obfuscated");
        
        searchTextField.setTooltip(new Tooltip("Name of field/method to find"));
        ArrayList<String[]> data = new ArrayList<String[]>();
        if(this.forgeVersions.get(this.forgeVersionComboBox.getItems().size()).getName().startsWith("1.8"))
            data = parseCSV(new File(this.getGradleLocation(), "caches/minecraft/de/oceanlabs/mcp/mcp_snapshot/" + (new File(this.forgeVersions.get(this.forgeVersionComboBox.getItems().size()), "snapshot")).list()[0] + "/" + ((String)this.typeList) + ".csv"));
        else
            data = parseCSV(new File(this.forgeVersions.get(this.forgeVersionComboBox.getItems().size()), "unpacked/conf/" + ((String)this.typeList.getPromptText()) + ".csv"));
        this.csvData.clear();
        
        box.getChildren().addAll(forgeVersionComboBox, searchTextField, checkBoxObfuscated, checkBoxDeObfuscated, typeList);
        
        StackPane root = new StackPane(box);
        root.getChildren().addAll();
        stage.setScene(new Scene(root, 854, 480));
        stage.show();
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
    
    public static void main(String[] args)
    {
        launch(args);
    }
}