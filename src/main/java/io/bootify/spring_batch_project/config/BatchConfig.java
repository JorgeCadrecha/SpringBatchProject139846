package io.bootify.spring_batch_project.config;

import io.bootify.spring_batch_project.transaccion.domain.Transaccion;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private static final String[] Nombres_Variables_Transaccion = {"TransactionID", "CustomerID", "CustomerDOB", "CustGender", "CustLocation", "CustAccountBalance", "TransactionDate", "TransactionTime", "TransactionAmount"};

    @Bean
    public FlatFileItemReader<Transaccion> reader() {
        return new FlatFileItemReaderBuilder<Transaccion>()
                .name("transaccionReader")
                .resource(new FileSystemResource("src/main/resources/bank_transactions.csv"))
                .delimited()
                .names(Nombres_Variables_Transaccion)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Transaccion>() {{
                    setTargetType(Transaccion.class);
                }})
                .build();
    }
}
