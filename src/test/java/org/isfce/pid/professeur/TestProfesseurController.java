package org.isfce.pid.professeur;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.isfce.pid.controller.dto.ProfesseurDto;
import org.isfce.pid.model.Professeur;
import org.isfce.pid.model.Roles;
import org.isfce.pid.model.User;
import org.isfce.pid.service.ProfesseurServices;
import org.isfce.pid.service.UserServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("testU")
@SpringBootTest
@AutoConfigureMockMvc
@MockBeans({ @MockBean(UserServices.class), @MockBean(ProfesseurServices.class) })
//Permet d'exécuter @BeforeAll sans être une méthode static
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) 
class TestProfesseurController {
	// User pour les tests
	static Professeur profVo, profWaf, profNew;
	// static Etudiant et1;

	@Autowired
	MockMvc mockMvc; // Mock MVC auto construit

	/**
	 * Injection des mocks dont il faudra initialiser le comportement
	 * dans @BeforeAll
	 */
	@Autowired
	ProfesseurServices mockProfSrv;
	@Autowired
	UserServices mockUserSrv;
	
	/**
	 * Paramètres pour des tests paramètrés Permettra de lancer un même test avec
	 * des utilisateurs différents et dont le résultat du test doit être le même
	 * 
	 * @return liste des utilisateurs
	 */
	static Stream<Arguments> listeADMIN() {
		return Stream.of(arguments("admin", new String[] { "ADMIN" }));
	}

	/**
	 * Paramètres pour des tests paramètrés Permettra de lancer un même test avec
	 * des utilisateurs différents et dont le résultat du test doit être le même
	 * 
	 * @return liste des utilisateurs
	 */
	static Stream<Arguments> listeVO_ADMIN() {
		return Stream.of(arguments("admin", new String[] { "ADMIN" }), arguments("vo", new String[] { "PROF" }));
	}

	/**
	 * Idem pour les différents Roles
	 * 
	 * @return 3 utilisateurs avec des roles différents
	 */
	static Stream<Arguments> listeALL_USERS() {
		return Stream.of(arguments("admin", new String[] { "ADMIN" }), arguments("vo", new String[] { "PROF" }),
				arguments("et1", new String[] { "ETUDIANT" }));
	}

	@BeforeEach // Réinitialise les données avant chaque test
	public void initMockDAO() {
		log.debug("Création des données de test");
		profVo = new Professeur(1, "Van Oudenhove", "Didier", "vo@isfce.be", new User("vo", "secretVo1", Roles.ROLE_PROF));
		profWaf = new Professeur(2, "Wafflar", "wa", "waf@isfce.be", new User("wa", "secretWa1", Roles.ROLE_PROF));
		profNew = new Professeur(3, "NewProf", "New", "new@isfce.be", new User("new", "secretNew1", Roles.ROLE_PROF));
		// et1 =new Etudiant("Etudiant1", "Et1", "email@hh.com", "02/647.25.69",
		// new User("et1", "secretEt1", Roles.ROLE_ETUDIANT));
		// Mock de la couche de service
		when(mockProfSrv.existsById(1)).thenReturn(true);
		when(mockProfSrv.existsById(2)).thenReturn(true);
		when(mockProfSrv.findAll()).thenReturn(Arrays.asList(profVo, profWaf));
		when(mockProfSrv.existsByNomPrenom(profVo.getNom(), profVo.getPrenom())).thenReturn(true);
		when(mockProfSrv.existsByNomPrenom(profWaf.getNom(), profWaf.getPrenom())).thenReturn(true);
		when(mockProfSrv.existsByUserName("vo")).thenReturn(true);
		when(mockProfSrv.existsByUserName("wa")).thenReturn(true);
		when(mockProfSrv.getById(1)).thenReturn(Optional.of(profVo));
		when(mockProfSrv.getById(2)).thenReturn(Optional.of(profWaf));
		when(mockProfSrv.getByUserName("vo")).thenReturn(Optional.of(profVo));
		when(mockProfSrv.getByUserName("wa")).thenReturn(Optional.of(profWaf));
		when(mockProfSrv.save(profVo)).thenReturn(profVo);
		when(mockProfSrv.save(profWaf)).thenReturn(profWaf);
		// MockuserSrv
		when(mockUserSrv.createUser(profNew.getUser())).thenReturn(profNew.getUser());
		when(mockUserSrv.existsById("vo")).thenReturn(true);
		when(mockUserSrv.existsById("wa")).thenReturn(true);
		when(mockUserSrv.existsById("et1")).thenReturn(true);
		when(mockUserSrv.findById("vo")).thenReturn(Optional.of(profVo.getUser()));
		when(mockUserSrv.findById("wa")).thenReturn(Optional.of(profWaf.getUser()));
		when(mockUserSrv.createUser(profNew.getUser())).thenReturn(profNew.getUser());
	}

	@ParameterizedTest
	@MethodSource("listeALL_USERS")
	void testListeProf(String username, String[] listeRoles) throws Exception {
		mockMvc.perform(get("/professeur/liste").with(user(username).roles(listeRoles)))
				.andExpect(view().name("professeur/listeProfesseur"))
				.andExpect(model().attribute("professeurList", List.of(profVo, profWaf)));
	}

