package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.ChuchService;
import org.church.domain.Church;
import org.church.repository.ChuchServiceRepository;
import org.church.service.ChuchServiceService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.ChuchServiceCriteria;
import org.church.service.ChuchServiceQueryService;

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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.church.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.church.domain.enumeration.WeekDay;
/**
 * Integration tests for the {@link ChuchServiceResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class ChuchServiceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final WeekDay DEFAULT_DAY = WeekDay.Monday;
    private static final WeekDay UPDATED_DAY = WeekDay.Tuesday;

    private static final String DEFAULT_START_TIME = "AAAAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBBBB";

    @Autowired
    private ChuchServiceRepository chuchServiceRepository;

    @Autowired
    private ChuchServiceService chuchServiceService;

    @Autowired
    private ChuchServiceQueryService chuchServiceQueryService;

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

    private MockMvc restChuchServiceMockMvc;

    private ChuchService chuchService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChuchServiceResource chuchServiceResource = new ChuchServiceResource(chuchServiceService, chuchServiceQueryService);
        this.restChuchServiceMockMvc = MockMvcBuilders.standaloneSetup(chuchServiceResource)
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
    public static ChuchService createEntity(EntityManager em) {
        ChuchService chuchService = new ChuchService()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .day(DEFAULT_DAY)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        chuchService.setChurch(church);
        return chuchService;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChuchService createUpdatedEntity(EntityManager em) {
        ChuchService chuchService = new ChuchService()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .day(UPDATED_DAY)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createUpdatedEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        chuchService.setChurch(church);
        return chuchService;
    }

    @BeforeEach
    public void initTest() {
        chuchService = createEntity(em);
    }

    @Test
    @Transactional
    public void createChuchService() throws Exception {
        int databaseSizeBeforeCreate = chuchServiceRepository.findAll().size();

        // Create the ChuchService
        restChuchServiceMockMvc.perform(post("/api/chuch-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chuchService)))
            .andExpect(status().isCreated());

        // Validate the ChuchService in the database
        List<ChuchService> chuchServiceList = chuchServiceRepository.findAll();
        assertThat(chuchServiceList).hasSize(databaseSizeBeforeCreate + 1);
        ChuchService testChuchService = chuchServiceList.get(chuchServiceList.size() - 1);
        assertThat(testChuchService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChuchService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChuchService.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testChuchService.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testChuchService.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    public void createChuchServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chuchServiceRepository.findAll().size();

        // Create the ChuchService with an existing ID
        chuchService.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChuchServiceMockMvc.perform(post("/api/chuch-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chuchService)))
            .andExpect(status().isBadRequest());

        // Validate the ChuchService in the database
        List<ChuchService> chuchServiceList = chuchServiceRepository.findAll();
        assertThat(chuchServiceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = chuchServiceRepository.findAll().size();
        // set the field null
        chuchService.setName(null);

        // Create the ChuchService, which fails.

        restChuchServiceMockMvc.perform(post("/api/chuch-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chuchService)))
            .andExpect(status().isBadRequest());

        List<ChuchService> chuchServiceList = chuchServiceRepository.findAll();
        assertThat(chuchServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChuchServices() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList
        restChuchServiceMockMvc.perform(get("/api/chuch-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chuchService.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)));
    }
    
    @Test
    @Transactional
    public void getChuchService() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get the chuchService
        restChuchServiceMockMvc.perform(get("/api/chuch-services/{id}", chuchService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chuchService.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME));
    }

    @Test
    @Transactional
    public void getAllChuchServicesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where name equals to DEFAULT_NAME
        defaultChuchServiceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the chuchServiceList where name equals to UPDATED_NAME
        defaultChuchServiceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where name not equals to DEFAULT_NAME
        defaultChuchServiceShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the chuchServiceList where name not equals to UPDATED_NAME
        defaultChuchServiceShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultChuchServiceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the chuchServiceList where name equals to UPDATED_NAME
        defaultChuchServiceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where name is not null
        defaultChuchServiceShouldBeFound("name.specified=true");

        // Get all the chuchServiceList where name is null
        defaultChuchServiceShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllChuchServicesByNameContainsSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where name contains DEFAULT_NAME
        defaultChuchServiceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the chuchServiceList where name contains UPDATED_NAME
        defaultChuchServiceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where name does not contain DEFAULT_NAME
        defaultChuchServiceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the chuchServiceList where name does not contain UPDATED_NAME
        defaultChuchServiceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllChuchServicesByDayIsEqualToSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where day equals to DEFAULT_DAY
        defaultChuchServiceShouldBeFound("day.equals=" + DEFAULT_DAY);

        // Get all the chuchServiceList where day equals to UPDATED_DAY
        defaultChuchServiceShouldNotBeFound("day.equals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where day not equals to DEFAULT_DAY
        defaultChuchServiceShouldNotBeFound("day.notEquals=" + DEFAULT_DAY);

        // Get all the chuchServiceList where day not equals to UPDATED_DAY
        defaultChuchServiceShouldBeFound("day.notEquals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByDayIsInShouldWork() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where day in DEFAULT_DAY or UPDATED_DAY
        defaultChuchServiceShouldBeFound("day.in=" + DEFAULT_DAY + "," + UPDATED_DAY);

        // Get all the chuchServiceList where day equals to UPDATED_DAY
        defaultChuchServiceShouldNotBeFound("day.in=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where day is not null
        defaultChuchServiceShouldBeFound("day.specified=true");

        // Get all the chuchServiceList where day is null
        defaultChuchServiceShouldNotBeFound("day.specified=false");
    }

    @Test
    @Transactional
    public void getAllChuchServicesByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where startTime equals to DEFAULT_START_TIME
        defaultChuchServiceShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the chuchServiceList where startTime equals to UPDATED_START_TIME
        defaultChuchServiceShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByStartTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where startTime not equals to DEFAULT_START_TIME
        defaultChuchServiceShouldNotBeFound("startTime.notEquals=" + DEFAULT_START_TIME);

        // Get all the chuchServiceList where startTime not equals to UPDATED_START_TIME
        defaultChuchServiceShouldBeFound("startTime.notEquals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultChuchServiceShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the chuchServiceList where startTime equals to UPDATED_START_TIME
        defaultChuchServiceShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where startTime is not null
        defaultChuchServiceShouldBeFound("startTime.specified=true");

        // Get all the chuchServiceList where startTime is null
        defaultChuchServiceShouldNotBeFound("startTime.specified=false");
    }
                @Test
    @Transactional
    public void getAllChuchServicesByStartTimeContainsSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where startTime contains DEFAULT_START_TIME
        defaultChuchServiceShouldBeFound("startTime.contains=" + DEFAULT_START_TIME);

        // Get all the chuchServiceList where startTime contains UPDATED_START_TIME
        defaultChuchServiceShouldNotBeFound("startTime.contains=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByStartTimeNotContainsSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where startTime does not contain DEFAULT_START_TIME
        defaultChuchServiceShouldNotBeFound("startTime.doesNotContain=" + DEFAULT_START_TIME);

        // Get all the chuchServiceList where startTime does not contain UPDATED_START_TIME
        defaultChuchServiceShouldBeFound("startTime.doesNotContain=" + UPDATED_START_TIME);
    }


    @Test
    @Transactional
    public void getAllChuchServicesByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where endTime equals to DEFAULT_END_TIME
        defaultChuchServiceShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the chuchServiceList where endTime equals to UPDATED_END_TIME
        defaultChuchServiceShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByEndTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where endTime not equals to DEFAULT_END_TIME
        defaultChuchServiceShouldNotBeFound("endTime.notEquals=" + DEFAULT_END_TIME);

        // Get all the chuchServiceList where endTime not equals to UPDATED_END_TIME
        defaultChuchServiceShouldBeFound("endTime.notEquals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultChuchServiceShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the chuchServiceList where endTime equals to UPDATED_END_TIME
        defaultChuchServiceShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where endTime is not null
        defaultChuchServiceShouldBeFound("endTime.specified=true");

        // Get all the chuchServiceList where endTime is null
        defaultChuchServiceShouldNotBeFound("endTime.specified=false");
    }
                @Test
    @Transactional
    public void getAllChuchServicesByEndTimeContainsSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where endTime contains DEFAULT_END_TIME
        defaultChuchServiceShouldBeFound("endTime.contains=" + DEFAULT_END_TIME);

        // Get all the chuchServiceList where endTime contains UPDATED_END_TIME
        defaultChuchServiceShouldNotBeFound("endTime.contains=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllChuchServicesByEndTimeNotContainsSomething() throws Exception {
        // Initialize the database
        chuchServiceRepository.saveAndFlush(chuchService);

        // Get all the chuchServiceList where endTime does not contain DEFAULT_END_TIME
        defaultChuchServiceShouldNotBeFound("endTime.doesNotContain=" + DEFAULT_END_TIME);

        // Get all the chuchServiceList where endTime does not contain UPDATED_END_TIME
        defaultChuchServiceShouldBeFound("endTime.doesNotContain=" + UPDATED_END_TIME);
    }


    @Test
    @Transactional
    public void getAllChuchServicesByChurchIsEqualToSomething() throws Exception {
        // Get already existing entity
        Church church = chuchService.getChurch();
        chuchServiceRepository.saveAndFlush(chuchService);
        Long churchId = church.getId();

        // Get all the chuchServiceList where church equals to churchId
        defaultChuchServiceShouldBeFound("churchId.equals=" + churchId);

        // Get all the chuchServiceList where church equals to churchId + 1
        defaultChuchServiceShouldNotBeFound("churchId.equals=" + (churchId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChuchServiceShouldBeFound(String filter) throws Exception {
        restChuchServiceMockMvc.perform(get("/api/chuch-services?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chuchService.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)));

        // Check, that the count call also returns 1
        restChuchServiceMockMvc.perform(get("/api/chuch-services/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChuchServiceShouldNotBeFound(String filter) throws Exception {
        restChuchServiceMockMvc.perform(get("/api/chuch-services?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChuchServiceMockMvc.perform(get("/api/chuch-services/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChuchService() throws Exception {
        // Get the chuchService
        restChuchServiceMockMvc.perform(get("/api/chuch-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChuchService() throws Exception {
        // Initialize the database
        chuchServiceService.save(chuchService);

        int databaseSizeBeforeUpdate = chuchServiceRepository.findAll().size();

        // Update the chuchService
        ChuchService updatedChuchService = chuchServiceRepository.findById(chuchService.getId()).get();
        // Disconnect from session so that the updates on updatedChuchService are not directly saved in db
        em.detach(updatedChuchService);
        updatedChuchService
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .day(UPDATED_DAY)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);

        restChuchServiceMockMvc.perform(put("/api/chuch-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChuchService)))
            .andExpect(status().isOk());

        // Validate the ChuchService in the database
        List<ChuchService> chuchServiceList = chuchServiceRepository.findAll();
        assertThat(chuchServiceList).hasSize(databaseSizeBeforeUpdate);
        ChuchService testChuchService = chuchServiceList.get(chuchServiceList.size() - 1);
        assertThat(testChuchService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChuchService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChuchService.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testChuchService.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testChuchService.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingChuchService() throws Exception {
        int databaseSizeBeforeUpdate = chuchServiceRepository.findAll().size();

        // Create the ChuchService

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChuchServiceMockMvc.perform(put("/api/chuch-services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chuchService)))
            .andExpect(status().isBadRequest());

        // Validate the ChuchService in the database
        List<ChuchService> chuchServiceList = chuchServiceRepository.findAll();
        assertThat(chuchServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChuchService() throws Exception {
        // Initialize the database
        chuchServiceService.save(chuchService);

        int databaseSizeBeforeDelete = chuchServiceRepository.findAll().size();

        // Delete the chuchService
        restChuchServiceMockMvc.perform(delete("/api/chuch-services/{id}", chuchService.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChuchService> chuchServiceList = chuchServiceRepository.findAll();
        assertThat(chuchServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChuchService.class);
        ChuchService chuchService1 = new ChuchService();
        chuchService1.setId(1L);
        ChuchService chuchService2 = new ChuchService();
        chuchService2.setId(chuchService1.getId());
        assertThat(chuchService1).isEqualTo(chuchService2);
        chuchService2.setId(2L);
        assertThat(chuchService1).isNotEqualTo(chuchService2);
        chuchService1.setId(null);
        assertThat(chuchService1).isNotEqualTo(chuchService2);
    }
}
