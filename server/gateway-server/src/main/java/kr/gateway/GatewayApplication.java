package kr.gateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
    @Bean
    public CommandLineRunner testMongoConnection(ReactiveMongoTemplate mongoTemplate) {
        return args -> {
            // MongoDB에서 "testCollection"이라는 컬렉션이 있는지 확인
            Mono<Boolean> collectionExists = mongoTemplate.collectionExists("testCollection");

            // 결과를 출력
            collectionExists.subscribe(exists -> {
                if (exists) {
                    System.out.println("MongoDB에 'testCollection' 컬렉션이 존재합니다.");
                } else {
                    System.out.println("MongoDB에 'testCollection' 컬렉션이 없습니다.");
                }
            }, error -> {
                System.out.println("MongoDB 연결에 실패했습니다: " + error.getMessage());
            });
        };
    }
}
