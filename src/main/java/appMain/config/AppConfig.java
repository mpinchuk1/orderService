package appMain.config;


import appMain.entities.Seller;
import appMain.repo.SellerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;


@Configuration
public class AppConfig {
    @Bean
    public CommandLineRunner demo(final SellerRepository sellerRepository) {
        return strings -> {
            Seller seller1 = new Seller(UUID.randomUUID(), "Kolya", "Frolov");
            sellerRepository.save(seller1);

        };
    }
}
