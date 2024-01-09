package mingu.spring.paseto.mapper;

import lombok.experimental.UtilityClass;
import mingu.spring.paseto.dto.GroupRoleDTO;
import mingu.spring.paseto.dto.UserDTO;
import mingu.spring.paseto.entity.GroupRole;
import mingu.spring.paseto.entity.User;
import org.springframework.beans.BeanUtils;

@UtilityClass
public class AppMapper {
    public UserDTO copyUserEntityToDto(User userEntity) {
        var userDTO = new UserDTO();
        BeanUtils.copyProperties(userEntity, userDTO);
        userDTO.setRoles(userEntity.getRoles().stream().map(AppMapper::copyGroupRoleToGroupRoleDTO).toList());
        return userDTO;
    }

    private GroupRoleDTO copyGroupRoleToGroupRoleDTO(GroupRole entity) {
        var groupRoleDTO = new GroupRoleDTO();
        BeanUtils.copyProperties(entity, groupRoleDTO);
        return groupRoleDTO;
    }

    public User copyUserDtoToEntity(UserDTO dto) {
        var user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setRoles(dto.getRoles().stream().map(AppMapper::copyGroupRoleDTOToGroupRole).toList());
        BeanUtils.copyProperties(dto.getRoles(), user.getRoles());
        return user;
    }

    private GroupRole copyGroupRoleDTOToGroupRole(GroupRoleDTO dto) {
        var groupRole = new GroupRole();
        BeanUtils.copyProperties(dto, groupRole);
        return groupRole;
    }
}
