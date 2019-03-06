package org.cat73.pager.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties(PagerConfigure.class)
@ComponentScan("org.cat73.pager")
public class PagerAutoConfigure {}
