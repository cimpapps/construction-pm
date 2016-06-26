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

public class DTOtoDBEntities {

    public static ProjectLayer mapLayerToDto(ProjectLayerDTO layer) {
        
        ProjectLayer dbBean = new ProjectLayer();
        if (layer == null)
            return null;

        dbBean.setId(layer.getId());
        dbBean.setName(layer.getName());
        dbBean.setProjectsId(mapDtoToProject(layer.getProjectsId()));
        dbBean.setDrawingCollection(mapDrawingCollectionToDtoCollection(layer.getDrawingCollection()));

        return dbBean;
    }

    public static List<ProjectLayer> mapLayerCollectionToDtoCollection(Collection<ProjectLayerDTO> layers) {
        if (layers == null)
            return new ArrayList<ProjectLayer>();
        
        List<ProjectLayer> dbBean = null;
        dbBean = layers.stream()
                .map(l -> mapLayerToDto(l))
                .collect(Collectors.toList());

        return dbBean;
    }

    public static Employee mapEmployeeToDto(EmployeeDTO employee) {
        Employee dbBean = new Employee();
        
        if (employee == null)
            return null;

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
        if (user == null)
            return null;
        dbBean.setId(user.getId());
        dbBean.setUsername(user.getUsername());
        dbBean.setPassword(user.getPassword());
        dbBean.setEmployeeCollection(mapEmployeeCollectionToDtoCollection(user.getEmployeeCollection()));

        return dbBean;
    }

    public static List<Employee> mapEmployeeCollectionToDtoCollection(Collection<EmployeeDTO> employees) {
        if (employees == null)
            return new ArrayList<Employee>();
        
        List<Employee> dbBean = null;
        dbBean = employees.stream()
                .map(emp -> mapEmployeeToDto(emp))
                .collect(Collectors.toList());

        return dbBean;
    }

    public static Project mapDtoToProject(ProjectDTO project) {
        Project dbBean = new Project();
        
        if (project == null)
            return null;
        
        dbBean.setId(project.getId());
        dbBean.setName(project.getName());
        dbBean.setEmployeeCollection(mapEmployeeCollectionToDtoCollection(project.getEmployeeCollection()));
        dbBean.setProjectCategoriesId(mapCategoryToDto(project.getProjectCategoriesId()));
        dbBean.setProjectLayerCollection(mapLayerCollectionToDtoCollection(project.getProjectLayerCollection()));

        return dbBean;
    }

    public static List<Project> mapProjectCollecitonToDtoCollection(Collection<ProjectDTO> projects) {
        List<Project> dbBean = new ArrayList<>();
        
        if(projects == null)
            return dbBean;
        
        dbBean = projects.stream()
                .map(p -> mapDtoToProject(p))
                .collect(Collectors.toList());

        return dbBean;
    }

    public static ProjectCategory mapCategoryToDto(ProjectCategoryDTO category) {
        
        ProjectCategory dbBean = new ProjectCategory();
        if (category ==null)
            return null;
        
        dbBean.setId(category.getId());
        dbBean.setName(category.getName());
        dbBean.setProjectCollection(mapProjectCollecitonToDtoCollection(category.getProjectCollection()));

        return dbBean;
    }

    public static List<ProjectCategory> mapCategoryCollectionToDtoCollection(Collection<ProjectCategoryDTO> categories) {
        List<ProjectCategory> dbBean = new ArrayList<>();
        if (categories == null)
            return dbBean;
        
        dbBean = categories.stream()
                .map(c -> mapCategoryToDto(c))
                .collect(Collectors.toList());

        return dbBean;
    }

    public static EmployeePosition mapPositionToDto(EmployeePositionDTO position) {
        EmployeePosition dbBean = new EmployeePosition();
        if(position == null)
            return null;
        
        dbBean.setId(position.getId());
        dbBean.setPosition(position.getPosition());
        dbBean.setEmployeeCollection(mapEmployeeCollectionToDtoCollection(position.getEmployeeCollection()));

        return dbBean;
    }

    public static Drawing mapDrawingToDto(DrawingDTO drawing) {
        Drawing dbBean = new Drawing();
        if (drawing == null)
            return null;

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
        List<Drawing> dbBean = new ArrayList<>();
        if(drawings == null)
            return dbBean;
        

        dbBean = drawings.stream()
                .map(d -> mapDrawingToDto(d))
                .collect(Collectors.toList());

        return dbBean;
    }
}

