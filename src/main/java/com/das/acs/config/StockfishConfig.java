package com.das.acs.config;

import com.das.acs.util.StockfishUCIAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class StockfishConfig {

    @Value("${stockfish.engine.path}")
    private String stockfishPath;

    @Bean
    public String stockfishEnginePath() throws IOException {
        Resource resource = new FileSystemResource(stockfishPath);

        if (!resource.exists()) {
            throw new FileNotFoundException(
                    "Stockfish not found at: " + stockfishPath +
                    ". Download it from https://stockfishchess.org/download/ and update the path."
            );
        }

        File engineFile = resource.getFile();

        if (!engineFile.canExecute()) {
            throw new IOException("Stockfish is not executable: " + engineFile.getAbsolutePath());
        }

        return engineFile.getAbsolutePath();
    }

    @Bean
    public StockfishUCIAdapter stockfishUCIAdapter() throws IOException {
        StockfishUCIAdapter adapter = new StockfishUCIAdapter();
        String stockfishEnginePath = stockfishEnginePath();
        adapter.startEngine(stockfishEnginePath);
        return adapter;
    }
}
