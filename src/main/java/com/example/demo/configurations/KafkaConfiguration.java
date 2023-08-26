package com.example.demo.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.micrometer.KafkaTemplateObservation.DefaultKafkaTemplateObservationConvention;

@Configuration(proxyBeanMethods = false)
public class KafkaConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public NewTopic topicWithCompressionExample() {
        return TopicBuilder.name("kafka-topic-with-compression")
            .partitions(6)
            .replicas(1)
            .build();
    }
    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory,
                                             ProducerListener<Object, Object> kafkaProducerListener,
                                             ObjectProvider<RecordMessageConverter> messageConverter) {
        var map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        var kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        kafkaTemplate.setObservationEnabled(true);
        kafkaTemplate.setObservationConvention(new DefaultKafkaTemplateObservationConvention());
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        map.from(kafkaProducerListener).to(kafkaTemplate::setProducerListener);
        map.from(kafkaProperties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
        map.from(kafkaProperties.getTemplate().getTransactionIdPrefix()).to(kafkaTemplate::setTransactionIdPrefix);
        return kafkaTemplate;
    }

    @Bean
    RecordMessageConverter recordMessageConverter() {
        return new StringJsonMessageConverter();
    }

}
