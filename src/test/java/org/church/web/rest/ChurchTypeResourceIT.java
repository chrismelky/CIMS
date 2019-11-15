package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.ChurchType;
import org.church.repository.ChurchTypeRepository;
import org.church.service.ChurchTypeService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.ChurchTypeCriteria;
import org.church.service.ChurchTypeQueryService;

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
 * Integration tests for the {@link ChurchTypeResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class ChurchTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ChurchTypeRepository churchTypeRepository;

    @Autowired
    private ChurchTypeService churchTypeService;

    @Autowired
    private ChurchTypeQueryService churchTypeQueryService;

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

    private MockMvc restChurchTypeMockMvc;

    private ChurchType churchType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChurchTypeResource churchTypeResource = new ChurchTypeResource(churchTypeService, churchTypeQueryService);
        this.restChurchTypeMockMvc = MockMvcBuilders.standaloneSetup(churchTypeResource)
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
    public static ChurchType createEntity(EntityManager em) {
        ChurchType churchType = new ChurchType()
            .name(DEFAULT_NAME);
        return churchType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChurchType createUpdatedEntity(EntityManager em) {
        ChurchType churchType = new ChurchType()
            .name(UPDATED_NAME);
        return churchType;
    }

    @BeforeEach
    public void initTest() {
        churchType = createEntity(em);
    }

    @Test
    @Transactional
    public void createChurchType() throws Exception {
        int databaseSizeBeforeCreate = churchTypeRepository.findAll().size();

        // Create the ChurchType
        restChurchTypeMockMvc.perform(post("/api/church-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchType)))
            .andExpect(status().isCreated());

        // Validate the ChurchType in the database
        List<ChurchType> churchTypeList = churchTypeRepository.findAll();
        assertThat(churchTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ChurchType testChurchType = churchTypeList.get(churchTypeList.size() - 1);
        assertThat(testChurchType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createChurchTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = churchTypeRepository.findAll().size();

        // Create the ChurchType with an existing ID
        churchType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChurchTypeMockMvc.perform(post("/api/church-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchType)))
            .andExpect(status().isBadRequest());

        // Validate the ChurchType in the database
        List<ChurchType> churchTypeList = churchTypeRepository.findAll();
        assertThat(churchTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchTypeRepository.findAll().size();
        // set the field null
        churchType.setName(null);

        // Create the ChurchType, which fails.

        restChurchTypeMockMvc.perform(post("/api/church-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchType)))
            .andExpect(status().isBadRequest());

        List<ChurchType> churchTypeList = churchTypeRepository.findAll();
        assertThat(churchTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChurchTypes() throws Exception {
        // Initialize the database
        churchTypeRepository.saveAndFlush(churchType);

        // Get all the churchTypeList
        restChurchTypeMockMvc.perform(get("/api/church-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churchType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getChurchType() throws Exception {
        // Initialize the database
        churchTypeRepository.saveAndFlush(churchType);

        // Get the churchType
        restChurchTypeMockMvc.perform(get("/api/church-types/{id}", churchType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(churchType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getAllChurchTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        churchTypeRepository.saveAndFlush(churchType);

        // Get all the churchTypeList where name equals to DEFAULT_NAME
        defaultChurchTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the churchTypeList where name equals to UPDATED_NAME
        defaultChurchTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchTypeRepository.saveAndFlush(churchType);

        // Get all the churchTypeList where name not equals to DEFAULT_NAME
        defaultChurchTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the churchTypeList where name not equals to UPDATED_NAME
        defaultChurchTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        churchTypeRepository.saveAndFlush(churchType);

        // Get all the churchTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultChurchTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the churchTypeList where name equals to UPDATED_NAME
        defaultChurchTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchTypeRepository.saveAndFlush(churchType);

        // Get all the churchTypeList where name is not null
        defaultChurchTypeShouldBeFound("name.specified=true");

        // Get all the churchTypeList where name is null
        defaultChurchTypeShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllChurchTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        churchTypeRepository.saveAndFlush(churchType);

        // Get all the churchTypeList where name contains DEFAULT_NAME
        defaultChurchTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the churchTypeList where name contains UPDATED_NAME
        defaultChurchTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        churchTypeRepository.saveAndFlush(churchType);

        // Get all the churchTypeList where name does not contain DEFAULT_NAME
        defaultChurchTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the churchTypeList where name does not contain UPDATED_NAME
        defaultChurchTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChurchTypeShouldBeFound(String filter) throws Exception {
        restChurchTypeMockMvc.perform(get("/api/church-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churchType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restChurchTypeMockMvc.perform(get("/api/church-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChurchTypeShouldNotBeFound(String filter) throws Exception {
        restChurchTypeMockMvc.perform(get("/api/church-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChurchTypeMockMvc.perform(get("/api/church-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChurchType() throws Exception {
        // Get the churchType
        restChurchTypeMockMvc.perform(get("/api/church-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChurchType() throws Exception {
        // Initialize the database
        churchTypeService.save(churchType);

        int databaseSizeBeforeUpdate = churchTypeRepository.findAll().size();

        // Update the churchType
        ChurchType updatedChurchType = churchTypeRepository.findById(churchType.getId()).get();
        // Disconnect from session so that the updates on updatedChurchType are not directly saved in db
        em.detach(updatedChurchType);
        updatedChurchType
            .name(UPDATED_NAME);

        restChurchTypeMockMvc.perform(put("/api/church-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChurchType)))
            .andExpect(status().isOk());

        // Validate the ChurchType in the database
        List<ChurchType> churchTypeList = churchTypeRepository.findAll();
        assertThat(churchTypeList).hasSize(databaseSizeBeforeUpdate);
        ChurchType testChurchType = churchTypeList.get(churchTypeList.size() - 1);
        assertThat(testChurchType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingChurchType() throws Exception {
        int databaseSizeBeforeUpdate = churchTypeRepository.findAll().size();

        // Create the ChurchType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchTypeMockMvc.perform(put("/api/church-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchType)))
            .andExpect(status().isBadRequest());

        // Validate the ChurchType in the database
        List<ChurchType> churchTypeList = churchTypeRepository.findAll();
        assertThat(churchTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChurchType() throws Exception {
        // Initialize the database
        churchTypeService.save(churchType);

        int databaseSizeBeforeDelete = churchTypeRepository.findAll().size();

        // Delete the churchType
        restChurchTypeMockMvc.perform(delete("/api/church-types/{id}", churchType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChurchType> churchTypeList = churchTypeRepository.findAll();
        assertThat(churchTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChurchType.class);
        ChurchType churchType1 = new ChurchType();
        churchType1.setId(1L);
        ChurchType churchType2 = new ChurchType();
        churchType2.setId(churchType1.getId());
        assertThat(churchType1).isEqualTo(churchType2);
        churchType2.setId(2L);
        assertThat(churchType1).isNotEqualTo(churchType2);
        churchType1.setId(null);
        assertThat(churchType1).isNotEqualTo(churchType2);
    }
}
