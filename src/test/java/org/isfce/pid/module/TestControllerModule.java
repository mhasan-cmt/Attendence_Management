package org.isfce.pid.module;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.isfce.pid.controller.CoursController;
import org.isfce.pid.controller.ModuleController;
import org.isfce.pid.controller.dto.ModulesDto;
import org.isfce.pid.model.Cours;
import org.isfce.pid.model.Professeur;
import org.isfce.pid.model.Roles;
import org.isfce.pid.model.User;
import org.isfce.pid.model.Module.MAS;
import org.isfce.pid.service.CoursServices;
import org.isfce.pid.service.ModuleServices;
import org.isfce.pid.service.ProfesseurServices;
import org.isfce.pid.service.UserServices;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("testU")
public class TestControllerModule {
	//  pour les 4 premiers tests
	static Module modulePID, moduleSO2, moduleTest;
	//  pour les cours
	static Cours coursPID, coursSO2, coursTest;
	// pour les profs 
	static Professeur profVo, profWaf, profNew;
	
	// les mock auto construit
	@Autowired
	MockMvc mockMvc; // Mock MVC auto construit
	@Autowired
	ProfesseurServices mockProfSrv;
	@Autowired
	UserServices mockUserSrv;
	@Autowired
	CoursServices mockCoursDAO;
	@Autowired
	ModuleServices mockModuleDAO;
	
	static Stream<Arguments> listeADMIN() {
		return Stream.of(arguments("admin", new String[] { "ADMIN" }));
	}
	
	static Stream<Arguments> listeVO_ADMIN() {
		return Stream.of(arguments("admin", new String[] { "ADMIN" }), arguments("vo", new String[] { "PROF" }));
	}
	
	static Stream<Arguments> listeALL_USERS() {
		return Stream.of(arguments("admin", new String[] { "ADMIN" }), arguments("vo", new String[] { "PROF" }),
				arguments("et1", new String[] { "ETUDIANT" }));
	}
	
	/**
	 * Initialisation d'un mock de CoursService, utilis� dans les x premiers tests
	 */
	@BeforeAll
	public static void initMockDAO() {
		log.debug("Création des données de test");
		// prof
		profVo = new Professeur(1, "Van Oudenhove", "Didier", "vo@isfce.be", new User("vo", "secretVo1", Roles.ROLE_PROF));
		profWaf = new Professeur(2, "Wafflar", "wa", "waf@isfce.be", new User("wa", "secretWa1", Roles.ROLE_PROF));
		profNew = new Professeur(3, "NewProf", "New", "new@isfce.be", new User("new", "secretNew1", Roles.ROLE_PROF));
		// cours
		coursPID = new Cours("IPID", "Projet de developpement", (short) 60);
		coursPID.setSections(Set.of("Informatique"));
		coursSO2 = new Cours("ISO2", "Structure des ordinateurs", (short) 60);
		coursSO2.setSections(Set.of("Informatique"));
		coursTest = new Cours("ITEST", "TEST", (short) 60);
		coursTest.setSections(Set.of("Informatique"));
	}
	
	@Test
	public void testListeModule() throws Exception {
		mockCoursDAO = mock(CoursServices.class);
		mockModuleDAO = mock(ModuleServices.class);
		mockProfSrv = mock(ProfesseurServices.class);
		
		ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
		mockMvc = standaloneSetup(moduleController).build();
		mockMvc.perform(get("/module/liste")).andExpect(view().name("module/listeModules"))
				.andExpect(model().attributeExists("modulesList"));
	}
	
	@Test
	public void testCodeCoursInvalide() throws Exception {
		mockCoursDAO = mock(CoursServices.class);
		mockModuleDAO = mock(ModuleServices.class);
		mockProfSrv = mock(ProfesseurServices.class);
		
		ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
		mockMvc = standaloneSetup(moduleController).build();
		/*mockMvc.perform(get("/module/POPI/add")).andExpect(status().is3xxRedirection())
		.andExpect(view().name("/error"));*/
		 // Arrange
	    when(mockCoursDAO.findOne("POPI")).thenReturn(Optional.empty());
	    when(mockCoursDAO.findOne(coursPID.getCode())).thenReturn(Optional.ofNullable(coursPID));
	    // Act & Assert
	    mockMvc.perform(get("/module/POPI/add")).andExpect(status().isFound()).andExpect(view().name("redirect:/error"));
	}
	
