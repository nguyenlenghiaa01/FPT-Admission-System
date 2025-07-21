package com.example.aiservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.dsl.Files;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
@EnableIntegration
@Slf4j
public class EtlConfig {
    @Value("${etl.file.input-directory}")
    private String inputDirectory;

    @Value("${etl.file.polling-rate:5000}")
    private long pollingRate;

    @Bean
    public IntegrationFlow fileReadingFlow() {
        return IntegrationFlow
                .from(Files.inboundAdapter(new File(inputDirectory))
                                .regexFilter(".*\\.(txt|pdf|docx)")
                                .useWatchService(true)
                                .watchEvents(FileReadingMessageSource.WatchEventType.CREATE)
                        , e -> e.poller(Pollers.fixedDelay(pollingRate)))
                .channel(processedFileChannel())
                .get();
    }


    @Bean
    public MessageChannel processedFileChannel() {
        return new DirectChannel();
    }


    @Bean
    public IntegrationFlow processFileFlow(ChromaVectorStore chromaVectorStore) {
        return IntegrationFlow.from(processedFileChannel())
                .handle(message -> {
                    log.info("Processing file: {}", message.getPayload());
                    var tikaDocumentReader = new TikaDocumentReader(new FileSystemResource(message.getPayload().toString()));
                    var documents = tikaDocumentReader.read();
                    var splitter = new TokenTextSplitter(300, 50, 20, 100, true);
                    documents = splitter.apply(documents);
                    chromaVectorStore.accept(documents);
                    log.info("Processed file: {}", message.getPayload());
                })
                .get();
    }
}
