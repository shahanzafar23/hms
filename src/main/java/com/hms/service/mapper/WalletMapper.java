package com.hms.service.mapper;


import com.hms.domain.Wallet;
import com.hms.service.dto.WalletDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { })
public interface WalletMapper extends EntityMapper<WalletDTO, Wallet> {
}
