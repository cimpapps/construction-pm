/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.service.test;

import com.cimpapps.construction.pm.models.ProjectLayer;
import com.cimpapps.construction.pm.service.ProjectService;
import construction.pm.lib.dto.ProjectDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cimbonda
 */
public class ProjectServiceTest {
    
    @Test
    public void insertProject(){
        ProjectDTO project = new ProjectDTO();
        project.setId(324346);
        project.setName("dsdsdsada");
        
        
        
        List<ProjectLayer> layers = null;
        
        ProjectService.getInstance().addProject(project);
    }
}