	@Test
	public void testCodeCoursValide() throws Exception {
		mockCoursDAO = mock(CoursServices.class);
		mockModuleDAO = mock(ModuleServices.class);
		mockProfSrv = mock(ProfesseurServices.class);
		
		ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
		mockMvc = standaloneSetup(moduleController).build();

		when(mockCoursDAO.findOne("IPID")).thenReturn(Optional.ofNullable(coursPID));
		
	    mockMvc.perform(get("/module/IPID/add"))
	    		.andExpect(view().name("module/addModule"))
	    		.andExpect(model().attributeExists("module", "mas", "profs"));
	}
	
	@Test
	public void testAddModulePostInvalidDate() throws Exception {
	    mockCoursDAO = mock(CoursServices.class);
	    mockModuleDAO = mock(ModuleServices.class);
	    mockProfSrv = mock(ProfesseurServices.class);
	    
	    ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
	    mockMvc = standaloneSetup(moduleController).build();
	    
	    when(mockModuleDAO.generateCodeModule("IPID")).thenReturn("IPID-1-A");
	    when(mockProfSrv.getByUserName(profVo.getUser().getUsername())).thenReturn(Optional.ofNullable(profVo));
	    when(mockCoursDAO.findOne(coursPID.getCode())).thenReturn(Optional.ofNullable(coursPID));
	    
	    ModulesDto modules = new ModulesDto(mockModuleDAO.generateCodeModule("IPID"), LocalDate.now(), LocalDate.now().plusDays(5), MAS.MATIN, "IPID", profVo.getUser().getUsername());

	    mockMvc.perform(post("/module/IPID/add")
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .param("code", modules.getCode())
	            .param("dateDebut", modules.getDateDebut().toString())
	            .param("dateFin", modules.getDateFin().toString())
	            .param("moment", modules.getMoment().toString())
	            .param("coursCode", modules.getCoursCode())
	            .param("profUsername", modules.getProfUsername()))
			    .andExpect(status().isOk())
				.andExpect(view().name("module/addModule"));
				//.andExpect(model().attributeHasFieldErrorCode("module", "dateDebut", "module.dateDebut.notvalid"));
	}
	
	@Test
	public void testAddModulePostCodeNonEncoder() throws Exception {
	    mockCoursDAO = mock(CoursServices.class);
	    mockModuleDAO = mock(ModuleServices.class);
	    mockProfSrv = mock(ProfesseurServices.class);
	    
	    ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
	    mockMvc = standaloneSetup(moduleController).build();
	    
	    when(mockModuleDAO.generateCodeModule("IPID")).thenReturn("IPID-1-A");
	    when(mockProfSrv.getByUserName(profVo.getUser().getUsername())).thenReturn(Optional.ofNullable(profVo));
	    when(mockCoursDAO.findOne(coursPID.getCode())).thenReturn(Optional.ofNullable(coursPID));
	    
	    ModulesDto modules = new ModulesDto(mockModuleDAO.generateCodeModule("IPID"), LocalDate.now(), LocalDate.now().plusDays(5), MAS.MATIN, "IPID", profVo.getUser().getUsername());

	    mockMvc.perform(post("/module/IPID/add")
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            //.param("code", modules.getCode())
	            .param("dateDebut", modules.getDateDebut().toString())
	            .param("dateFin", modules.getDateFin().toString())
	            .param("moment", modules.getMoment().toString())
	            .param("coursCode", modules.getCoursCode())
	            .param("profUsername", modules.getProfUsername()))
			    .andExpect(status().isOk())
				.andExpect(view().name("module/addModule"));
				//.andExpect(model().attributeHasFieldErrorCode("module", "dateDebut", "module.dateDebut.notvalid"));
	}
	
