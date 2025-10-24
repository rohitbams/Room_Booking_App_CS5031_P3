package com.stacs.cs5031.p3.server.mapper;
import com.stacs.cs5031.p3.server.dto.OrganiserDto;
import com.stacs.cs5031.p3.server.model.Organiser;

public final class OrganiserDtoMapper {
    
    private OrganiserDtoMapper() {
        // Prevent instantiation of utility class
    }
    
    /**
     * Maps Organiser entity to OrganiserDTO.
     *
     * @param organiser The Organiser entity
     * @return The OrganiserDTO
     */
    public static OrganiserDto toOrganiserDTO(Organiser organiser) {
        if (organiser == null) {
            return null;
        }
        
        return new OrganiserDto(
            organiser.getId(),
            organiser.getName(),
            organiser.getUsername()
        );
    }
}
