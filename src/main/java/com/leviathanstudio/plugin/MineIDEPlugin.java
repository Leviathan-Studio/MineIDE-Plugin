package com.leviathanstudio.plugin;

import java.util.ArrayList;
import java.util.List;

public abstract class MineIDEPlugin
{
    private String pluginName, pluginDescription, pluginAuthor, pluginCredits;
    private List<String> nullPluginList = new ArrayList<String>();
    private boolean informationIncomplete;
    
    public String getPluginName()
    {
        return pluginName;
    }
    
    public void setPluginName(String pluginName)
    {
        this.pluginName = pluginName;
    }
    
    public String getPluginDescription()
    {
        return pluginDescription;
    }
    
    public void setPluginDescription(String pluginDescription)
    {
        this.pluginDescription = pluginDescription;
    }
    
    public String getPluginAuthor()
    {
        return pluginAuthor;
    }
    
    public void setPluginAuthor(String pluginAuthor)
    {
        this.pluginAuthor = pluginAuthor;
    }
    
    public String getPluginCredits()
    {
        return pluginCredits;
    }
    
    public void setPluginCredits(String pluginCredits)
    {
        this.pluginCredits = pluginCredits;
    }
    
    public boolean isInformationIncomplete()
    {
        return informationIncomplete;
    }
    
    public void setInformationIncomplete(boolean informationIncomplete)
    {
        this.informationIncomplete = informationIncomplete;
    }
    
    public abstract void preInitPlugin();
    
    public abstract void initPlugin();
    
    public void executePlugin()
    {
        this.preInitPlugin();
        this.checkPluginInformation();
        
        if(!this.isInformationIncomplete())
            this.initPlugin();
        else
        {
            System.err.println("[MineIDE-Plugin] Incomplete Plugin Information {");
            for(int i = 0; i < nullPluginList.size(); i++)
                System.err.println(nullPluginList.get(i));
                
            System.err.println("}");
        }
    }
    
    private void checkPluginInformation()
    {
        if(this.getPluginName() == null || this.getPluginName().isEmpty())
        {
            nullPluginList.add("Plugin Name is null or empty");
            this.setInformationIncomplete(true);
        }
        else if(this.getPluginAuthor() == null || this.getPluginAuthor().isEmpty())
        {
            nullPluginList.add("Plugin Author is null or empty");
            this.setInformationIncomplete(true);
        }
        else if(this.getPluginDescription() == null || this.getPluginDescription().isEmpty())
        {
            nullPluginList.add("Plugin Description is null or empty");
            this.setInformationIncomplete(true);
        }
        else if(this.getPluginCredits() == null || this.getPluginCredits().isEmpty())
        {
            nullPluginList.add("Plugin Credits is null or empty");
            this.setInformationIncomplete(true);
        }
    }
}