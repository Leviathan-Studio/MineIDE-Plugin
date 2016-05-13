package com.leviathanstudio.plugin;

import java.util.Set;

import lombok.Builder;
import lombok.Singular;

@Builder
public class MIClass
{
    private String      name;
    private String      extend;
    @Singular("implement")
    private Set<String> implement;
}
