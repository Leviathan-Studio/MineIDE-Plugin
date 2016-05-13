package com.leviathanstudio.plugin.block;

import com.leviathanstudio.plugin.MIPluginProperties;
import com.leviathanstudio.plugin.MineIDEBean;
import com.leviathanstudio.plugin.MineIDEPlugin;

public class BlockPlugin extends MineIDEPlugin
{
    @Override
    public void init()
    {

    }

    @Override
    public MineIDEBean getData()
    {
        return null;
    }

    @Override
    public MIPluginProperties getProperties()
    {
        return MIPluginProperties.builder().name("BlockPlugin").version("0.0.1")
                .description("Test MineIDE plugin to add basic blocks").author("Ourten").build();
    }
}