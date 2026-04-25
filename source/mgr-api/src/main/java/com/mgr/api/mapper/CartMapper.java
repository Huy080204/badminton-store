package com.mgr.api.mapper;

import com.mgr.api.dto.cart.CartDto;
import com.mgr.api.dto.cartItem.CartItemDto;
import com.mgr.api.model.Cart;
import com.mgr.api.model.CartItem;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ProductMapper.class})
public interface CartMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "quantity", target = "quantity")
    CartItemDto fromEntityToCartItemDto(CartItem item);

    @Mapping(source = "items", target = "items")
    @Mapping(target = "totalPrice", expression = "java(cart.getItems().stream().mapToDouble(i -> i.getQuantity() * i.getProduct().getPrice()).sum())")
    CartDto fromEntityToCartDto(Cart cart);
}