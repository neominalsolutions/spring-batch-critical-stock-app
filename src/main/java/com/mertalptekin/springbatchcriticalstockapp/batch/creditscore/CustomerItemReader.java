package com.mertalptekin.springbatchcriticalstockapp.batch.creditscore;

import com.mertalptekin.springbatchcriticalstockapp.dto.CustomerDto;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

//@Component
public class CustomerItemReader extends FlatFileItemReader<CustomerDto> {
    @Override
    public CustomerDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        FlatFileItemReader<CustomerDto> reader = new FlatFileItemReader<>();

        reader.setResource(new ClassPathResource("customer.csv")); // Kaynak dosyayı ayarla (örneğin, bir dosya yolu)
        reader.setLinesToSkip(1); // Başlık satırını atla
        var lineMapper = new DefaultLineMapper<CustomerDto>();
        var delimiter = new DelimitedLineTokenizer();
        lineMapper.setLineTokenizer(delimiter);
        // csv customerDto nesnesine dönüştürme işlemi için FieldSetMapper kullanılır
        lineMapper.setFieldSetMapper(fieldSet -> {
            CustomerDto customer = new CustomerDto();
            customer.setName(fieldSet.readString("name"));
            customer.setAge(fieldSet.readInt("age"));
            customer.setCreditScore(fieldSet.readInt("creditScore"));
            return customer;
        });
        // Delimiter ayarları ve hangi alanların okunacağını belirtme
        delimiter.setNames(new String[] {  "name", "age", "creditScore" });
        delimiter.setDelimiter(",");
        reader.setLineMapper(lineMapper);
        reader.open(new ExecutionContext());

    return reader.read();

    }
}
