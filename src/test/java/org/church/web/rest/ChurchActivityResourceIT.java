package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.ChurchActivity;
import org.church.domain.Church;
import org.church.repository.ChurchActivityRepository;
import org.church.service.ChurchActivityService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.ChurchActivityCriteria;
import org.church.service.ChurchActivityQueryService;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.church.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChurchActivityResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class ChurchActivityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ESTAMATE_BUDGET = new BigDecimal(1);
    private static final BigDecimal UPDATED_ESTAMATE_BUDGET = new BigDecimal(2);
    private static final BigDecimal SMALLER_ESTAMATE_BUDGET = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private ChurchActivityRepository churchActivityRepository;

    @Autowired
    private ChurchActivityService churchActivityService;

    @Autowired
    private ChurchActivityQueryService churchActivityQueryService;

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

    private MockMvc restChurchActivityMockMvc;

    private ChurchActivity churchActivity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChurchActivityResource churchActivityResource = new ChurchActivityResource(churchActivityService, churchActivityQueryService);
        this.restChurchActivityMockMvc = MockMvcBuilders.standaloneSetup(churchActivityResource)
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
    public static ChurchActivity createEntity(EntityManager em) {
        ChurchActivity churchActivity = new ChurchActivity()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .estamateBudget(DEFAULT_ESTAMATE_BUDGET)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        churchActivity.setChurch(church);
        return churchActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChurchActivity createUpdatedEntity(EntityManager em) {
        ChurchActivity churchActivity = new ChurchActivity()
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .estamateBudget(UPDATED_ESTAMATE_BUDGET)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createUpdatedEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        churchActivity.setChurch(church);
        return churchActivity;
    }

    @BeforeEach
    public void initTest() {
        churchActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createChurchActivity() throws Exception {
        int databaseSizeBeforeCreate = churchActivityRepository.findAll().size();

        // Create the ChurchActivity
        restChurchActivityMockMvc.perform(post("/api/church-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchActivity)))
            .andExpect(status().isCreated());

        // Validate the ChurchActivity in the database
        List<ChurchActivity> churchActivityList = churchActivityRepository.findAll();
        assertThat(churchActivityList).hasSize(databaseSizeBeforeCreate + 1);
        ChurchActivity testChurchActivity = churchActivityList.get(churchActivityList.size() - 1);
        assertThat(testChurchActivity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChurchActivity.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testChurchActivity.getEstamateBudget()).isEqualTo(DEFAULT_ESTAMATE_BUDGET);
        assertThat(testChurchActivity.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testChurchActivity.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createChurchActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = churchActivityRepository.findAll().size();

        // Create the ChurchActivity with an existing ID
        churchActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChurchActivityMockMvc.perform(post("/api/church-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchActivity)))
            .andExpect(status().isBadRequest());

        // Validate the ChurchActivity in the database
        List<ChurchActivity> churchActivityList = churchActivityRepository.findAll();
        assertThat(churchActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchActivityRepository.findAll().size();
        // set the field null
        churchActivity.setName(null);

        // Create the ChurchActivity, which fails.

        restChurchActivityMockMvc.perform(post("/api/church-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchActivity)))
            .andExpect(status().isBadRequest());

        List<ChurchActivity> churchActivityList = churchActivityRepository.findAll();
        assertThat(churchActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchActivityRepository.findAll().size();
        // set the field null
        churchActivity.setStartDate(null);

        // Create the ChurchActivity, which fails.

        restChurchActivityMockMvc.perform(post("/api/church-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchActivity)))
            .andExpect(status().isBadRequest());

        List<ChurchActivity> churchActivityList = churchActivityRepository.findAll();
        assertThat(churchActivityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChurchActivities() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList
        restChurchActivityMockMvc.perform(get("/api/church-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churchActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].estamateBudget").value(hasItem(DEFAULT_ESTAMATE_BUDGET.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getChurchActivity() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get the churchActivity
        restChurchActivityMockMvc.perform(get("/api/church-activities/{id}", churchActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(churchActivity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.estamateBudget").value(DEFAULT_ESTAMATE_BUDGET.intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where name equals to DEFAULT_NAME
        defaultChurchActivityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the churchActivityList where name equals to UPDATED_NAME
        defaultChurchActivityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where name not equals to DEFAULT_NAME
        defaultChurchActivityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the churchActivityList where name not equals to UPDATED_NAME
        defaultChurchActivityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultChurchActivityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the churchActivityList where name equals to UPDATED_NAME
        defaultChurchActivityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where name is not null
        defaultChurchActivityShouldBeFound("name.specified=true");

        // Get all the churchActivityList where name is null
        defaultChurchActivityShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllChurchActivitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where name contains DEFAULT_NAME
        defaultChurchActivityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the churchActivityList where name contains UPDATED_NAME
        defaultChurchActivityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where name does not contain DEFAULT_NAME
        defaultChurchActivityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the churchActivityList where name does not contain UPDATED_NAME
        defaultChurchActivityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllChurchActivitiesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where location equals to DEFAULT_LOCATION
        defaultChurchActivityShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the churchActivityList where location equals to UPDATED_LOCATION
        defaultChurchActivityShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where location not equals to DEFAULT_LOCATION
        defaultChurchActivityShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the churchActivityList where location not equals to UPDATED_LOCATION
        defaultChurchActivityShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultChurchActivityShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the churchActivityList where location equals to UPDATED_LOCATION
        defaultChurchActivityShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where location is not null
        defaultChurchActivityShouldBeFound("location.specified=true");

        // Get all the churchActivityList where location is null
        defaultChurchActivityShouldNotBeFound("location.specified=false");
    }
                @Test
    @Transactional
    public void getAllChurchActivitiesByLocationContainsSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where location contains DEFAULT_LOCATION
        defaultChurchActivityShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the churchActivityList where location contains UPDATED_LOCATION
        defaultChurchActivityShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where location does not contain DEFAULT_LOCATION
        defaultChurchActivityShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the churchActivityList where location does not contain UPDATED_LOCATION
        defaultChurchActivityShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }


    @Test
    @Transactional
    public void getAllChurchActivitiesByEstamateBudgetIsEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where estamateBudget equals to DEFAULT_ESTAMATE_BUDGET
        defaultChurchActivityShouldBeFound("estamateBudget.equals=" + DEFAULT_ESTAMATE_BUDGET);

        // Get all the churchActivityList where estamateBudget equals to UPDATED_ESTAMATE_BUDGET
        defaultChurchActivityShouldNotBeFound("estamateBudget.equals=" + UPDATED_ESTAMATE_BUDGET);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEstamateBudgetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where estamateBudget not equals to DEFAULT_ESTAMATE_BUDGET
        defaultChurchActivityShouldNotBeFound("estamateBudget.notEquals=" + DEFAULT_ESTAMATE_BUDGET);

        // Get all the churchActivityList where estamateBudget not equals to UPDATED_ESTAMATE_BUDGET
        defaultChurchActivityShouldBeFound("estamateBudget.notEquals=" + UPDATED_ESTAMATE_BUDGET);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEstamateBudgetIsInShouldWork() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where estamateBudget in DEFAULT_ESTAMATE_BUDGET or UPDATED_ESTAMATE_BUDGET
        defaultChurchActivityShouldBeFound("estamateBudget.in=" + DEFAULT_ESTAMATE_BUDGET + "," + UPDATED_ESTAMATE_BUDGET);

        // Get all the churchActivityList where estamateBudget equals to UPDATED_ESTAMATE_BUDGET
        defaultChurchActivityShouldNotBeFound("estamateBudget.in=" + UPDATED_ESTAMATE_BUDGET);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEstamateBudgetIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where estamateBudget is not null
        defaultChurchActivityShouldBeFound("estamateBudget.specified=true");

        // Get all the churchActivityList where estamateBudget is null
        defaultChurchActivityShouldNotBeFound("estamateBudget.specified=false");
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEstamateBudgetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where estamateBudget is greater than or equal to DEFAULT_ESTAMATE_BUDGET
        defaultChurchActivityShouldBeFound("estamateBudget.greaterThanOrEqual=" + DEFAULT_ESTAMATE_BUDGET);

        // Get all the churchActivityList where estamateBudget is greater than or equal to UPDATED_ESTAMATE_BUDGET
        defaultChurchActivityShouldNotBeFound("estamateBudget.greaterThanOrEqual=" + UPDATED_ESTAMATE_BUDGET);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEstamateBudgetIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where estamateBudget is less than or equal to DEFAULT_ESTAMATE_BUDGET
        defaultChurchActivityShouldBeFound("estamateBudget.lessThanOrEqual=" + DEFAULT_ESTAMATE_BUDGET);

        // Get all the churchActivityList where estamateBudget is less than or equal to SMALLER_ESTAMATE_BUDGET
        defaultChurchActivityShouldNotBeFound("estamateBudget.lessThanOrEqual=" + SMALLER_ESTAMATE_BUDGET);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEstamateBudgetIsLessThanSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where estamateBudget is less than DEFAULT_ESTAMATE_BUDGET
        defaultChurchActivityShouldNotBeFound("estamateBudget.lessThan=" + DEFAULT_ESTAMATE_BUDGET);

        // Get all the churchActivityList where estamateBudget is less than UPDATED_ESTAMATE_BUDGET
        defaultChurchActivityShouldBeFound("estamateBudget.lessThan=" + UPDATED_ESTAMATE_BUDGET);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEstamateBudgetIsGreaterThanSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where estamateBudget is greater than DEFAULT_ESTAMATE_BUDGET
        defaultChurchActivityShouldNotBeFound("estamateBudget.greaterThan=" + DEFAULT_ESTAMATE_BUDGET);

        // Get all the churchActivityList where estamateBudget is greater than SMALLER_ESTAMATE_BUDGET
        defaultChurchActivityShouldBeFound("estamateBudget.greaterThan=" + SMALLER_ESTAMATE_BUDGET);
    }


    @Test
    @Transactional
    public void getAllChurchActivitiesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where startDate equals to DEFAULT_START_DATE
        defaultChurchActivityShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the churchActivityList where startDate equals to UPDATED_START_DATE
        defaultChurchActivityShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where startDate not equals to DEFAULT_START_DATE
        defaultChurchActivityShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the churchActivityList where startDate not equals to UPDATED_START_DATE
        defaultChurchActivityShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultChurchActivityShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the churchActivityList where startDate equals to UPDATED_START_DATE
        defaultChurchActivityShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where startDate is not null
        defaultChurchActivityShouldBeFound("startDate.specified=true");

        // Get all the churchActivityList where startDate is null
        defaultChurchActivityShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultChurchActivityShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the churchActivityList where startDate is greater than or equal to UPDATED_START_DATE
        defaultChurchActivityShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where startDate is less than or equal to DEFAULT_START_DATE
        defaultChurchActivityShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the churchActivityList where startDate is less than or equal to SMALLER_START_DATE
        defaultChurchActivityShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where startDate is less than DEFAULT_START_DATE
        defaultChurchActivityShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the churchActivityList where startDate is less than UPDATED_START_DATE
        defaultChurchActivityShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where startDate is greater than DEFAULT_START_DATE
        defaultChurchActivityShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the churchActivityList where startDate is greater than SMALLER_START_DATE
        defaultChurchActivityShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllChurchActivitiesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where endDate equals to DEFAULT_END_DATE
        defaultChurchActivityShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the churchActivityList where endDate equals to UPDATED_END_DATE
        defaultChurchActivityShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where endDate not equals to DEFAULT_END_DATE
        defaultChurchActivityShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the churchActivityList where endDate not equals to UPDATED_END_DATE
        defaultChurchActivityShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultChurchActivityShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the churchActivityList where endDate equals to UPDATED_END_DATE
        defaultChurchActivityShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where endDate is not null
        defaultChurchActivityShouldBeFound("endDate.specified=true");

        // Get all the churchActivityList where endDate is null
        defaultChurchActivityShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultChurchActivityShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the churchActivityList where endDate is greater than or equal to UPDATED_END_DATE
        defaultChurchActivityShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where endDate is less than or equal to DEFAULT_END_DATE
        defaultChurchActivityShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the churchActivityList where endDate is less than or equal to SMALLER_END_DATE
        defaultChurchActivityShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where endDate is less than DEFAULT_END_DATE
        defaultChurchActivityShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the churchActivityList where endDate is less than UPDATED_END_DATE
        defaultChurchActivityShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllChurchActivitiesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        churchActivityRepository.saveAndFlush(churchActivity);

        // Get all the churchActivityList where endDate is greater than DEFAULT_END_DATE
        defaultChurchActivityShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the churchActivityList where endDate is greater than SMALLER_END_DATE
        defaultChurchActivityShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllChurchActivitiesByChurchIsEqualToSomething() throws Exception {
        // Get already existing entity
        Church church = churchActivity.getChurch();
        churchActivityRepository.saveAndFlush(churchActivity);
        Long churchId = church.getId();

        // Get all the churchActivityList where church equals to churchId
        defaultChurchActivityShouldBeFound("churchId.equals=" + churchId);

        // Get all the churchActivityList where church equals to churchId + 1
        defaultChurchActivityShouldNotBeFound("churchId.equals=" + (churchId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChurchActivityShouldBeFound(String filter) throws Exception {
        restChurchActivityMockMvc.perform(get("/api/church-activities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(churchActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].estamateBudget").value(hasItem(DEFAULT_ESTAMATE_BUDGET.intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restChurchActivityMockMvc.perform(get("/api/church-activities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChurchActivityShouldNotBeFound(String filter) throws Exception {
        restChurchActivityMockMvc.perform(get("/api/church-activities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChurchActivityMockMvc.perform(get("/api/church-activities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChurchActivity() throws Exception {
        // Get the churchActivity
        restChurchActivityMockMvc.perform(get("/api/church-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChurchActivity() throws Exception {
        // Initialize the database
        churchActivityService.save(churchActivity);

        int databaseSizeBeforeUpdate = churchActivityRepository.findAll().size();

        // Update the churchActivity
        ChurchActivity updatedChurchActivity = churchActivityRepository.findById(churchActivity.getId()).get();
        // Disconnect from session so that the updates on updatedChurchActivity are not directly saved in db
        em.detach(updatedChurchActivity);
        updatedChurchActivity
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .estamateBudget(UPDATED_ESTAMATE_BUDGET)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restChurchActivityMockMvc.perform(put("/api/church-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChurchActivity)))
            .andExpect(status().isOk());

        // Validate the ChurchActivity in the database
        List<ChurchActivity> churchActivityList = churchActivityRepository.findAll();
        assertThat(churchActivityList).hasSize(databaseSizeBeforeUpdate);
        ChurchActivity testChurchActivity = churchActivityList.get(churchActivityList.size() - 1);
        assertThat(testChurchActivity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChurchActivity.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testChurchActivity.getEstamateBudget()).isEqualTo(UPDATED_ESTAMATE_BUDGET);
        assertThat(testChurchActivity.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testChurchActivity.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingChurchActivity() throws Exception {
        int databaseSizeBeforeUpdate = churchActivityRepository.findAll().size();

        // Create the ChurchActivity

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchActivityMockMvc.perform(put("/api/church-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(churchActivity)))
            .andExpect(status().isBadRequest());

        // Validate the ChurchActivity in the database
        List<ChurchActivity> churchActivityList = churchActivityRepository.findAll();
        assertThat(churchActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChurchActivity() throws Exception {
        // Initialize the database
        churchActivityService.save(churchActivity);

        int databaseSizeBeforeDelete = churchActivityRepository.findAll().size();

        // Delete the churchActivity
        restChurchActivityMockMvc.perform(delete("/api/church-activities/{id}", churchActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChurchActivity> churchActivityList = churchActivityRepository.findAll();
        assertThat(churchActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChurchActivity.class);
        ChurchActivity churchActivity1 = new ChurchActivity();
        churchActivity1.setId(1L);
        ChurchActivity churchActivity2 = new ChurchActivity();
        churchActivity2.setId(churchActivity1.getId());
        assertThat(churchActivity1).isEqualTo(churchActivity2);
        churchActivity2.setId(2L);
        assertThat(churchActivity1).isNotEqualTo(churchActivity2);
        churchActivity1.setId(null);
        assertThat(churchActivity1).isNotEqualTo(churchActivity2);
    }
}
