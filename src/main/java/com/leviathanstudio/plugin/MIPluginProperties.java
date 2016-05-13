package com.leviathanstudio.plugin;

import java.util.Set;

import lombok.Builder;
import lombok.Singular;

@Builder
public class MIPluginProperties
{
    private String      name;
    private String      version;
    private String      description;
    @Singular
    private Set<String> authors;
    /**
     * Requirements of your plugin, example :
     * .requirement("Before:BlockPlugin").requirement("After:ItemPlugin");
     */
    @Singular
    private Set<String> requirements;
}