package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.ContributionType;
import org.church.repository.ContributionTypeRepository;
import org.church.service.ContributionTypeService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.ContributionTypeCriteria;
import org.church.service.ContributionTypeQueryService;

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
 * Integration tests for the {@link ContributionTypeResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class ContributionTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ContributionTypeRepository contributionTypeRepository;

    @Autowired
    private ContributionTypeService contributionTypeService;

    @Autowired
    private ContributionTypeQueryService contributionTypeQueryService;

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

    private MockMvc restContributionTypeMockMvc;

    private ContributionType contributionType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContributionTypeResource contributionTypeResource = new ContributionTypeResource(contributionTypeService, contributionTypeQueryService);
        this.restContributionTypeMockMvc = MockMvcBuilders.standaloneSetup(contributionTypeResource)
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
    public static ContributionType createEntity(EntityManager em) {
        ContributionType contributionType = new ContributionType()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME);
        return contributionType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContributionType createUpdatedEntity(EntityManager em) {
        ContributionType contributionType = new ContributionType()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME);
        return contributionType;
    }

    @BeforeEach
    public void initTest() {
        contributionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createContributionType() throws Exception {
        int databaseSizeBeforeCreate = contributionTypeRepository.findAll().size();

        // Create the ContributionType
        restContributionTypeMockMvc.perform(post("/api/contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionType)))
            .andExpect(status().isCreated());

        // Validate the ContributionType in the database
        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ContributionType testContributionType = contributionTypeList.get(contributionTypeList.size() - 1);
        assertThat(testContributionType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testContributionType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createContributionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contributionTypeRepository.findAll().size();

        // Create the ContributionType with an existing ID
        contributionType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContributionTypeMockMvc.perform(post("/api/contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionType)))
            .andExpect(status().isBadRequest());

        // Validate the ContributionType in the database
        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contributionTypeRepository.findAll().size();
        // set the field null
        contributionType.setName(null);

        // Create the ContributionType, which fails.

        restContributionTypeMockMvc.perform(post("/api/contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionType)))
            .andExpect(status().isBadRequest());

        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContributionTypes() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList
        restContributionTypeMockMvc.perform(get("/api/contribution-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getContributionType() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get the contributionType
        restContributionTypeMockMvc.perform(get("/api/contribution-types/{id}", contributionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contributionType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getAllContributionTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where code equals to DEFAULT_CODE
        defaultContributionTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the contributionTypeList where code equals to UPDATED_CODE
        defaultContributionTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllContributionTypesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where code not equals to DEFAULT_CODE
        defaultContributionTypeShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the contributionTypeList where code not equals to UPDATED_CODE
        defaultContributionTypeShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllContributionTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultContributionTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the contributionTypeList where code equals to UPDATED_CODE
        defaultContributionTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllContributionTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where code is not null
        defaultContributionTypeShouldBeFound("code.specified=true");

        // Get all the contributionTypeList where code is null
        defaultContributionTypeShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllContributionTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where code contains DEFAULT_CODE
        defaultContributionTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the contributionTypeList where code contains UPDATED_CODE
        defaultContributionTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllContributionTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where code does not contain DEFAULT_CODE
        defaultContributionTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the contributionTypeList where code does not contain UPDATED_CODE
        defaultContributionTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllContributionTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where name equals to DEFAULT_NAME
        defaultContributionTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the contributionTypeList where name equals to UPDATED_NAME
        defaultContributionTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContributionTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where name not equals to DEFAULT_NAME
        defaultContributionTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the contributionTypeList where name not equals to UPDATED_NAME
        defaultContributionTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContributionTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultContributionTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the contributionTypeList where name equals to UPDATED_NAME
        defaultContributionTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContributionTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where name is not null
        defaultContributionTypeShouldBeFound("name.specified=true");

        // Get all the contributionTypeList where name is null
        defaultContributionTypeShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllContributionTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where name contains DEFAULT_NAME
        defaultContributionTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the contributionTypeList where name contains UPDATED_NAME
        defaultContributionTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContributionTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        contributionTypeRepository.saveAndFlush(contributionType);

        // Get all the contributionTypeList where name does not contain DEFAULT_NAME
        defaultContributionTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the contributionTypeList where name does not contain UPDATED_NAME
        defaultContributionTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContributionTypeShouldBeFound(String filter) throws Exception {
        restContributionTypeMockMvc.perform(get("/api/contribution-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restContributionTypeMockMvc.perform(get("/api/contribution-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContributionTypeShouldNotBeFound(String filter) throws Exception {
        restContributionTypeMockMvc.perform(get("/api/contribution-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContributionTypeMockMvc.perform(get("/api/contribution-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingContributionType() throws Exception {
        // Get the contributionType
        restContributionTypeMockMvc.perform(get("/api/contribution-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContributionType() throws Exception {
        // Initialize the database
        contributionTypeService.save(contributionType);

        int databaseSizeBeforeUpdate = contributionTypeRepository.findAll().size();

        // Update the contributionType
        ContributionType updatedContributionType = contributionTypeRepository.findById(contributionType.getId()).get();
        // Disconnect from session so that the updates on updatedContributionType are not directly saved in db
        em.detach(updatedContributionType);
        updatedContributionType
            .code(UPDATED_CODE)
            .name(UPDATED_NAME);

        restContributionTypeMockMvc.perform(put("/api/contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContributionType)))
            .andExpect(status().isOk());

        // Validate the ContributionType in the database
        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeUpdate);
        ContributionType testContributionType = contributionTypeList.get(contributionTypeList.size() - 1);
        assertThat(testContributionType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testContributionType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingContributionType() throws Exception {
        int databaseSizeBeforeUpdate = contributionTypeRepository.findAll().size();

        // Create the ContributionType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContributionTypeMockMvc.perform(put("/api/contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionType)))
            .andExpect(status().isBadRequest());

        // Validate the ContributionType in the database
        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContributionType() throws Exception {
        // Initialize the database
        contributionTypeService.save(contributionType);

        int databaseSizeBeforeDelete = contributionTypeRepository.findAll().size();

        // Delete the contributionType
        restContributionTypeMockMvc.perform(delete("/api/contribution-types/{id}", contributionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContributionType> contributionTypeList = contributionTypeRepository.findAll();
        assertThat(contributionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionType.class);
        ContributionType contributionType1 = new ContributionType();
        contributionType1.setId(1L);
        ContributionType contributionType2 = new ContributionType();
        contributionType2.setId(contributionType1.getId());
        assertThat(contributionType1).isEqualTo(contributionType2);
        contributionType2.setId(2L);
        assertThat(contributionType1).isNotEqualTo(contributionType2);
        contributionType1.setId(null);
        assertThat(contributionType1).isNotEqualTo(contributionType2);
    }
}
