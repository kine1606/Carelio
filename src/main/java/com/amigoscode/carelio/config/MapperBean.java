package com.amigoscode.carelio.config;

import com.amigoscode.carelio.room.mapper.RoomMapper;
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
}
