package com.kdt_y_be_toy_project2.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
class OpenApi3Config {

	@Bean
	OpenAPI springShopOpenAPI() {
		return new OpenAPI()
			.info(new Info().title("야놀자 토이프로젝트 4조 여행 API 문서")
				.description("YBE Toy Team4 trip api")
				.version("v1"))
			.externalDocs(new ExternalDocumentation()
				.description("프로젝트 readme")
				.url("https://github.com/FastCampusKDTBackend-Toy4/KDT_Y_BE_Toy_Project2"));
	}

	@Bean
	GroupedOpenApi tripsApi() {
		return GroupedOpenApi.builder()
			.group("여행 API")
			.pathsToMatch("/v1/trips/**")
			.build();
	}
}
