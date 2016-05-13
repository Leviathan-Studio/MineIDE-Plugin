package com.leviathanstudio.plugin;

public abstract class MineIDEPlugin
{
    public abstract MIPluginProperties getProperties();

    public abstract void init();

    public abstract MineIDEBean getData();
}