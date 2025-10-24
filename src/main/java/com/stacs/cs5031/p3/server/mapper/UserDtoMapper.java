package com.stacs.cs5031.p3.server.mapper;

import com.stacs.cs5031.p3.server.dto.UserDto;
import com.stacs.cs5031.p3.server.model.Admin;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.model.User;

/**
 * UserDtoMapper utility class.
 *
 */
public final class UserDtoMapper {
    private UserDtoMapper() {
    }

    public static UserDto mapToDTO(User user) {
            String role = "USER";
            if (user instanceof Admin) {
                role = "ADMIN";
            } else if (user instanceof Organiser) {
                role = "ORGANISER";
            } else if (user instanceof Attendee) {
                role = "ATTENDEE";
            }

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                role
        );
    }
}