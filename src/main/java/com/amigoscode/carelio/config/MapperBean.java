package com.amigoscode.carelio.config;

import com.amigoscode.carelio.equipment.entity.Equipment;
import com.amigoscode.carelio.equipment.mapper.EquipmentMapper;
import com.amigoscode.carelio.room.mapper.RoomMapper;
import com.amigoscode.carelio.user.mapper.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperBean
{
    @Bean
    public RoomMapper roomMapper()
    {
        return Mappers.getMapper(RoomMapper.class);
    }

    @Bean
    public EquipmentMapper equipmentMapper()
    {
        return Mappers.getMapper(EquipmentMapper.class);
    }

    @Bean
    public UserMapper userMapper(){return Mappers.getMapper(UserMapper.class);}
}
