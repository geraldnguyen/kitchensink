package com.example.springweb.services;

import com.example.springweb.data.dto.FriendDTO;
import com.example.springweb.data.entity.FriendEntity;
import com.example.springweb.data.repository.FriendRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public List<FriendDTO> findAll() {
        return friendRepository.findAll().stream()
            .map(this::entityToDTO)
            .collect(Collectors.toList());
    }

    public FriendDTO add(FriendDTO dto) {
        var entity = friendRepository.save(dtoToEntity(dto));
        return entityToDTO(entity);
    }

    private FriendDTO entityToDTO(FriendEntity e) {
        return FriendDTO.builder().id(e.getId()).name(e.getName()).createDt(e.getCreateDt()).build();
    }

    private FriendEntity dtoToEntity(FriendDTO dto) {
        return FriendEntity.builder().id(dto.getId()).name(dto.getName()).createDt(dto.getCreateDt()).build();
    }
}