	@Test
	public void testAddModulePostDateDebutNonEncoder() throws Exception {
	    mockCoursDAO = mock(CoursServices.class);
	    mockModuleDAO = mock(ModuleServices.class);
	    mockProfSrv = mock(ProfesseurServices.class);
	    
	    ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
	    mockMvc = standaloneSetup(moduleController).build();
	    
	    when(mockModuleDAO.generateCodeModule("IPID")).thenReturn("IPID-1-A");
	    when(mockProfSrv.getByUserName(profVo.getUser().getUsername())).thenReturn(Optional.ofNullable(profVo));
	    when(mockCoursDAO.findOne(coursPID.getCode())).thenReturn(Optional.ofNullable(coursPID));
	    
	    ModulesDto modules = new ModulesDto(mockModuleDAO.generateCodeModule("IPID"), LocalDate.now(), LocalDate.now().plusDays(5), MAS.MATIN, "IPID", profVo.getUser().getUsername());

	    mockMvc.perform(post("/module/IPID/add")
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .param("code", modules.getCode())
	            //.param("dateDebut", modules.getDateDebut().toString())
	            .param("dateFin", modules.getDateFin().toString())
	            .param("moment", modules.getMoment().toString())
	            .param("coursCode", modules.getCoursCode())
	            .param("profUsername", modules.getProfUsername()))
			    .andExpect(status().isOk())
				.andExpect(view().name("module/addModule"));
				//.andExpect(model().attributeHasFieldErrorCode("module", "dateDebut", "module.dateDebut.notvalid"));
	}
	
	@Test
	public void testAddModulePostDateFinNonEncoder() throws Exception {
	    mockCoursDAO = mock(CoursServices.class);
	    mockModuleDAO = mock(ModuleServices.class);
	    mockProfSrv = mock(ProfesseurServices.class);
	    
	    ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
	    mockMvc = standaloneSetup(moduleController).build();
	    
	    when(mockModuleDAO.generateCodeModule("IPID")).thenReturn("IPID-1-A");
	    when(mockProfSrv.getByUserName(profVo.getUser().getUsername())).thenReturn(Optional.ofNullable(profVo));
	    when(mockCoursDAO.findOne(coursPID.getCode())).thenReturn(Optional.ofNullable(coursPID));
	    
	    ModulesDto modules = new ModulesDto(mockModuleDAO.generateCodeModule("IPID"), LocalDate.now(), LocalDate.now().plusDays(5), MAS.MATIN, "IPID", profVo.getUser().getUsername());

	    mockMvc.perform(post("/module/IPID/add")
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .param("code", modules.getCode())
	            .param("dateDebut", modules.getDateDebut().toString())
	            //.param("dateFin", modules.getDateFin().toString())
	            .param("moment", modules.getMoment().toString())
	            .param("coursCode", modules.getCoursCode())
	            .param("profUsername", modules.getProfUsername()))
			    .andExpect(status().isOk())
				.andExpect(view().name("module/addModule"));
				//.andExpect(model().attributeHasFieldErrorCode("module", "dateDebut", "module.dateDebut.notvalid"));
	}

	@Test
	public void testAddModulePostMomentNonEncoder() throws Exception {
	    mockCoursDAO = mock(CoursServices.class);
	    mockModuleDAO = mock(ModuleServices.class);
	    mockProfSrv = mock(ProfesseurServices.class);
	    
	    ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
	    mockMvc = standaloneSetup(moduleController).build();
	    
	    when(mockModuleDAO.generateCodeModule("IPID")).thenReturn("IPID-1-A");
	    when(mockProfSrv.getByUserName(profVo.getUser().getUsername())).thenReturn(Optional.ofNullable(profVo));
	    when(mockCoursDAO.findOne(coursPID.getCode())).thenReturn(Optional.ofNullable(coursPID));
	    
	    ModulesDto modules = new ModulesDto(mockModuleDAO.generateCodeModule("IPID"), LocalDate.now(), LocalDate.now().plusDays(5), MAS.MATIN, "IPID", profVo.getUser().getUsername());

	    mockMvc.perform(post("/module/IPID/add")
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .param("code", modules.getCode())
	            .param("dateDebut", modules.getDateDebut().toString())
	            .param("dateFin", modules.getDateFin().toString())
	            //.param("moment", modules.getMoment().toString())
	            .param("coursCode", modules.getCoursCode())
	            .param("profUsername", modules.getProfUsername()))
			    .andExpect(status().isOk())
				.andExpect(view().name("module/addModule"));
				//.andExpect(model().attributeHasFieldErrorCode("module", "dateDebut", "module.dateDebut.notvalid"));
	}
	
