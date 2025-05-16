package ru.example.catalogservice.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.example.catalogservice.model.entity.Product;
import ru.example.catalogservice.model.payload.product.ProductPayload;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(target = "images", source = "imagesUrls")
    ProductPayload mapToProductPayload(Product product, List<String> imagesUrls);
}
