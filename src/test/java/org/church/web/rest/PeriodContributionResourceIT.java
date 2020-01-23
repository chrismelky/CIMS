package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.PeriodContribution;
import org.church.domain.Period;
import org.church.domain.Member;
import org.church.domain.Church;
import org.church.domain.PeriodContributionType;
import org.church.repository.PeriodContributionRepository;
import org.church.service.PeriodContributionService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.PeriodContributionCriteria;
import org.church.service.PeriodContributionQueryService;

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
 * Integration tests for the {@link PeriodContributionResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class PeriodContributionResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT_PROMISED = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_PROMISED = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT_PROMISED = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AMOUNT_CONTRIBUTED = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_CONTRIBUTED = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT_CONTRIBUTED = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DUE_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private PeriodContributionRepository periodContributionRepository;

    @Autowired
    private PeriodContributionService periodContributionService;

    @Autowired
    private PeriodContributionQueryService periodContributionQueryService;

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

    private MockMvc restPeriodContributionMockMvc;

    private PeriodContribution periodContribution;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PeriodContributionResource periodContributionResource = new PeriodContributionResource(periodContributionService, periodContributionQueryService);
        this.restPeriodContributionMockMvc = MockMvcBuilders.standaloneSetup(periodContributionResource)
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
    public static PeriodContribution createEntity(EntityManager em) {
        PeriodContribution periodContribution = new PeriodContribution()
            .amountPromised(DEFAULT_AMOUNT_PROMISED)
            .amountContributed(DEFAULT_AMOUNT_CONTRIBUTED)
            .description(DEFAULT_DESCRIPTION)
            .dueDate(DEFAULT_DUE_DATE);
        // Add required entity
        Period period;
        if (TestUtil.findAll(em, Period.class).isEmpty()) {
            period = PeriodResourceIT.createEntity(em);
            em.persist(period);
            em.flush();
        } else {
            period = TestUtil.findAll(em, Period.class).get(0);
        }
        periodContribution.setPeriod(period);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceIT.createEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        periodContribution.setMember(member);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        periodContribution.setChurch(church);
        // Add required entity
        PeriodContributionType periodContributionType;
        if (TestUtil.findAll(em, PeriodContributionType.class).isEmpty()) {
            periodContributionType = PeriodContributionTypeResourceIT.createEntity(em);
            em.persist(periodContributionType);
            em.flush();
        } else {
            periodContributionType = TestUtil.findAll(em, PeriodContributionType.class).get(0);
        }
        periodContribution.setPeriodContributionType(periodContributionType);
        return periodContribution;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodContribution createUpdatedEntity(EntityManager em) {
        PeriodContribution periodContribution = new PeriodContribution()
            .amountPromised(UPDATED_AMOUNT_PROMISED)
            .amountContributed(UPDATED_AMOUNT_CONTRIBUTED)
            .description(UPDATED_DESCRIPTION)
            .dueDate(UPDATED_DUE_DATE);
        // Add required entity
        Period period;
        if (TestUtil.findAll(em, Period.class).isEmpty()) {
            period = PeriodResourceIT.createUpdatedEntity(em);
            em.persist(period);
            em.flush();
        } else {
            period = TestUtil.findAll(em, Period.class).get(0);
        }
        periodContribution.setPeriod(period);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceIT.createUpdatedEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        periodContribution.setMember(member);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createUpdatedEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        periodContribution.setChurch(church);
        // Add required entity
        PeriodContributionType periodContributionType;
        if (TestUtil.findAll(em, PeriodContributionType.class).isEmpty()) {
            periodContributionType = PeriodContributionTypeResourceIT.createUpdatedEntity(em);
            em.persist(periodContributionType);
            em.flush();
        } else {
            periodContributionType = TestUtil.findAll(em, PeriodContributionType.class).get(0);
        }
        periodContribution.setPeriodContributionType(periodContributionType);
        return periodContribution;
    }

    @BeforeEach
    public void initTest() {
        periodContribution = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeriodContribution() throws Exception {
        int databaseSizeBeforeCreate = periodContributionRepository.findAll().size();

        // Create the PeriodContribution
        restPeriodContributionMockMvc.perform(post("/api/period-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContribution)))
            .andExpect(status().isCreated());

        // Validate the PeriodContribution in the database
        List<PeriodContribution> periodContributionList = periodContributionRepository.findAll();
        assertThat(periodContributionList).hasSize(databaseSizeBeforeCreate + 1);
        PeriodContribution testPeriodContribution = periodContributionList.get(periodContributionList.size() - 1);
        assertThat(testPeriodContribution.getAmountPromised()).isEqualTo(DEFAULT_AMOUNT_PROMISED);
        assertThat(testPeriodContribution.getAmountContributed()).isEqualTo(DEFAULT_AMOUNT_CONTRIBUTED);
        assertThat(testPeriodContribution.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPeriodContribution.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
    }

    @Test
    @Transactional
    public void createPeriodContributionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = periodContributionRepository.findAll().size();

        // Create the PeriodContribution with an existing ID
        periodContribution.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodContributionMockMvc.perform(post("/api/period-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContribution)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodContribution in the database
        List<PeriodContribution> periodContributionList = periodContributionRepository.findAll();
        assertThat(periodContributionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAmountPromisedIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodContributionRepository.findAll().size();
        // set the field null
        periodContribution.setAmountPromised(null);

        // Create the PeriodContribution, which fails.

        restPeriodContributionMockMvc.perform(post("/api/period-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContribution)))
            .andExpect(status().isBadRequest());

        List<PeriodContribution> periodContributionList = periodContributionRepository.findAll();
        assertThat(periodContributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodContributionRepository.findAll().size();
        // set the field null
        periodContribution.setDueDate(null);

        // Create the PeriodContribution, which fails.

        restPeriodContributionMockMvc.perform(post("/api/period-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContribution)))
            .andExpect(status().isBadRequest());

        List<PeriodContribution> periodContributionList = periodContributionRepository.findAll();
        assertThat(periodContributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeriodContributions() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList
        restPeriodContributionMockMvc.perform(get("/api/period-contributions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodContribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountPromised").value(hasItem(DEFAULT_AMOUNT_PROMISED.intValue())))
            .andExpect(jsonPath("$.[*].amountContributed").value(hasItem(DEFAULT_AMOUNT_CONTRIBUTED.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getPeriodContribution() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get the periodContribution
        restPeriodContributionMockMvc.perform(get("/api/period-contributions/{id}", periodContribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(periodContribution.getId().intValue()))
            .andExpect(jsonPath("$.amountPromised").value(DEFAULT_AMOUNT_PROMISED.intValue()))
            .andExpect(jsonPath("$.amountContributed").value(DEFAULT_AMOUNT_CONTRIBUTED.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountPromisedIsEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountPromised equals to DEFAULT_AMOUNT_PROMISED
        defaultPeriodContributionShouldBeFound("amountPromised.equals=" + DEFAULT_AMOUNT_PROMISED);

        // Get all the periodContributionList where amountPromised equals to UPDATED_AMOUNT_PROMISED
        defaultPeriodContributionShouldNotBeFound("amountPromised.equals=" + UPDATED_AMOUNT_PROMISED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountPromisedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountPromised not equals to DEFAULT_AMOUNT_PROMISED
        defaultPeriodContributionShouldNotBeFound("amountPromised.notEquals=" + DEFAULT_AMOUNT_PROMISED);

        // Get all the periodContributionList where amountPromised not equals to UPDATED_AMOUNT_PROMISED
        defaultPeriodContributionShouldBeFound("amountPromised.notEquals=" + UPDATED_AMOUNT_PROMISED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountPromisedIsInShouldWork() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountPromised in DEFAULT_AMOUNT_PROMISED or UPDATED_AMOUNT_PROMISED
        defaultPeriodContributionShouldBeFound("amountPromised.in=" + DEFAULT_AMOUNT_PROMISED + "," + UPDATED_AMOUNT_PROMISED);

        // Get all the periodContributionList where amountPromised equals to UPDATED_AMOUNT_PROMISED
        defaultPeriodContributionShouldNotBeFound("amountPromised.in=" + UPDATED_AMOUNT_PROMISED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountPromisedIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountPromised is not null
        defaultPeriodContributionShouldBeFound("amountPromised.specified=true");

        // Get all the periodContributionList where amountPromised is null
        defaultPeriodContributionShouldNotBeFound("amountPromised.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountPromisedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountPromised is greater than or equal to DEFAULT_AMOUNT_PROMISED
        defaultPeriodContributionShouldBeFound("amountPromised.greaterThanOrEqual=" + DEFAULT_AMOUNT_PROMISED);

        // Get all the periodContributionList where amountPromised is greater than or equal to UPDATED_AMOUNT_PROMISED
        defaultPeriodContributionShouldNotBeFound("amountPromised.greaterThanOrEqual=" + UPDATED_AMOUNT_PROMISED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountPromisedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountPromised is less than or equal to DEFAULT_AMOUNT_PROMISED
        defaultPeriodContributionShouldBeFound("amountPromised.lessThanOrEqual=" + DEFAULT_AMOUNT_PROMISED);

        // Get all the periodContributionList where amountPromised is less than or equal to SMALLER_AMOUNT_PROMISED
        defaultPeriodContributionShouldNotBeFound("amountPromised.lessThanOrEqual=" + SMALLER_AMOUNT_PROMISED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountPromisedIsLessThanSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountPromised is less than DEFAULT_AMOUNT_PROMISED
        defaultPeriodContributionShouldNotBeFound("amountPromised.lessThan=" + DEFAULT_AMOUNT_PROMISED);

        // Get all the periodContributionList where amountPromised is less than UPDATED_AMOUNT_PROMISED
        defaultPeriodContributionShouldBeFound("amountPromised.lessThan=" + UPDATED_AMOUNT_PROMISED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountPromisedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountPromised is greater than DEFAULT_AMOUNT_PROMISED
        defaultPeriodContributionShouldNotBeFound("amountPromised.greaterThan=" + DEFAULT_AMOUNT_PROMISED);

        // Get all the periodContributionList where amountPromised is greater than SMALLER_AMOUNT_PROMISED
        defaultPeriodContributionShouldBeFound("amountPromised.greaterThan=" + SMALLER_AMOUNT_PROMISED);
    }


    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountContributedIsEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountContributed equals to DEFAULT_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldBeFound("amountContributed.equals=" + DEFAULT_AMOUNT_CONTRIBUTED);

        // Get all the periodContributionList where amountContributed equals to UPDATED_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldNotBeFound("amountContributed.equals=" + UPDATED_AMOUNT_CONTRIBUTED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountContributedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountContributed not equals to DEFAULT_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldNotBeFound("amountContributed.notEquals=" + DEFAULT_AMOUNT_CONTRIBUTED);

        // Get all the periodContributionList where amountContributed not equals to UPDATED_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldBeFound("amountContributed.notEquals=" + UPDATED_AMOUNT_CONTRIBUTED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountContributedIsInShouldWork() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountContributed in DEFAULT_AMOUNT_CONTRIBUTED or UPDATED_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldBeFound("amountContributed.in=" + DEFAULT_AMOUNT_CONTRIBUTED + "," + UPDATED_AMOUNT_CONTRIBUTED);

        // Get all the periodContributionList where amountContributed equals to UPDATED_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldNotBeFound("amountContributed.in=" + UPDATED_AMOUNT_CONTRIBUTED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountContributedIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountContributed is not null
        defaultPeriodContributionShouldBeFound("amountContributed.specified=true");

        // Get all the periodContributionList where amountContributed is null
        defaultPeriodContributionShouldNotBeFound("amountContributed.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountContributedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountContributed is greater than or equal to DEFAULT_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldBeFound("amountContributed.greaterThanOrEqual=" + DEFAULT_AMOUNT_CONTRIBUTED);

        // Get all the periodContributionList where amountContributed is greater than or equal to UPDATED_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldNotBeFound("amountContributed.greaterThanOrEqual=" + UPDATED_AMOUNT_CONTRIBUTED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountContributedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountContributed is less than or equal to DEFAULT_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldBeFound("amountContributed.lessThanOrEqual=" + DEFAULT_AMOUNT_CONTRIBUTED);

        // Get all the periodContributionList where amountContributed is less than or equal to SMALLER_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldNotBeFound("amountContributed.lessThanOrEqual=" + SMALLER_AMOUNT_CONTRIBUTED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountContributedIsLessThanSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountContributed is less than DEFAULT_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldNotBeFound("amountContributed.lessThan=" + DEFAULT_AMOUNT_CONTRIBUTED);

        // Get all the periodContributionList where amountContributed is less than UPDATED_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldBeFound("amountContributed.lessThan=" + UPDATED_AMOUNT_CONTRIBUTED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByAmountContributedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where amountContributed is greater than DEFAULT_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldNotBeFound("amountContributed.greaterThan=" + DEFAULT_AMOUNT_CONTRIBUTED);

        // Get all the periodContributionList where amountContributed is greater than SMALLER_AMOUNT_CONTRIBUTED
        defaultPeriodContributionShouldBeFound("amountContributed.greaterThan=" + SMALLER_AMOUNT_CONTRIBUTED);
    }


    @Test
    @Transactional
    public void getAllPeriodContributionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where description equals to DEFAULT_DESCRIPTION
        defaultPeriodContributionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the periodContributionList where description equals to UPDATED_DESCRIPTION
        defaultPeriodContributionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where description not equals to DEFAULT_DESCRIPTION
        defaultPeriodContributionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the periodContributionList where description not equals to UPDATED_DESCRIPTION
        defaultPeriodContributionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPeriodContributionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the periodContributionList where description equals to UPDATED_DESCRIPTION
        defaultPeriodContributionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where description is not null
        defaultPeriodContributionShouldBeFound("description.specified=true");

        // Get all the periodContributionList where description is null
        defaultPeriodContributionShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeriodContributionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where description contains DEFAULT_DESCRIPTION
        defaultPeriodContributionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the periodContributionList where description contains UPDATED_DESCRIPTION
        defaultPeriodContributionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where description does not contain DEFAULT_DESCRIPTION
        defaultPeriodContributionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the periodContributionList where description does not contain UPDATED_DESCRIPTION
        defaultPeriodContributionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllPeriodContributionsByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where dueDate equals to DEFAULT_DUE_DATE
        defaultPeriodContributionShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the periodContributionList where dueDate equals to UPDATED_DUE_DATE
        defaultPeriodContributionShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where dueDate not equals to DEFAULT_DUE_DATE
        defaultPeriodContributionShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

        // Get all the periodContributionList where dueDate not equals to UPDATED_DUE_DATE
        defaultPeriodContributionShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultPeriodContributionShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the periodContributionList where dueDate equals to UPDATED_DUE_DATE
        defaultPeriodContributionShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where dueDate is not null
        defaultPeriodContributionShouldBeFound("dueDate.specified=true");

        // Get all the periodContributionList where dueDate is null
        defaultPeriodContributionShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where dueDate is greater than or equal to DEFAULT_DUE_DATE
        defaultPeriodContributionShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the periodContributionList where dueDate is greater than or equal to UPDATED_DUE_DATE
        defaultPeriodContributionShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where dueDate is less than or equal to DEFAULT_DUE_DATE
        defaultPeriodContributionShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the periodContributionList where dueDate is less than or equal to SMALLER_DUE_DATE
        defaultPeriodContributionShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where dueDate is less than DEFAULT_DUE_DATE
        defaultPeriodContributionShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the periodContributionList where dueDate is less than UPDATED_DUE_DATE
        defaultPeriodContributionShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionsByDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        periodContributionRepository.saveAndFlush(periodContribution);

        // Get all the periodContributionList where dueDate is greater than DEFAULT_DUE_DATE
        defaultPeriodContributionShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

        // Get all the periodContributionList where dueDate is greater than SMALLER_DUE_DATE
        defaultPeriodContributionShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
    }


    @Test
    @Transactional
    public void getAllPeriodContributionsByPeriodIsEqualToSomething() throws Exception {
        // Get already existing entity
        Period period = periodContribution.getPeriod();
        periodContributionRepository.saveAndFlush(periodContribution);
        Long periodId = period.getId();

        // Get all the periodContributionList where period equals to periodId
        defaultPeriodContributionShouldBeFound("periodId.equals=" + periodId);

        // Get all the periodContributionList where period equals to periodId + 1
        defaultPeriodContributionShouldNotBeFound("periodId.equals=" + (periodId + 1));
    }


    @Test
    @Transactional
    public void getAllPeriodContributionsByMemberIsEqualToSomething() throws Exception {
        // Get already existing entity
        Member member = periodContribution.getMember();
        periodContributionRepository.saveAndFlush(periodContribution);
        Long memberId = member.getId();

        // Get all the periodContributionList where member equals to memberId
        defaultPeriodContributionShouldBeFound("memberId.equals=" + memberId);

        // Get all the periodContributionList where member equals to memberId + 1
        defaultPeriodContributionShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }


    @Test
    @Transactional
    public void getAllPeriodContributionsByChurchIsEqualToSomething() throws Exception {
        // Get already existing entity
        Church church = periodContribution.getChurch();
        periodContributionRepository.saveAndFlush(periodContribution);
        Long churchId = church.getId();

        // Get all the periodContributionList where church equals to churchId
        defaultPeriodContributionShouldBeFound("churchId.equals=" + churchId);

        // Get all the periodContributionList where church equals to churchId + 1
        defaultPeriodContributionShouldNotBeFound("churchId.equals=" + (churchId + 1));
    }


    @Test
    @Transactional
    public void getAllPeriodContributionsByPeriodContributionTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        PeriodContributionType periodContributionType = periodContribution.getPeriodContributionType();
        periodContributionRepository.saveAndFlush(periodContribution);
        Long periodContributionTypeId = periodContributionType.getId();

        // Get all the periodContributionList where periodContributionType equals to periodContributionTypeId
        defaultPeriodContributionShouldBeFound("periodContributionTypeId.equals=" + periodContributionTypeId);

        // Get all the periodContributionList where periodContributionType equals to periodContributionTypeId + 1
        defaultPeriodContributionShouldNotBeFound("periodContributionTypeId.equals=" + (periodContributionTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPeriodContributionShouldBeFound(String filter) throws Exception {
        restPeriodContributionMockMvc.perform(get("/api/period-contributions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodContribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountPromised").value(hasItem(DEFAULT_AMOUNT_PROMISED.intValue())))
            .andExpect(jsonPath("$.[*].amountContributed").value(hasItem(DEFAULT_AMOUNT_CONTRIBUTED.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())));

        // Check, that the count call also returns 1
        restPeriodContributionMockMvc.perform(get("/api/period-contributions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPeriodContributionShouldNotBeFound(String filter) throws Exception {
        restPeriodContributionMockMvc.perform(get("/api/period-contributions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPeriodContributionMockMvc.perform(get("/api/period-contributions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPeriodContribution() throws Exception {
        // Get the periodContribution
        restPeriodContributionMockMvc.perform(get("/api/period-contributions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriodContribution() throws Exception {
        // Initialize the database
        periodContributionService.save(periodContribution);

        int databaseSizeBeforeUpdate = periodContributionRepository.findAll().size();

        // Update the periodContribution
        PeriodContribution updatedPeriodContribution = periodContributionRepository.findById(periodContribution.getId()).get();
        // Disconnect from session so that the updates on updatedPeriodContribution are not directly saved in db
        em.detach(updatedPeriodContribution);
        updatedPeriodContribution
            .amountPromised(UPDATED_AMOUNT_PROMISED)
            .amountContributed(UPDATED_AMOUNT_CONTRIBUTED)
            .description(UPDATED_DESCRIPTION)
            .dueDate(UPDATED_DUE_DATE);

        restPeriodContributionMockMvc.perform(put("/api/period-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeriodContribution)))
            .andExpect(status().isOk());

        // Validate the PeriodContribution in the database
        List<PeriodContribution> periodContributionList = periodContributionRepository.findAll();
        assertThat(periodContributionList).hasSize(databaseSizeBeforeUpdate);
        PeriodContribution testPeriodContribution = periodContributionList.get(periodContributionList.size() - 1);
        assertThat(testPeriodContribution.getAmountPromised()).isEqualTo(UPDATED_AMOUNT_PROMISED);
        assertThat(testPeriodContribution.getAmountContributed()).isEqualTo(UPDATED_AMOUNT_CONTRIBUTED);
        assertThat(testPeriodContribution.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPeriodContribution.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPeriodContribution() throws Exception {
        int databaseSizeBeforeUpdate = periodContributionRepository.findAll().size();

        // Create the PeriodContribution

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodContributionMockMvc.perform(put("/api/period-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContribution)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodContribution in the database
        List<PeriodContribution> periodContributionList = periodContributionRepository.findAll();
        assertThat(periodContributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePeriodContribution() throws Exception {
        // Initialize the database
        periodContributionService.save(periodContribution);

        int databaseSizeBeforeDelete = periodContributionRepository.findAll().size();

        // Delete the periodContribution
        restPeriodContributionMockMvc.perform(delete("/api/period-contributions/{id}", periodContribution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PeriodContribution> periodContributionList = periodContributionRepository.findAll();
        assertThat(periodContributionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodContribution.class);
        PeriodContribution periodContribution1 = new PeriodContribution();
        periodContribution1.setId(1L);
        PeriodContribution periodContribution2 = new PeriodContribution();
        periodContribution2.setId(periodContribution1.getId());
        assertThat(periodContribution1).isEqualTo(periodContribution2);
        periodContribution2.setId(2L);
        assertThat(periodContribution1).isNotEqualTo(periodContribution2);
        periodContribution1.setId(null);
        assertThat(periodContribution1).isNotEqualTo(periodContribution2);
    }
}
