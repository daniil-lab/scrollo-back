package com.inst.base;

import com.inst.base.entity.user.User;
import com.inst.base.repository.user.UserRepository;
import com.inst.base.util.FileStorageProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {
		BaseApplication.class,
		Jsr310JpaConverters.class
})
@EnableJpaAuditing
@SecurityScheme(
		name = "Bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
@OpenAPIDefinition(
		info = @Info(
				title = "Inst API",
				version = "1.0",
				description = "Inst system")
)
public class BaseApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
