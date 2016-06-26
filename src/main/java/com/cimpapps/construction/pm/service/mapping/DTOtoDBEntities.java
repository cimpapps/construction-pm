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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DTOtoDBEntities {

    public static ProjectLayer mapLayerToDto(ProjectLayerDTO layer) {
        ProjectLayer dbBean = new ProjectLayer();

        dbBean.setId(layer.getId());
        dbBean.setName(layer.getName());
        dbBean.setProjectsId(mapDtoToProject(layer.getProjectsId()));
        dbBean.setDrawingCollection(mapDrawingCollectionToDtoCollection(layer.getDrawingCollection()));

        return dbBean;
    }

    public static List<ProjectLayer> mapLayerCollectionToDtoCollection(Collection<ProjectLayerDTO> layers) {
        List<ProjectLayer> dbBean = null;
        dbBean = layers.stream()
                .map(l -> mapLayerToDto(l))
                .collect(Collectors.toList());

        return dbBean;
    }

    public static Employee mapEmployeeToDto(EmployeeDTO employee) {
        Employee dbBean = new Employee();

        dbBean.setId(employee.getId());
        dbBean.setFirstName(employee.getFirstName());
        dbBean.setLastName(employee.getLastName());
        dbBean.setEmail(employee.getEmail());
        dbBean.setUsersId(mapUserToDto(employee.getUsersId()));
        dbBean.setProjectCollection(mapProjectCollecitonToDtoCollection(employee.getProjectCollection()));
        dbBean.setEmployeePositionsId(mapPositionToDto(employee.getEmployeePositionsId()));
        dbBean.setDrawingCollection(mapDrawingCollectionToDtoCollection(employee.getDrawingCollection()));

        return dbBean;
    }

    public static User mapUserToDto(UserDTO user) {
        User dbBean = new User();

        dbBean.setId(user.getId());
        dbBean.setUsername(user.getUsername());
        dbBean.setPassword(user.getPassword());
        dbBean.setEmployeeCollection(mapEmployeeCollectionToDtoCollection(user.getEmployeeCollection()));

        return dbBean;
    }

    public static List<Employee> mapEmployeeCollectionToDtoCollection(Collection<EmployeeDTO> employees) {
        List<Employee> dbBean = null;
        dbBean = employees.stream()
                .map(emp -> mapEmployeeToDto(emp))
                .collect(Collectors.toList());

        return dbBean;
    }

    public static Project mapDtoToProject(ProjectDTO project) {
        Project dbBean = new Project();

        dbBean.setId(project.getId());
        dbBean.setName(project.getName());
        dbBean.setEmployeeCollection(mapEmployeeCollectionToDtoCollection(project.getEmployeeCollection()));
        dbBean.setProjectCategoriesId(mapCategoryToDto(project.getProjectCategoriesId()));
        dbBean.setProjectLayerCollection(mapLayerCollectionToDtoCollection(project.getProjectLayerCollection()));

        return dbBean;
    }

    public static List<Project> mapProjectCollecitonToDtoCollection(Collection<ProjectDTO> projects) {
        List<Project> dbBean = null;
        dbBean = projects.stream()
                .map(p -> mapDtoToProject(p))
                .collect(Collectors.toList());

        return dbBean;
    }

    public static ProjectCategory mapCategoryToDto(ProjectCategoryDTO category) {
        ProjectCategory dbBean = new ProjectCategory();

        dbBean.setId(category.getId());
        dbBean.setName(category.getName());
        dbBean.setProjectCollection(mapProjectCollecitonToDtoCollection(category.getProjectCollection()));

        return dbBean;
    }

    public static List<ProjectCategory> mapCategoryCollectionToDtoCollection(Collection<ProjectCategoryDTO> categories) {
        List<ProjectCategory> dbBean = null;
        dbBean = categories.stream()
                .map(c -> mapCategoryToDto(c))
                .collect(Collectors.toList());

        return dbBean;
    }

    public static EmployeePosition mapPositionToDto(EmployeePositionDTO position) {
        EmployeePosition dbBean = new EmployeePosition();

        dbBean.setId(position.getId());
        dbBean.setPosition(position.getPosition());
        dbBean.setEmployeeCollection(mapEmployeeCollectionToDtoCollection(position.getEmployeeCollection()));

        return dbBean;
    }

    public static Drawing mapDrawingToDto(DrawingDTO drawing) {
        Drawing dbBean = new Drawing();

        dbBean.setId(drawing.getId());
        dbBean.setNumber(drawing.getNumber());
        dbBean.setDescription(drawing.getDescription());
        dbBean.setDateStarted(drawing.getDateStarted());
        dbBean.setDateFinished(drawing.getDateFinished());
        dbBean.setDateDue(drawing.getDateDue());
        dbBean.setEmployeesId(mapEmployeeToDto(drawing.getEmployeesId()));
        dbBean.setProjectLayersId(mapLayerToDto(drawing.getProjectLayersId()));

        return dbBean;
    }

    public static List<Drawing> mapDrawingCollectionToDtoCollection(Collection<DrawingDTO> drawings) {
        List<Drawing> dto = null;

        dto = drawings.stream()
                .map(d -> mapDrawingToDto(d))
                .collect(Collectors.toList());

        return dto;
    }
}

