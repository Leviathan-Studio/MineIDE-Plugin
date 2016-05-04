package com.leviathanstudio.plugin;

import plugin.fr.scarex.onf.ObfuscatedNameFinder;

public class RunPluginTest extends MineIDEPlugin
{
    
    @Override
    public void preInitPlugin()
    {
        this.setPluginName("Obfuscated Name Finder");
        this.setPluginAuthor("SCAREX");
        this.setPluginCredits("SCAREX");
        this.setPluginDescription("Simple plugin to find the properly name of an obfuscated field or method");
        System.out.println(this.getPluginAuthor() + System.lineSeparator() + this.getPluginName() + System.lineSeparator() + this.getPluginDescription() + System.lineSeparator() + this.getPluginCredits());
    }
    
    @Override
    public void initPlugin()
    {
        new ObfuscatedNameFinder();
    }
    
    // Debug
    public static void main(String[] args)
    {
        RunPluginTest test = new RunPluginTest();
        test.executePlugin();
    }
}
