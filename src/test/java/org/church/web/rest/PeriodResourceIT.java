package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.Period;
import org.church.domain.PeriodType;
import org.church.repository.PeriodRepository;
import org.church.service.PeriodService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.PeriodCriteria;
import org.church.service.PeriodQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.church.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PeriodResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class PeriodResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_CURRENT = false;
    private static final Boolean UPDATED_IS_CURRENT = true;

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private PeriodQueryService periodQueryService;

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

    private MockMvc restPeriodMockMvc;

    private Period period;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PeriodResource periodResource = new PeriodResource(periodService, periodQueryService);
        this.restPeriodMockMvc = MockMvcBuilders.standaloneSetup(periodResource)
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
    public static Period createEntity(EntityManager em) {
        Period period = new Period()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isCurrent(DEFAULT_IS_CURRENT);
        // Add required entity
        PeriodType periodType;
        if (TestUtil.findAll(em, PeriodType.class).isEmpty()) {
            periodType = PeriodTypeResourceIT.createEntity(em);
            em.persist(periodType);
            em.flush();
        } else {
            periodType = TestUtil.findAll(em, PeriodType.class).get(0);
        }
        period.setType(periodType);
        return period;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Period createUpdatedEntity(EntityManager em) {
        Period period = new Period()
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCurrent(UPDATED_IS_CURRENT);
        // Add required entity
        PeriodType periodType;
        if (TestUtil.findAll(em, PeriodType.class).isEmpty()) {
            periodType = PeriodTypeResourceIT.createUpdatedEntity(em);
            em.persist(periodType);
            em.flush();
        } else {
            periodType = TestUtil.findAll(em, PeriodType.class).get(0);
        }
        period.setType(periodType);
        return period;
    }

    @BeforeEach
    public void initTest() {
        period = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeriod() throws Exception {
        int databaseSizeBeforeCreate = periodRepository.findAll().size();

        // Create the Period
        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isCreated());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeCreate + 1);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPeriod.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPeriod.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPeriod.isIsCurrent()).isEqualTo(DEFAULT_IS_CURRENT);
    }

    @Test
    @Transactional
    public void createPeriodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = periodRepository.findAll().size();

        // Create the Period with an existing ID
        period.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodRepository.findAll().size();
        // set the field null
        period.setName(null);

        // Create the Period, which fails.

        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isBadRequest());

        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodRepository.findAll().size();
        // set the field null
        period.setStartDate(null);

        // Create the Period, which fails.

        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isBadRequest());

        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodRepository.findAll().size();
        // set the field null
        period.setEndDate(null);

        // Create the Period, which fails.

        restPeriodMockMvc.perform(post("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isBadRequest());

        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeriods() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList
        restPeriodMockMvc.perform(get("/api/periods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(period.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCurrent").value(hasItem(DEFAULT_IS_CURRENT.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get the period
        restPeriodMockMvc.perform(get("/api/periods/{id}", period.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(period.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isCurrent").value(DEFAULT_IS_CURRENT.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllPeriodsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where name equals to DEFAULT_NAME
        defaultPeriodShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the periodList where name equals to UPDATED_NAME
        defaultPeriodShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where name not equals to DEFAULT_NAME
        defaultPeriodShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the periodList where name not equals to UPDATED_NAME
        defaultPeriodShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPeriodShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the periodList where name equals to UPDATED_NAME
        defaultPeriodShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where name is not null
        defaultPeriodShouldBeFound("name.specified=true");

        // Get all the periodList where name is null
        defaultPeriodShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeriodsByNameContainsSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where name contains DEFAULT_NAME
        defaultPeriodShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the periodList where name contains UPDATED_NAME
        defaultPeriodShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPeriodsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where name does not contain DEFAULT_NAME
        defaultPeriodShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the periodList where name does not contain UPDATED_NAME
        defaultPeriodShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPeriodsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where startDate equals to DEFAULT_START_DATE
        defaultPeriodShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the periodList where startDate equals to UPDATED_START_DATE
        defaultPeriodShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where startDate not equals to DEFAULT_START_DATE
        defaultPeriodShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the periodList where startDate not equals to UPDATED_START_DATE
        defaultPeriodShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultPeriodShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the periodList where startDate equals to UPDATED_START_DATE
        defaultPeriodShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where startDate is not null
        defaultPeriodShouldBeFound("startDate.specified=true");

        // Get all the periodList where startDate is null
        defaultPeriodShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeriodsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultPeriodShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the periodList where startDate is greater than or equal to UPDATED_START_DATE
        defaultPeriodShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where startDate is less than or equal to DEFAULT_START_DATE
        defaultPeriodShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the periodList where startDate is less than or equal to SMALLER_START_DATE
        defaultPeriodShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where startDate is less than DEFAULT_START_DATE
        defaultPeriodShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the periodList where startDate is less than UPDATED_START_DATE
        defaultPeriodShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where startDate is greater than DEFAULT_START_DATE
        defaultPeriodShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the periodList where startDate is greater than SMALLER_START_DATE
        defaultPeriodShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllPeriodsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where endDate equals to DEFAULT_END_DATE
        defaultPeriodShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the periodList where endDate equals to UPDATED_END_DATE
        defaultPeriodShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where endDate not equals to DEFAULT_END_DATE
        defaultPeriodShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the periodList where endDate not equals to UPDATED_END_DATE
        defaultPeriodShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultPeriodShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the periodList where endDate equals to UPDATED_END_DATE
        defaultPeriodShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where endDate is not null
        defaultPeriodShouldBeFound("endDate.specified=true");

        // Get all the periodList where endDate is null
        defaultPeriodShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeriodsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultPeriodShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the periodList where endDate is greater than or equal to UPDATED_END_DATE
        defaultPeriodShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where endDate is less than or equal to DEFAULT_END_DATE
        defaultPeriodShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the periodList where endDate is less than or equal to SMALLER_END_DATE
        defaultPeriodShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where endDate is less than DEFAULT_END_DATE
        defaultPeriodShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the periodList where endDate is less than UPDATED_END_DATE
        defaultPeriodShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where endDate is greater than DEFAULT_END_DATE
        defaultPeriodShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the periodList where endDate is greater than SMALLER_END_DATE
        defaultPeriodShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllPeriodsByIsCurrentIsEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where isCurrent equals to DEFAULT_IS_CURRENT
        defaultPeriodShouldBeFound("isCurrent.equals=" + DEFAULT_IS_CURRENT);

        // Get all the periodList where isCurrent equals to UPDATED_IS_CURRENT
        defaultPeriodShouldNotBeFound("isCurrent.equals=" + UPDATED_IS_CURRENT);
    }

    @Test
    @Transactional
    public void getAllPeriodsByIsCurrentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where isCurrent not equals to DEFAULT_IS_CURRENT
        defaultPeriodShouldNotBeFound("isCurrent.notEquals=" + DEFAULT_IS_CURRENT);

        // Get all the periodList where isCurrent not equals to UPDATED_IS_CURRENT
        defaultPeriodShouldBeFound("isCurrent.notEquals=" + UPDATED_IS_CURRENT);
    }

    @Test
    @Transactional
    public void getAllPeriodsByIsCurrentIsInShouldWork() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where isCurrent in DEFAULT_IS_CURRENT or UPDATED_IS_CURRENT
        defaultPeriodShouldBeFound("isCurrent.in=" + DEFAULT_IS_CURRENT + "," + UPDATED_IS_CURRENT);

        // Get all the periodList where isCurrent equals to UPDATED_IS_CURRENT
        defaultPeriodShouldNotBeFound("isCurrent.in=" + UPDATED_IS_CURRENT);
    }

    @Test
    @Transactional
    public void getAllPeriodsByIsCurrentIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList where isCurrent is not null
        defaultPeriodShouldBeFound("isCurrent.specified=true");

        // Get all the periodList where isCurrent is null
        defaultPeriodShouldNotBeFound("isCurrent.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeriodsByTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        PeriodType type = period.getType();
        periodRepository.saveAndFlush(period);
        Long typeId = type.getId();

        // Get all the periodList where type equals to typeId
        defaultPeriodShouldBeFound("typeId.equals=" + typeId);

        // Get all the periodList where type equals to typeId + 1
        defaultPeriodShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPeriodShouldBeFound(String filter) throws Exception {
        restPeriodMockMvc.perform(get("/api/periods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(period.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCurrent").value(hasItem(DEFAULT_IS_CURRENT.booleanValue())));

        // Check, that the count call also returns 1
        restPeriodMockMvc.perform(get("/api/periods/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPeriodShouldNotBeFound(String filter) throws Exception {
        restPeriodMockMvc.perform(get("/api/periods?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPeriodMockMvc.perform(get("/api/periods/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPeriod() throws Exception {
        // Get the period
        restPeriodMockMvc.perform(get("/api/periods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriod() throws Exception {
        // Initialize the database
        periodService.save(period);

        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Update the period
        Period updatedPeriod = periodRepository.findById(period.getId()).get();
        // Disconnect from session so that the updates on updatedPeriod are not directly saved in db
        em.detach(updatedPeriod);
        updatedPeriod
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCurrent(UPDATED_IS_CURRENT);

        restPeriodMockMvc.perform(put("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeriod)))
            .andExpect(status().isOk());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
        Period testPeriod = periodList.get(periodList.size() - 1);
        assertThat(testPeriod.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPeriod.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPeriod.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPeriod.isIsCurrent()).isEqualTo(UPDATED_IS_CURRENT);
    }

    @Test
    @Transactional
    public void updateNonExistingPeriod() throws Exception {
        int databaseSizeBeforeUpdate = periodRepository.findAll().size();

        // Create the Period

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodMockMvc.perform(put("/api/periods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(period)))
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePeriod() throws Exception {
        // Initialize the database
        periodService.save(period);

        int databaseSizeBeforeDelete = periodRepository.findAll().size();

        // Delete the period
        restPeriodMockMvc.perform(delete("/api/periods/{id}", period.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Period> periodList = periodRepository.findAll();
        assertThat(periodList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Period.class);
        Period period1 = new Period();
        period1.setId(1L);
        Period period2 = new Period();
        period2.setId(period1.getId());
        assertThat(period1).isEqualTo(period2);
        period2.setId(2L);
        assertThat(period1).isNotEqualTo(period2);
        period1.setId(null);
        assertThat(period1).isNotEqualTo(period2);
    }
}
