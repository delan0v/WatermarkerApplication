package com.feldman.blazej.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:configuration/configuration.properties")
public class ApplicationConfiguration {

    @Getter
    @Value("${save.temporary.uploaded.files.filepath}")
    private String filepath;

    public String qrcFilePathShow =  "C:\\tmp\\qrcodesToRead\\";
    public String qrcFilePath = "C:\\tmp\\qrcodes\\";
    public String docFilePath = "C:\\tmp\\doc\\";
    public String logoFormatter = "C:\\tmp\\logoFormatter\\";
}
