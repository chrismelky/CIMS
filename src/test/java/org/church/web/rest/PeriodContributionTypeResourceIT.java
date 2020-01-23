package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.PeriodContributionType;
import org.church.domain.PeriodType;
import org.church.domain.Church;
import org.church.repository.PeriodContributionTypeRepository;
import org.church.service.PeriodContributionTypeService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.PeriodContributionTypeCriteria;
import org.church.service.PeriodContributionTypeQueryService;

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
 * Integration tests for the {@link PeriodContributionTypeResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class PeriodContributionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PeriodContributionTypeRepository periodContributionTypeRepository;

    @Autowired
    private PeriodContributionTypeService periodContributionTypeService;

    @Autowired
    private PeriodContributionTypeQueryService periodContributionTypeQueryService;

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

    private MockMvc restPeriodContributionTypeMockMvc;

    private PeriodContributionType periodContributionType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PeriodContributionTypeResource periodContributionTypeResource = new PeriodContributionTypeResource(periodContributionTypeService, periodContributionTypeQueryService);
        this.restPeriodContributionTypeMockMvc = MockMvcBuilders.standaloneSetup(periodContributionTypeResource)
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
    public static PeriodContributionType createEntity(EntityManager em) {
        PeriodContributionType periodContributionType = new PeriodContributionType()
            .name(DEFAULT_NAME);
        // Add required entity
        PeriodType periodType;
        if (TestUtil.findAll(em, PeriodType.class).isEmpty()) {
            periodType = PeriodTypeResourceIT.createEntity(em);
            em.persist(periodType);
            em.flush();
        } else {
            periodType = TestUtil.findAll(em, PeriodType.class).get(0);
        }
        periodContributionType.setPeriodType(periodType);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        periodContributionType.setChurch(church);
        return periodContributionType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodContributionType createUpdatedEntity(EntityManager em) {
        PeriodContributionType periodContributionType = new PeriodContributionType()
            .name(UPDATED_NAME);
        // Add required entity
        PeriodType periodType;
        if (TestUtil.findAll(em, PeriodType.class).isEmpty()) {
            periodType = PeriodTypeResourceIT.createUpdatedEntity(em);
            em.persist(periodType);
            em.flush();
        } else {
            periodType = TestUtil.findAll(em, PeriodType.class).get(0);
        }
        periodContributionType.setPeriodType(periodType);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createUpdatedEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        periodContributionType.setChurch(church);
        return periodContributionType;
    }

    @BeforeEach
    public void initTest() {
        periodContributionType = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeriodContributionType() throws Exception {
        int databaseSizeBeforeCreate = periodContributionTypeRepository.findAll().size();

        // Create the PeriodContributionType
        restPeriodContributionTypeMockMvc.perform(post("/api/period-contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContributionType)))
            .andExpect(status().isCreated());

        // Validate the PeriodContributionType in the database
        List<PeriodContributionType> periodContributionTypeList = periodContributionTypeRepository.findAll();
        assertThat(periodContributionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PeriodContributionType testPeriodContributionType = periodContributionTypeList.get(periodContributionTypeList.size() - 1);
        assertThat(testPeriodContributionType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPeriodContributionTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = periodContributionTypeRepository.findAll().size();

        // Create the PeriodContributionType with an existing ID
        periodContributionType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodContributionTypeMockMvc.perform(post("/api/period-contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContributionType)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodContributionType in the database
        List<PeriodContributionType> periodContributionTypeList = periodContributionTypeRepository.findAll();
        assertThat(periodContributionTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodContributionTypeRepository.findAll().size();
        // set the field null
        periodContributionType.setName(null);

        // Create the PeriodContributionType, which fails.

        restPeriodContributionTypeMockMvc.perform(post("/api/period-contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContributionType)))
            .andExpect(status().isBadRequest());

        List<PeriodContributionType> periodContributionTypeList = periodContributionTypeRepository.findAll();
        assertThat(periodContributionTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionTypes() throws Exception {
        // Initialize the database
        periodContributionTypeRepository.saveAndFlush(periodContributionType);

        // Get all the periodContributionTypeList
        restPeriodContributionTypeMockMvc.perform(get("/api/period-contribution-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodContributionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getPeriodContributionType() throws Exception {
        // Initialize the database
        periodContributionTypeRepository.saveAndFlush(periodContributionType);

        // Get the periodContributionType
        restPeriodContributionTypeMockMvc.perform(get("/api/period-contribution-types/{id}", periodContributionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(periodContributionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getAllPeriodContributionTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionTypeRepository.saveAndFlush(periodContributionType);

        // Get all the periodContributionTypeList where name equals to DEFAULT_NAME
        defaultPeriodContributionTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the periodContributionTypeList where name equals to UPDATED_NAME
        defaultPeriodContributionTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionTypeRepository.saveAndFlush(periodContributionType);

        // Get all the periodContributionTypeList where name not equals to DEFAULT_NAME
        defaultPeriodContributionTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the periodContributionTypeList where name not equals to UPDATED_NAME
        defaultPeriodContributionTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        periodContributionTypeRepository.saveAndFlush(periodContributionType);

        // Get all the periodContributionTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPeriodContributionTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the periodContributionTypeList where name equals to UPDATED_NAME
        defaultPeriodContributionTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodContributionTypeRepository.saveAndFlush(periodContributionType);

        // Get all the periodContributionTypeList where name is not null
        defaultPeriodContributionTypeShouldBeFound("name.specified=true");

        // Get all the periodContributionTypeList where name is null
        defaultPeriodContributionTypeShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeriodContributionTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        periodContributionTypeRepository.saveAndFlush(periodContributionType);

        // Get all the periodContributionTypeList where name contains DEFAULT_NAME
        defaultPeriodContributionTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the periodContributionTypeList where name contains UPDATED_NAME
        defaultPeriodContributionTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        periodContributionTypeRepository.saveAndFlush(periodContributionType);

        // Get all the periodContributionTypeList where name does not contain DEFAULT_NAME
        defaultPeriodContributionTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the periodContributionTypeList where name does not contain UPDATED_NAME
        defaultPeriodContributionTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPeriodContributionTypesByPeriodTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        PeriodType periodType = periodContributionType.getPeriodType();
        periodContributionTypeRepository.saveAndFlush(periodContributionType);
        Long periodTypeId = periodType.getId();

        // Get all the periodContributionTypeList where periodType equals to periodTypeId
        defaultPeriodContributionTypeShouldBeFound("periodTypeId.equals=" + periodTypeId);

        // Get all the periodContributionTypeList where periodType equals to periodTypeId + 1
        defaultPeriodContributionTypeShouldNotBeFound("periodTypeId.equals=" + (periodTypeId + 1));
    }


    @Test
    @Transactional
    public void getAllPeriodContributionTypesByChurchIsEqualToSomething() throws Exception {
        // Get already existing entity
        Church church = periodContributionType.getChurch();
        periodContributionTypeRepository.saveAndFlush(periodContributionType);
        Long churchId = church.getId();

        // Get all the periodContributionTypeList where church equals to churchId
        defaultPeriodContributionTypeShouldBeFound("churchId.equals=" + churchId);

        // Get all the periodContributionTypeList where church equals to churchId + 1
        defaultPeriodContributionTypeShouldNotBeFound("churchId.equals=" + (churchId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPeriodContributionTypeShouldBeFound(String filter) throws Exception {
        restPeriodContributionTypeMockMvc.perform(get("/api/period-contribution-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodContributionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restPeriodContributionTypeMockMvc.perform(get("/api/period-contribution-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPeriodContributionTypeShouldNotBeFound(String filter) throws Exception {
        restPeriodContributionTypeMockMvc.perform(get("/api/period-contribution-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPeriodContributionTypeMockMvc.perform(get("/api/period-contribution-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPeriodContributionType() throws Exception {
        // Get the periodContributionType
        restPeriodContributionTypeMockMvc.perform(get("/api/period-contribution-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriodContributionType() throws Exception {
        // Initialize the database
        periodContributionTypeService.save(periodContributionType);

        int databaseSizeBeforeUpdate = periodContributionTypeRepository.findAll().size();

        // Update the periodContributionType
        PeriodContributionType updatedPeriodContributionType = periodContributionTypeRepository.findById(periodContributionType.getId()).get();
        // Disconnect from session so that the updates on updatedPeriodContributionType are not directly saved in db
        em.detach(updatedPeriodContributionType);
        updatedPeriodContributionType
            .name(UPDATED_NAME);

        restPeriodContributionTypeMockMvc.perform(put("/api/period-contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeriodContributionType)))
            .andExpect(status().isOk());

        // Validate the PeriodContributionType in the database
        List<PeriodContributionType> periodContributionTypeList = periodContributionTypeRepository.findAll();
        assertThat(periodContributionTypeList).hasSize(databaseSizeBeforeUpdate);
        PeriodContributionType testPeriodContributionType = periodContributionTypeList.get(periodContributionTypeList.size() - 1);
        assertThat(testPeriodContributionType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPeriodContributionType() throws Exception {
        int databaseSizeBeforeUpdate = periodContributionTypeRepository.findAll().size();

        // Create the PeriodContributionType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodContributionTypeMockMvc.perform(put("/api/period-contribution-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContributionType)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodContributionType in the database
        List<PeriodContributionType> periodContributionTypeList = periodContributionTypeRepository.findAll();
        assertThat(periodContributionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePeriodContributionType() throws Exception {
        // Initialize the database
        periodContributionTypeService.save(periodContributionType);

        int databaseSizeBeforeDelete = periodContributionTypeRepository.findAll().size();

        // Delete the periodContributionType
        restPeriodContributionTypeMockMvc.perform(delete("/api/period-contribution-types/{id}", periodContributionType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PeriodContributionType> periodContributionTypeList = periodContributionTypeRepository.findAll();
        assertThat(periodContributionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodContributionType.class);
        PeriodContributionType periodContributionType1 = new PeriodContributionType();
        periodContributionType1.setId(1L);
        PeriodContributionType periodContributionType2 = new PeriodContributionType();
        periodContributionType2.setId(periodContributionType1.getId());
        assertThat(periodContributionType1).isEqualTo(periodContributionType2);
        periodContributionType2.setId(2L);
        assertThat(periodContributionType1).isNotEqualTo(periodContributionType2);
        periodContributionType1.setId(null);
        assertThat(periodContributionType1).isNotEqualTo(periodContributionType2);
    }
}
