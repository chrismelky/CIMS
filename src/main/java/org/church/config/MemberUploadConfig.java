package org.church.config;

import org.church.domain.Member;
import org.church.dto.MemberUploadDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class MemberUploadConfig {

    @Bean
    public Job memberUploadJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                ItemReader<MemberUploadDto> memberCsvFileReader,
                                ItemProcessor<MemberUploadDto, Member> memberUploadProcessor,
                                ItemWriter<Member> memberUploadWriter){

        Step step = stepBuilderFactory.get("ETL-file-load")
            .<MemberUploadDto, Member>chunk(100)
            .reader(memberCsvFileReader)
            .processor(memberUploadProcessor)
            .writer(memberUploadWriter)
            .build();
        return jobBuilderFactory.get("ETL-load")
            .incrementer(new RunIdIncrementer())
            .start(step)
            .build();

    }

    @Bean
    @StepScope
    public FlatFileItemReader<MemberUploadDto> memberCsvFileReader(@Value("#{jobParameters[filePath]}") String path){
        FlatFileItemReader<MemberUploadDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource(path));
        flatFileItemReader.setName("CSV-reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(memberCsvLineMapper());

        return  flatFileItemReader;
    }
    @Bean
    public LineMapper<MemberUploadDto> memberCsvLineMapper(){
        DefaultLineMapper<MemberUploadDto> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("id","jina", "jinsia", "namba_ya_ahadi", "tarehe_ya_kuzaliwa", "alipozaliwa","hali_ya_ndoa", "mwenza", "simu","simu2", "mzee_wa_kanisa", "namba_ya_mzee","shughuli", "ubatizo","kipaimara","ndoa", "jumuiya", "huduma", "ahadi", "jengo");

        BeanWrapperFieldSetMapper<MemberUploadDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(MemberUploadDto.class);
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }
}
