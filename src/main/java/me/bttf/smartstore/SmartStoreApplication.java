package me.bttf.smartstore;

import me.bttf.smartstore.domain.category.Category;
import me.bttf.smartstore.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmartStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartStoreApplication.class, args);
    }

    @Bean
    CommandLineRunner seedCategories(CategoryRepository repo) {
        return args -> {
            if (repo.count() > 0) return;

            repo.save(new Category("패션·의류", 1, null));
            repo.save(new Category("뷰티·미용", 1, null));
            repo.save(new Category("식품·건강", 1, null));
            repo.save(new Category("디지털·가전", 1, null));
            repo.save(new Category("스포츠·레저", 1, null));
            repo.save(new Category("홈·리빙", 1, null));
        };
    }
}