	@ParameterizedTest
	@MethodSource("listeVO_ADMIN")
	void testgetProf(String username, String[] listeRoles) throws Exception {
		mockMvc.perform(get("/professeur/1").with(user(username).roles(listeRoles)))
				.andExpect(view().name("professeur/professeur")).andExpect(model().attribute("professeur", profVo));
		mockMvc.perform(get("/professeur/?username=vo").with(user(username).roles(listeRoles)))
				.andExpect(view().name("professeur/professeur")).andExpect(model().attribute("professeur", profVo));
	}

	@ParameterizedTest
	@MethodSource("listeVO_ADMIN") // PAs étudiant car bloqué sur l'URL
	void testgetInconnu(String username, String[] listeRoles) throws Exception {
		mockMvc.perform(get("/professeur/999").with(user(username).roles(listeRoles))).andExpect(view().name("error"))
				.andExpect(model().attributeExists("exception"));

		mockMvc.perform(get("/professeur/?username=brol").with(user(username).roles(listeRoles)))
				.andExpect(view().name("error")).andExpect(model().attributeExists("exception"));
	}

	@Test
	void testgetProfDiffAuth() throws Exception {
		// Redirection vers la page de login
		mockMvc.perform(get("/professeur/1")).andExpect(status().is3xxRedirection());
		mockMvc.perform(get("/professeur/?username=vo")).andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" }, password = "admin")
	void addProfesseurGet() throws Exception {
		mockMvc.perform(get("/professeur/add"))
				.andExpect(view().name("professeur/addProfesseur")).andExpect(model().attributeExists("professeur"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" }, password = "admin")	
	void addProfesseurPost() throws Exception {
		mockMvc.perform(post("/professeur/add").with(csrf())
				.param("nom", profNew.getNom()).param("prenom", profNew.getPrenom()).param("email", profNew.getEmail())
				.param("user.username", profNew.getUser().getUsername())
				.param("user.password", profNew.getUser().getUsername())
				.param("user.confirmPassword", profNew.getUser().getUsername())
				.param("user.role", profNew.getUser().getRole().toString())).andExpect(status().isOk());
	}

	// Test Post with erreur
	@ParameterizedTest
	@MethodSource("listeADMIN")
	void addProfesseurPostErreur(String username, String[] listeRoles) throws Exception {
		mockMvc.perform(post("/professeur/add").with(csrf()).with(user(username).roles(listeRoles))
				.param("nom", profNew.getNom()).param("prenom", profNew.getPrenom()).param("email", "bademail")
				.param("user.username", profNew.getUser().getUsername()).param("user.password", "badpw1")
				.param("user.confirmPassword", "badpw2").param("user.role", profNew.getUser().getRole().toString()))
				.andExpect(status().isOk()).andExpect(view().name("professeur/addProfesseur"))
				.andExpect(model().attributeHasFieldErrors("professeur", "email"))
				.andExpect(model().attributeHasFieldErrors("professeur", "user.password"))
				.andExpect(model().attributeHasFieldErrors("professeur", "user.confirmPassword"))
				.andExpect(model().attributeErrorCount("professeur", 3))
				.andExpect(model().attributeExists("professeur"));
		// Erreur doublon username
		mockMvc.perform(post("/professeur/add").with(csrf()).with(user(username).roles(listeRoles))
				.param("nom", profNew.getNom()).param("prenom", profNew.getPrenom()).param("email", profNew.getEmail())
				.param("user.username", profVo.getUser().getUsername())
				.param("user.password", profNew.getUser().getPassword())
				.param("user.confirmPassword", profNew.getUser().getPassword())
				.param("user.role", profNew.getUser().getRole().toString())).andExpect(status().isOk())
				.andExpect(view().name("professeur/addProfesseur"))
				.andExpect(model().attributeHasFieldErrors("professeur", "user.username"))
				.andExpect(model().attributeErrorCount("professeur", 1))
				.andExpect(model().attributeExists("professeur"));
		// Erreur doublon nom prenom
		mockMvc.perform(post("/professeur/add").with(csrf()).with(user(username).roles(listeRoles))
				.param("nom", profVo.getNom()).param("prenom", profVo.getPrenom()).param("email", profNew.getEmail())
				.param("user.username", profNew.getUser().getUsername())
				.param("user.password", profNew.getUser().getPassword())
				.param("user.confirmPassword", profNew.getUser().getPassword())
				.param("user.role", profNew.getUser().getRole().toString())).andExpect(status().isOk())
				.andExpect(view().name("professeur/addProfesseur"))
				.andExpect(model().attributeErrorCount("professeur", 1))
				.andExpect(model().attributeExists("professeur"));
	}

//TestUpdate
	@ParameterizedTest
	@MethodSource("listeVO_ADMIN")
	void updateProfesseurGet(String username, String[] listeRoles) throws Exception {
		mockMvc.perform(get("/professeur/1/update").with(user(username).roles(listeRoles)))
				.andExpect(view().name("professeur/updateProfesseur")).andExpect(model().attributeExists("professeur"));
	}

	@ParameterizedTest
	@MethodSource("listeVO_ADMIN")
	void updateProfesseurPost(String username, String[] listeRoles) throws Exception {
		mockMvc.perform(post("/professeur/1/update").with(csrf()).with(user(username).roles(listeRoles))
				.param("id", "1").param("nom", profVo.getNom() + 2).param("prenom", profVo.getPrenom() + 2)
				.param("email", profVo.getEmail() + 'T').param("user.username", profVo.getUser().getUsername())
				.param("user.password", profVo.getUser().getUsername())
				.param("user.confirmPassword", profVo.getUser().getUsername())
				.param("user.role", profVo.getUser().getRole().toString())).andExpect(status().is(302))
				.andExpect(redirectedUrl("/professeur/1")).andExpect(flash().attributeExists("professeur"));
	}

	@ParameterizedTest
	@MethodSource("listeVO_ADMIN")
	void updateErreurProfesseurPost(String username, String[] listeRoles) throws Exception {
		Professeur profBadUpdate = new Professeur(1, profWaf.getNom(), profWaf.getPrenom(), "bad", profVo.getUser());
		mockMvc.perform(post("/professeur/1/update").with(csrf()).with(user(username).roles(listeRoles))
				.param("id", "1").param("nom", profBadUpdate.getNom()).param("prenom", profBadUpdate.getPrenom())
				.param("email", profBadUpdate.getEmail()).param("user.username", profBadUpdate.getUser().getUsername())
				.param("user.password", profBadUpdate.getUser().getPassword())
				.param("user.confirmPassword", profBadUpdate.getUser().getPassword())
				.param("user.role", profBadUpdate.getUser().getRole().toString())).andExpect(status().isOk())
				.andExpect(view().name("professeur/updateProfesseur"))
				.andExpect(model().attributeHasFieldErrors("professeur", "email")).andExpect(model().errorCount(1))
				.andExpect(model().attribute("professeur", ProfesseurDto.toDto(profBadUpdate)));
//erreur sur nom prenom
		mockMvc.perform(post("/professeur/1/update").with(csrf()).with(user(username).roles(listeRoles))
				.param("id", "1").param("nom", profBadUpdate.getNom()).param("prenom", profBadUpdate.getPrenom())
				.param("email", profVo.getEmail()).param("user.username", profBadUpdate.getUser().getUsername())
				.param("user.password", profBadUpdate.getUser().getPassword())
				.param("user.confirmPassword", profBadUpdate.getUser().getPassword())
				.param("user.role", profBadUpdate.getUser().getRole().toString())).andExpect(status().isOk())
				.andExpect(view().name("professeur/updateProfesseur")).andExpect(model().errorCount(1));
//erreur sur id inconnu
		mockMvc.perform(post("/professeur/999/update").with(csrf()).with(user(username).roles(listeRoles))
				.param("id", "999").param("nom", "huhuuh").param("prenom", "uuuiji").param("email", profVo.getEmail())
				.param("user.username", profBadUpdate.getUser().getUsername())
				.param("user.password", profBadUpdate.getUser().getPassword())
				.param("user.confirmPassword", profBadUpdate.getUser().getPassword())
				.param("user.role", profBadUpdate.getUser().getRole().toString())).andExpect(status().isOk())
				.andExpect(view().name("error")).andExpect(model().attributeExists("exception"));

	}

//Pour modifier un prof, il faut être admin ou le prof en question
	@Test
	@WithMockUser(username = "wa", roles = "PROF")
	void updateProfesseurPasWAPost() throws Exception {
		mockMvc.perform(post("/professeur/1/update").with(csrf()).param("id", "1").param("nom", profVo.getNom() + 2)
				.param("prenom", profVo.getPrenom() + 2).param("email", profVo.getEmail() + 'T')
				.param("user.username", profVo.getUser().getUsername())
				.param("user.password", profVo.getUser().getUsername())
				.param("user.confirmPassword", profVo.getUser().getUsername())
				.param("user.role", profVo.getUser().getRole().toString())).andExpect(status().is(403));
	}

	//TestDelete
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" }, password = "admin")
	void deleteProfesseurPost() throws Exception {
		// OK
		mockMvc.perform(post("/professeur/2/delete").with(csrf()))
				.andExpect(status().is(302)).andExpect(redirectedUrl("/professeur/liste"));
		// Avec prof inconnu
		mockMvc.perform(post("/professeur/999/delete").with(csrf()))
				.andExpect(status().isOk()).andExpect(view().name("error"))
				.andExpect(model().attributeExists("exception"));
	}

	@ParameterizedTest
	@MethodSource("listeVO_ADMIN")
	void testCheckID(String username, String[] listeRoles) throws Exception {
		// id existant
		mockMvc.perform(get("/user/check/vo").with(user(username).roles(listeRoles)))
				.andExpect(content().string("true")).andExpect(status().isOk());
		// id inconnu
		mockMvc.perform(get("/user/check/brol").with(user(username).roles(listeRoles)))
				.andExpect(content().string("false")).andExpect(status().isOk());
	}

}
