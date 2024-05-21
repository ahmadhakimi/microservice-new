package com.microservice.inventoryservice;

import com.microservice.inventoryservice.entity.Inventory;
import com.microservice.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("rog_z_g14");
			inventory1.setQuantity(20);

			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("rz_b_14");
			inventory2.setQuantity(10);

			Inventory inventory3 = new Inventory();
			inventory3.setSkuCode("hp_o_t_14");
			inventory3.setQuantity(1);

			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);
			inventoryRepository.save(inventory3);
		};

	}

}