	@Test
	public void testAddModulePostCoursCodeNonEncoder() throws Exception {
	    mockCoursDAO = mock(CoursServices.class);
	    mockModuleDAO = mock(ModuleServices.class);
	    mockProfSrv = mock(ProfesseurServices.class);
	    
	    ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
	    mockMvc = standaloneSetup(moduleController).build();
	    
	    when(mockModuleDAO.generateCodeModule("IPID")).thenReturn("IPID-1-A");
	    when(mockProfSrv.getByUserName(profVo.getUser().getUsername())).thenReturn(Optional.ofNullable(profVo));
	    when(mockCoursDAO.findOne(coursPID.getCode())).thenReturn(Optional.ofNullable(coursPID));
	    
	    ModulesDto modules = new ModulesDto(mockModuleDAO.generateCodeModule("IPID"), LocalDate.now(), LocalDate.now().plusDays(5), MAS.MATIN, "IPID", profVo.getUser().getUsername());

	    mockMvc.perform(post("/module/IPID/add")
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .param("code", modules.getCode())
	            .param("dateDebut", modules.getDateDebut().toString())
	            .param("dateFin", modules.getDateFin().toString())
	            .param("moment", modules.getMoment().toString())
	            //.param("coursCode", modules.getCoursCode())
	            .param("profUsername", modules.getProfUsername()))
			    .andExpect(status().isOk())
				.andExpect(view().name("module/addModule"));
				//.andExpect(model().attributeHasFieldErrorCode("module", "dateDebut", "module.dateDebut.notvalid"));
	}
	
	@Test
	public void testAddModulePostProfNonEncoder() throws Exception {
	    mockCoursDAO = mock(CoursServices.class);
	    mockModuleDAO = mock(ModuleServices.class);
	    mockProfSrv = mock(ProfesseurServices.class);
	    
	    ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
	    mockMvc = standaloneSetup(moduleController).build();
	    
	    when(mockModuleDAO.generateCodeModule("IPID")).thenReturn("IPID-1-A");
	    when(mockProfSrv.getByUserName(profVo.getUser().getUsername())).thenReturn(Optional.ofNullable(profVo));
	    when(mockCoursDAO.findOne(coursPID.getCode())).thenReturn(Optional.ofNullable(coursPID));
	    
	    ModulesDto modules = new ModulesDto(mockModuleDAO.generateCodeModule("IPID"), LocalDate.now(), LocalDate.now().plusDays(5), MAS.MATIN, "IPID", profVo.getUser().getUsername());

	    mockMvc.perform(post("/module/IPID/add")
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .param("code", modules.getCode())
	            .param("dateDebut", modules.getDateDebut().toString())
	            .param("dateFin", modules.getDateFin().toString())
	            .param("moment", modules.getMoment().toString())
	            .param("coursCode", modules.getCoursCode())
	            //.param("profUsername", modules.getProfUsername())
	            )
			    .andExpect(status().isOk())
				.andExpect(view().name("module/addModule"));
				//.andExpect(model().attributeHasFieldErrorCode("module", "dateDebut", "module.dateDebut.notvalid"));
	}
	
	@Test
	public void testAddModulePostReussi() throws Exception {
	    mockCoursDAO = mock(CoursServices.class);
	    mockModuleDAO = mock(ModuleServices.class);
	    mockProfSrv = mock(ProfesseurServices.class);
	    
	    ModuleController moduleController = new ModuleController(mockModuleDAO, mockProfSrv, mockCoursDAO);
	    mockMvc = standaloneSetup(moduleController).build();
	    
	    when(mockModuleDAO.generateCodeModule("IPID")).thenReturn("IPID-1-A");
	    when(mockProfSrv.getByUserName(profVo.getUser().getUsername())).thenReturn(Optional.ofNullable(profVo));
	    when(mockCoursDAO.findOne(coursPID.getCode())).thenReturn(Optional.ofNullable(coursPID));
	    
	    ModulesDto modules = new ModulesDto(mockModuleDAO.generateCodeModule("IPID"), LocalDate.now(), LocalDate.now().plusDays(5), MAS.MATIN, "IPID", profVo.getUser().getUsername());

	    MvcResult result = mockMvc.perform(post("/module/IPID/add")
	            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	            .param("code", modules.getCode())
	            .param("dateDebut", modules.getDateDebut().toString())
	            .param("dateFin", modules.getDateFin().toString())
	            .param("moment", modules.getMoment().toString())
	            .param("coursCode", modules.getCoursCode())
	            .param("profUsername", modules.getProfUsername()))
			    //.andExpect(status().isOk())
	    		.andExpect(status().isFound())
				.andExpect(redirectedUrl("/module/liste"))
				.andReturn();

		MockHttpServletResponse response = result.getResponse();
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++Error: " + response.getErrorMessage());
		System.out.println("Status code: " + response.getStatus());
	}
}
