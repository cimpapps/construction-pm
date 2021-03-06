package com.cimpapps.construction.pm.service.mapping;

import com.cimpapps.construction.pm.models.Drawing;
import com.cimpapps.construction.pm.models.Employee;
import com.cimpapps.construction.pm.models.EmployeePosition;
import com.cimpapps.construction.pm.models.Project;
import com.cimpapps.construction.pm.models.ProjectCategory;
import com.cimpapps.construction.pm.models.ProjectLayer;
import com.cimpapps.construction.pm.models.User;
import construction.pm.lib.dto.DrawingDTO;
import construction.pm.lib.dto.EmployeeDTO;
import construction.pm.lib.dto.EmployeePositionDTO;
import construction.pm.lib.dto.ProjectCategoryDTO;
import construction.pm.lib.dto.ProjectDTO;
import construction.pm.lib.dto.ProjectLayerDTO;
import construction.pm.lib.dto.UserDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DbEntitiesToDtoMapper {

    public static ProjectLayerDTO mapLayerToDto(ProjectLayer layer) {
        ProjectLayerDTO dto = new ProjectLayerDTO();
        
        if(layer == null)
            return null;

        dto.setId(layer.getId());
        dto.setName(layer.getName());
        dto.setProjectsId(mapProjectToDto(layer.getProjectsId()));
        dto.setDrawingCollection(mapDrawingCollectionToDtoCollection(layer.getDrawingCollection()));

        return dto;
    }

    public static List<ProjectLayerDTO> mapLayerCollectionToDtoCollection(Collection<ProjectLayer> layers) {
        List<ProjectLayerDTO> dto = new ArrayList<>();
        if(layers == null)
            return dto;
        
        dto = layers.stream()
                .map(l -> mapLayerToDto(l))
                .collect(Collectors.toList());

        return dto;
    }

    public static EmployeeDTO mapEmployeeToDto(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();

        if (employee == null)
            return null;
        
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setUsersId(mapUserToDto(employee.getUsersId()));
        dto.setProjectCollection(mapProjectCollecitonToDtoCollection(employee.getProjectCollection()));
        dto.setEmployeePositionsId(mapPositionToDto(employee.getEmployeePositionsId()));
        dto.setDrawingCollection(mapDrawingCollectionToDtoCollection(employee.getDrawingCollection()));

        return dto;
    }

    public static UserDTO mapUserToDto(User user) {
        UserDTO dto = new UserDTO();
        if(user == null)
            return null;

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEmployeeCollection(mapEmployeeCollectionToDtoCollection(user.getEmployeeCollection()));

        return dto;
    }

    public static List<EmployeeDTO> mapEmployeeCollectionToDtoCollection(Collection<Employee> employees) {
        List<EmployeeDTO> emplDtoes = new ArrayList<>();
        if(employees == null)
            return emplDtoes;
        
        emplDtoes = employees.stream()
                .map(emp -> mapEmployeeToDto(emp))
                .collect(Collectors.toList());

        return emplDtoes;
    }

    public static ProjectDTO mapProjectToDto(Project project) {
        ProjectDTO prDto = new ProjectDTO();

        if (project == null)
            return null;
        
        prDto.setId(project.getId());
        prDto.setName(project.getName());
        prDto.setEmployeeCollection(mapEmployeeCollectionToDtoCollection(project.getEmployeeCollection()));
        prDto.setProjectCategoriesId(mapCategoryToDto(project.getProjectCategoriesId()));
        prDto.setProjectLayerCollection(mapLayerCollectionToDtoCollection(project.getProjectLayerCollection()));

        return prDto;
    }

    public static List<ProjectDTO> mapProjectCollecitonToDtoCollection(Collection<Project> projects) {
        List<ProjectDTO> dto = new ArrayList<>();
        if (projects == null)
            return dto;
        dto = projects.stream()
                .map(p -> mapProjectToDto(p))
                .collect(Collectors.toList());

        return dto;
    }

    public static ProjectCategoryDTO mapCategoryToDto(ProjectCategory category) {
        ProjectCategoryDTO dto = new ProjectCategoryDTO();
        if (category == null)
            return null;
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setProjectCollection(mapProjectCollecitonToDtoCollection(category.getProjectCollection()));

        return dto;
    }

    public static List<ProjectCategoryDTO> mapCategoryCollectionToDtoCollection(Collection<ProjectCategory> categories) {
        List<ProjectCategoryDTO> dto = new ArrayList<>();
        if (categories == null)
            return dto;
        
        dto = categories.stream()
                .map(c -> mapCategoryToDto(c))
                .collect(Collectors.toList());

        return dto;
    }

    public static EmployeePositionDTO mapPositionToDto(EmployeePosition position) {
        EmployeePositionDTO dto = new EmployeePositionDTO();
        if (position == null)
            return null;
        dto.setId(position.getId());
        dto.setPosition(position.getPosition());
        dto.setEmployeeCollection(mapEmployeeCollectionToDtoCollection(position.getEmployeeCollection()));

        return dto;
    }

    public static DrawingDTO mapDrawingToDto(Drawing drawing) {
        DrawingDTO dto = new DrawingDTO();
        if(drawing == null)
            return null;
        dto.setId(drawing.getId());
        dto.setNumber(drawing.getNumber());
        dto.setDescription(drawing.getDescription());
        dto.setDateStarted(drawing.getDateStarted());
        dto.setDateFinished(drawing.getDateFinished());
        dto.setDateDue(drawing.getDateDue());
        dto.setEmployeesId(mapEmployeeToDto(drawing.getEmployeesId()));
        dto.setProjectLayersId(mapLayerToDto(drawing.getProjectLayersId()));

        return dto;
    }

    public static List<DrawingDTO> mapDrawingCollectionToDtoCollection(Collection<Drawing> drawings) {
        List<DrawingDTO> dto = new ArrayList<>();
        if(drawings == null)
            return dto;

        dto = drawings.stream()
                .map(d -> mapDrawingToDto(d))
                .collect(Collectors.toList());

        return dto;
    }
}

