package Social_Talk.Friends_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FriendsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendsServiceApplication.class, args);
	}

}
