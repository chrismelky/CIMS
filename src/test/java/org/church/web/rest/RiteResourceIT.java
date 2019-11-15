package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.Rite;
import org.church.repository.RiteRepository;
import org.church.service.RiteService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.RiteCriteria;
import org.church.service.RiteQueryService;

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
 * Integration tests for the {@link RiteResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class RiteResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private RiteRepository riteRepository;

    @Autowired
    private RiteService riteService;

    @Autowired
    private RiteQueryService riteQueryService;

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

    private MockMvc restRiteMockMvc;

    private Rite rite;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RiteResource riteResource = new RiteResource(riteService, riteQueryService);
        this.restRiteMockMvc = MockMvcBuilders.standaloneSetup(riteResource)
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
    public static Rite createEntity(EntityManager em) {
        Rite rite = new Rite()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME);
        return rite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rite createUpdatedEntity(EntityManager em) {
        Rite rite = new Rite()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME);
        return rite;
    }

    @BeforeEach
    public void initTest() {
        rite = createEntity(em);
    }

    @Test
    @Transactional
    public void createRite() throws Exception {
        int databaseSizeBeforeCreate = riteRepository.findAll().size();

        // Create the Rite
        restRiteMockMvc.perform(post("/api/rites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rite)))
            .andExpect(status().isCreated());

        // Validate the Rite in the database
        List<Rite> riteList = riteRepository.findAll();
        assertThat(riteList).hasSize(databaseSizeBeforeCreate + 1);
        Rite testRite = riteList.get(riteList.size() - 1);
        assertThat(testRite.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRite.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createRiteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = riteRepository.findAll().size();

        // Create the Rite with an existing ID
        rite.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRiteMockMvc.perform(post("/api/rites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rite)))
            .andExpect(status().isBadRequest());

        // Validate the Rite in the database
        List<Rite> riteList = riteRepository.findAll();
        assertThat(riteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = riteRepository.findAll().size();
        // set the field null
        rite.setName(null);

        // Create the Rite, which fails.

        restRiteMockMvc.perform(post("/api/rites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rite)))
            .andExpect(status().isBadRequest());

        List<Rite> riteList = riteRepository.findAll();
        assertThat(riteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRites() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList
        restRiteMockMvc.perform(get("/api/rites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rite.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getRite() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get the rite
        restRiteMockMvc.perform(get("/api/rites/{id}", rite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rite.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getAllRitesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where code equals to DEFAULT_CODE
        defaultRiteShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the riteList where code equals to UPDATED_CODE
        defaultRiteShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllRitesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where code not equals to DEFAULT_CODE
        defaultRiteShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the riteList where code not equals to UPDATED_CODE
        defaultRiteShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllRitesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where code in DEFAULT_CODE or UPDATED_CODE
        defaultRiteShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the riteList where code equals to UPDATED_CODE
        defaultRiteShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllRitesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where code is not null
        defaultRiteShouldBeFound("code.specified=true");

        // Get all the riteList where code is null
        defaultRiteShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllRitesByCodeContainsSomething() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where code contains DEFAULT_CODE
        defaultRiteShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the riteList where code contains UPDATED_CODE
        defaultRiteShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllRitesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where code does not contain DEFAULT_CODE
        defaultRiteShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the riteList where code does not contain UPDATED_CODE
        defaultRiteShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllRitesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where name equals to DEFAULT_NAME
        defaultRiteShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the riteList where name equals to UPDATED_NAME
        defaultRiteShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRitesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where name not equals to DEFAULT_NAME
        defaultRiteShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the riteList where name not equals to UPDATED_NAME
        defaultRiteShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRitesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRiteShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the riteList where name equals to UPDATED_NAME
        defaultRiteShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRitesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where name is not null
        defaultRiteShouldBeFound("name.specified=true");

        // Get all the riteList where name is null
        defaultRiteShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllRitesByNameContainsSomething() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where name contains DEFAULT_NAME
        defaultRiteShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the riteList where name contains UPDATED_NAME
        defaultRiteShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRitesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        riteRepository.saveAndFlush(rite);

        // Get all the riteList where name does not contain DEFAULT_NAME
        defaultRiteShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the riteList where name does not contain UPDATED_NAME
        defaultRiteShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRiteShouldBeFound(String filter) throws Exception {
        restRiteMockMvc.perform(get("/api/rites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rite.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restRiteMockMvc.perform(get("/api/rites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRiteShouldNotBeFound(String filter) throws Exception {
        restRiteMockMvc.perform(get("/api/rites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRiteMockMvc.perform(get("/api/rites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRite() throws Exception {
        // Get the rite
        restRiteMockMvc.perform(get("/api/rites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRite() throws Exception {
        // Initialize the database
        riteService.save(rite);

        int databaseSizeBeforeUpdate = riteRepository.findAll().size();

        // Update the rite
        Rite updatedRite = riteRepository.findById(rite.getId()).get();
        // Disconnect from session so that the updates on updatedRite are not directly saved in db
        em.detach(updatedRite);
        updatedRite
            .code(UPDATED_CODE)
            .name(UPDATED_NAME);

        restRiteMockMvc.perform(put("/api/rites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRite)))
            .andExpect(status().isOk());

        // Validate the Rite in the database
        List<Rite> riteList = riteRepository.findAll();
        assertThat(riteList).hasSize(databaseSizeBeforeUpdate);
        Rite testRite = riteList.get(riteList.size() - 1);
        assertThat(testRite.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRite.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRite() throws Exception {
        int databaseSizeBeforeUpdate = riteRepository.findAll().size();

        // Create the Rite

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRiteMockMvc.perform(put("/api/rites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rite)))
            .andExpect(status().isBadRequest());

        // Validate the Rite in the database
        List<Rite> riteList = riteRepository.findAll();
        assertThat(riteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRite() throws Exception {
        // Initialize the database
        riteService.save(rite);

        int databaseSizeBeforeDelete = riteRepository.findAll().size();

        // Delete the rite
        restRiteMockMvc.perform(delete("/api/rites/{id}", rite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rite> riteList = riteRepository.findAll();
        assertThat(riteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rite.class);
        Rite rite1 = new Rite();
        rite1.setId(1L);
        Rite rite2 = new Rite();
        rite2.setId(rite1.getId());
        assertThat(rite1).isEqualTo(rite2);
        rite2.setId(2L);
        assertThat(rite1).isNotEqualTo(rite2);
        rite1.setId(null);
        assertThat(rite1).isNotEqualTo(rite2);
    }
}
