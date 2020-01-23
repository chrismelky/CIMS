package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.PeriodType;
import org.church.repository.PeriodTypeRepository;
import org.church.service.PeriodTypeService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.PeriodTypeCriteria;
import org.church.service.PeriodTypeQueryService;

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
 * Integration tests for the {@link PeriodTypeResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class PeriodTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PeriodTypeRepository periodTypeRepository;

    @Autowired
    private PeriodTypeService periodTypeService;

    @Autowired
    private PeriodTypeQueryService periodTypeQueryService;

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

    private MockMvc restPeriodTypeMockMvc;

    private PeriodType periodType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PeriodTypeResource periodTypeResource = new PeriodTypeResource(periodTypeService, periodTypeQueryService);
        this.restPeriodTypeMockMvc = MockMvcBuilders.standaloneSetup(periodTypeResource)
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
    public static PeriodType createEntity(EntityManager em) {
        PeriodType periodType = new PeriodType()
            .name(DEFAULT_NAME);
        return periodType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodType createUpdatedEntity(EntityManager em) {
        PeriodType periodType = new PeriodType()
            .name(UPDATED_NAME);
        return periodType;
    }

    @BeforeEach
    public void initTest() {
        periodType = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeriodType() throws Exception {
        int databaseSizeBeforeCreate = periodTypeRepository.findAll().size();

        // Create the PeriodType
        restPeriodTypeMockMvc.perform(post("/api/period-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodType)))
            .andExpect(status().isCreated());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PeriodType testPeriodType = periodTypeList.get(periodTypeList.size() - 1);
        assertThat(testPeriodType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPeriodTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = periodTypeRepository.findAll().size();

        // Create the PeriodType with an existing ID
        periodType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodTypeMockMvc.perform(post("/api/period-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodType)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodTypeRepository.findAll().size();
        // set the field null
        periodType.setName(null);

        // Create the PeriodType, which fails.

        restPeriodTypeMockMvc.perform(post("/api/period-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodType)))
            .andExpect(status().isBadRequest());

        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeriodTypes() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get all the periodTypeList
        restPeriodTypeMockMvc.perform(get("/api/period-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getPeriodType() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get the periodType
        restPeriodTypeMockMvc.perform(get("/api/period-types/{id}", periodType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(periodType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getAllPeriodTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get all the periodTypeList where name equals to DEFAULT_NAME
        defaultPeriodTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the periodTypeList where name equals to UPDATED_NAME
        defaultPeriodTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get all the periodTypeList where name not equals to DEFAULT_NAME
        defaultPeriodTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the periodTypeList where name not equals to UPDATED_NAME
        defaultPeriodTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get all the periodTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPeriodTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the periodTypeList where name equals to UPDATED_NAME
        defaultPeriodTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get all the periodTypeList where name is not null
        defaultPeriodTypeShouldBeFound("name.specified=true");

        // Get all the periodTypeList where name is null
        defaultPeriodTypeShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeriodTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get all the periodTypeList where name contains DEFAULT_NAME
        defaultPeriodTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the periodTypeList where name contains UPDATED_NAME
        defaultPeriodTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        periodTypeRepository.saveAndFlush(periodType);

        // Get all the periodTypeList where name does not contain DEFAULT_NAME
        defaultPeriodTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the periodTypeList where name does not contain UPDATED_NAME
        defaultPeriodTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPeriodTypeShouldBeFound(String filter) throws Exception {
        restPeriodTypeMockMvc.perform(get("/api/period-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restPeriodTypeMockMvc.perform(get("/api/period-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPeriodTypeShouldNotBeFound(String filter) throws Exception {
        restPeriodTypeMockMvc.perform(get("/api/period-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPeriodTypeMockMvc.perform(get("/api/period-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPeriodType() throws Exception {
        // Get the periodType
        restPeriodTypeMockMvc.perform(get("/api/period-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriodType() throws Exception {
        // Initialize the database
        periodTypeService.save(periodType);

        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();

        // Update the periodType
        PeriodType updatedPeriodType = periodTypeRepository.findById(periodType.getId()).get();
        // Disconnect from session so that the updates on updatedPeriodType are not directly saved in db
        em.detach(updatedPeriodType);
        updatedPeriodType
            .name(UPDATED_NAME);

        restPeriodTypeMockMvc.perform(put("/api/period-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeriodType)))
            .andExpect(status().isOk());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
        PeriodType testPeriodType = periodTypeList.get(periodTypeList.size() - 1);
        assertThat(testPeriodType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPeriodType() throws Exception {
        int databaseSizeBeforeUpdate = periodTypeRepository.findAll().size();

        // Create the PeriodType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodTypeMockMvc.perform(put("/api/period-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodType)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodType in the database
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePeriodType() throws Exception {
        // Initialize the database
        periodTypeService.save(periodType);

        int databaseSizeBeforeDelete = periodTypeRepository.findAll().size();

        // Delete the periodType
        restPeriodTypeMockMvc.perform(delete("/api/period-types/{id}", periodType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PeriodType> periodTypeList = periodTypeRepository.findAll();
        assertThat(periodTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodType.class);
        PeriodType periodType1 = new PeriodType();
        periodType1.setId(1L);
        PeriodType periodType2 = new PeriodType();
        periodType2.setId(periodType1.getId());
        assertThat(periodType1).isEqualTo(periodType2);
        periodType2.setId(2L);
        assertThat(periodType1).isNotEqualTo(periodType2);
        periodType1.setId(null);
        assertThat(periodType1).isNotEqualTo(periodType2);
    }
}
