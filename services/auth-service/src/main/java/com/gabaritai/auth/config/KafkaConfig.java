package com.gabaritai.auth.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Topico compartilhado de notificacoes assincronas (RNF-008). auth-service publica eventos
 * de e-mail (confirmacao de cadastro, redefinicao de senha); o consumo cabe ao notification-service
 * (RF36 / UC-056), ainda nao implementado — ver ADR-0002 e ADR-0003.
 */
@Configuration
public class KafkaConfig {

    public static final String NOTIFICATIONS_TOPIC = "gabaritai.notifications";

    @Bean
    public NewTopic notificationsTopic() {
        return TopicBuilder.name(NOTIFICATIONS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
