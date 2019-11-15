package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.ChurchCommunity;
import org.church.domain.Church;
import org.church.domain.Member;
import org.church.repository.ChurchCommunityRepository;
import org.church.service.ChurchCommunityService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.ChurchCommunityCriteria;
import org.church.service.ChurchCommunityQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.church.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChurchCommunityResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class ChurchCommunityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ChurchCommunityRepository churchCommunityRepository;

    @Autowired
    private ChurchCommunityService churchCommunityService;

    @Autowired
    private ChurchCommunityQueryService churchCommunityQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restChurchCommunityMockMvc;

    private ChurchCommunity churchCommunity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChurchCommunityResource churchCommunityResource = new ChurchCommunityResource(churchCommunityService, churchCommunityQueryService);
        this.restChurchCommunityMockMvc = MockMvcBuilders.standaloneSetup(churchCommunityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChurchCommunity createEntity(EntityManager em) {
        ChurchCommunity churchCommunity = new ChurchCommunity()
            .name(DEFAULT_NAME);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        churchCommunity.setChurch(church);
        return churchCommunity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChurchCommunity createUpdatedEntity(EntityManager em) {
        ChurchCommunity churchCommunity = new ChurchCommunity()
            .name(UPDATED_NAME);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createUpdatedEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        churchCommunity.setChurch(church);
        return churchCommunity;
    }

    @BeforeEach
    public void initTest() {
        churchCommunity = createEntity(em);
    }

    @Test
    @Transactional
    public void createChurchCommunity() throws Exception {
        int databaseSizeBeforeCreate = churchCommunityRepository.findAll().size();

        // Create the ChurchCommunity
        restChurchCommunityMockMvc.perform(post("/api/church-communities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchCommunity)))
            .andExpect(status().isCreated());

        // Validate the ChurchCommunity in the database
        List<ChurchCommunity> churchCommunityList = churchCommunityRepository.findAll();
        assertThat(churchCommunityList).hasSize(databaseSizeBeforeCreate + 1);
        ChurchCommunity testChurchCommunity = churchCommunityList.get(churchCommunityList.size() - 1);
        assertThat(testChurchCommunity.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createChurchCommunityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = churchCommunityRepository.findAll().size();

        // Create the ChurchCommunity with an existing ID
        churchCommunity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChurchCommunityMockMvc.perform(post("/api/church-communities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchCommunity)))
            .andExpect(status().isBadRequest());

        // Validate the ChurchCommunity in the database
        List<ChurchCommunity> churchCommunityList = churchCommunityRepository.findAll();
        assertThat(churchCommunityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchCommunityRepository.findAll().size();
        // set the field null
        churchCommunity.setName(null);

        // Create the ChurchCommunity, which fails.

        restChurchCommunityMockMvc.perform(post("/api/church-communities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchCommunity)))
            .andExpect(status().isBadRequest());

        List<ChurchCommunity> churchCommunityList = churchCommunityRepository.findAll();
        assertThat(churchCommunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChurchCommunities() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);

        // Get all the churchCommunityList
        restChurchCommunityMockMvc.perform(get("/api/church-communities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churchCommunity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getChurchCommunity() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);

        // Get the churchCommunity
        restChurchCommunityMockMvc.perform(get("/api/church-communities/{id}", churchCommunity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(churchCommunity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getAllChurchCommunitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);

        // Get all the churchCommunityList where name equals to DEFAULT_NAME
        defaultChurchCommunityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the churchCommunityList where name equals to UPDATED_NAME
        defaultChurchCommunityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchCommunitiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);

        // Get all the churchCommunityList where name not equals to DEFAULT_NAME
        defaultChurchCommunityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the churchCommunityList where name not equals to UPDATED_NAME
        defaultChurchCommunityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchCommunitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);

        // Get all the churchCommunityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultChurchCommunityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the churchCommunityList where name equals to UPDATED_NAME
        defaultChurchCommunityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchCommunitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);

        // Get all the churchCommunityList where name is not null
        defaultChurchCommunityShouldBeFound("name.specified=true");

        // Get all the churchCommunityList where name is null
        defaultChurchCommunityShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllChurchCommunitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);

        // Get all the churchCommunityList where name contains DEFAULT_NAME
        defaultChurchCommunityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the churchCommunityList where name contains UPDATED_NAME
        defaultChurchCommunityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchCommunitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);

        // Get all the churchCommunityList where name does not contain DEFAULT_NAME
        defaultChurchCommunityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the churchCommunityList where name does not contain UPDATED_NAME
        defaultChurchCommunityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllChurchCommunitiesByChurchIsEqualToSomething() throws Exception {
        // Get already existing entity
        Church church = churchCommunity.getChurch();
        churchCommunityRepository.saveAndFlush(churchCommunity);
        Long churchId = church.getId();

        // Get all the churchCommunityList where church equals to churchId
        defaultChurchCommunityShouldBeFound("churchId.equals=" + churchId);

        // Get all the churchCommunityList where church equals to churchId + 1
        defaultChurchCommunityShouldNotBeFound("churchId.equals=" + (churchId + 1));
    }


    @Test
    @Transactional
    public void getAllChurchCommunitiesByChairPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);
        Member chairPerson = MemberResourceIT.createEntity(em);
        em.persist(chairPerson);
        em.flush();
        churchCommunity.setChairPerson(chairPerson);
        churchCommunityRepository.saveAndFlush(churchCommunity);
        Long chairPersonId = chairPerson.getId();

        // Get all the churchCommunityList where chairPerson equals to chairPersonId
        defaultChurchCommunityShouldBeFound("chairPersonId.equals=" + chairPersonId);

        // Get all the churchCommunityList where chairPerson equals to chairPersonId + 1
        defaultChurchCommunityShouldNotBeFound("chairPersonId.equals=" + (chairPersonId + 1));
    }


    @Test
    @Transactional
    public void getAllChurchCommunitiesBySecretaryIsEqualToSomething() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);
        Member secretary = MemberResourceIT.createEntity(em);
        em.persist(secretary);
        em.flush();
        churchCommunity.setSecretary(secretary);
        churchCommunityRepository.saveAndFlush(churchCommunity);
        Long secretaryId = secretary.getId();

        // Get all the churchCommunityList where secretary equals to secretaryId
        defaultChurchCommunityShouldBeFound("secretaryId.equals=" + secretaryId);

        // Get all the churchCommunityList where secretary equals to secretaryId + 1
        defaultChurchCommunityShouldNotBeFound("secretaryId.equals=" + (secretaryId + 1));
    }


    @Test
    @Transactional
    public void getAllChurchCommunitiesByTreasurerIsEqualToSomething() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);
        Member treasurer = MemberResourceIT.createEntity(em);
        em.persist(treasurer);
        em.flush();
        churchCommunity.setTreasurer(treasurer);
        churchCommunityRepository.saveAndFlush(churchCommunity);
        Long treasurerId = treasurer.getId();

        // Get all the churchCommunityList where treasurer equals to treasurerId
        defaultChurchCommunityShouldBeFound("treasurerId.equals=" + treasurerId);

        // Get all the churchCommunityList where treasurer equals to treasurerId + 1
        defaultChurchCommunityShouldNotBeFound("treasurerId.equals=" + (treasurerId + 1));
    }


    @Test
    @Transactional
    public void getAllChurchCommunitiesByMembersIsEqualToSomething() throws Exception {
        // Initialize the database
        churchCommunityRepository.saveAndFlush(churchCommunity);
        Member members = MemberResourceIT.createEntity(em);
        em.persist(members);
        em.flush();
        churchCommunity.addMembers(members);
        churchCommunityRepository.saveAndFlush(churchCommunity);
        Long membersId = members.getId();

        // Get all the churchCommunityList where members equals to membersId
        defaultChurchCommunityShouldBeFound("membersId.equals=" + membersId);

        // Get all the churchCommunityList where members equals to membersId + 1
        defaultChurchCommunityShouldNotBeFound("membersId.equals=" + (membersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChurchCommunityShouldBeFound(String filter) throws Exception {
        restChurchCommunityMockMvc.perform(get("/api/church-communities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churchCommunity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restChurchCommunityMockMvc.perform(get("/api/church-communities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChurchCommunityShouldNotBeFound(String filter) throws Exception {
        restChurchCommunityMockMvc.perform(get("/api/church-communities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChurchCommunityMockMvc.perform(get("/api/church-communities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChurchCommunity() throws Exception {
        // Get the churchCommunity
        restChurchCommunityMockMvc.perform(get("/api/church-communities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChurchCommunity() throws Exception {
        // Initialize the database
        churchCommunityService.save(churchCommunity);

        int databaseSizeBeforeUpdate = churchCommunityRepository.findAll().size();

        // Update the churchCommunity
        ChurchCommunity updatedChurchCommunity = churchCommunityRepository.findById(churchCommunity.getId()).get();
        // Disconnect from session so that the updates on updatedChurchCommunity are not directly saved in db
        em.detach(updatedChurchCommunity);
        updatedChurchCommunity
            .name(UPDATED_NAME);

        restChurchCommunityMockMvc.perform(put("/api/church-communities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChurchCommunity)))
            .andExpect(status().isOk());

        // Validate the ChurchCommunity in the database
        List<ChurchCommunity> churchCommunityList = churchCommunityRepository.findAll();
        assertThat(churchCommunityList).hasSize(databaseSizeBeforeUpdate);
        ChurchCommunity testChurchCommunity = churchCommunityList.get(churchCommunityList.size() - 1);
        assertThat(testChurchCommunity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingChurchCommunity() throws Exception {
        int databaseSizeBeforeUpdate = churchCommunityRepository.findAll().size();

        // Create the ChurchCommunity

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchCommunityMockMvc.perform(put("/api/church-communities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchCommunity)))
            .andExpect(status().isBadRequest());

        // Validate the ChurchCommunity in the database
        List<ChurchCommunity> churchCommunityList = churchCommunityRepository.findAll();
        assertThat(churchCommunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChurchCommunity() throws Exception {
        // Initialize the database
        churchCommunityService.save(churchCommunity);

        int databaseSizeBeforeDelete = churchCommunityRepository.findAll().size();

        // Delete the churchCommunity
        restChurchCommunityMockMvc.perform(delete("/api/church-communities/{id}", churchCommunity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChurchCommunity> churchCommunityList = churchCommunityRepository.findAll();
        assertThat(churchCommunityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChurchCommunity.class);
        ChurchCommunity churchCommunity1 = new ChurchCommunity();
        churchCommunity1.setId(1L);
        ChurchCommunity churchCommunity2 = new ChurchCommunity();
        churchCommunity2.setId(churchCommunity1.getId());
        assertThat(churchCommunity1).isEqualTo(churchCommunity2);
        churchCommunity2.setId(2L);
        assertThat(churchCommunity1).isNotEqualTo(churchCommunity2);
        churchCommunity1.setId(null);
        assertThat(churchCommunity1).isNotEqualTo(churchCommunity2);
    }
}
