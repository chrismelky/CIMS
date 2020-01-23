package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.PeriodContributionItem;
import org.church.domain.PeriodContribution;
import org.church.repository.PeriodContributionItemRepository;
import org.church.service.PeriodContributionItemService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.PeriodContributionItemCriteria;
import org.church.service.PeriodContributionItemQueryService;

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
 * Integration tests for the {@link PeriodContributionItemResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class PeriodContributionItemResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_RECEIVED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEIVED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_RECEIVED = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_RECEIVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVED_BY = "BBBBBBBBBB";

    @Autowired
    private PeriodContributionItemRepository periodContributionItemRepository;

    @Autowired
    private PeriodContributionItemService periodContributionItemService;

    @Autowired
    private PeriodContributionItemQueryService periodContributionItemQueryService;

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

    private MockMvc restPeriodContributionItemMockMvc;

    private PeriodContributionItem periodContributionItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PeriodContributionItemResource periodContributionItemResource = new PeriodContributionItemResource(periodContributionItemService, periodContributionItemQueryService);
        this.restPeriodContributionItemMockMvc = MockMvcBuilders.standaloneSetup(periodContributionItemResource)
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
    public static PeriodContributionItem createEntity(EntityManager em) {
        PeriodContributionItem periodContributionItem = new PeriodContributionItem()
            .amount(DEFAULT_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .dateReceived(DEFAULT_DATE_RECEIVED)
            .receivedBy(DEFAULT_RECEIVED_BY);
        // Add required entity
        PeriodContribution periodContribution;
        if (TestUtil.findAll(em, PeriodContribution.class).isEmpty()) {
            periodContribution = PeriodContributionResourceIT.createEntity(em);
            em.persist(periodContribution);
            em.flush();
        } else {
            periodContribution = TestUtil.findAll(em, PeriodContribution.class).get(0);
        }
        periodContributionItem.setPeriodContribution(periodContribution);
        return periodContributionItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodContributionItem createUpdatedEntity(EntityManager em) {
        PeriodContributionItem periodContributionItem = new PeriodContributionItem()
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .dateReceived(UPDATED_DATE_RECEIVED)
            .receivedBy(UPDATED_RECEIVED_BY);
        // Add required entity
        PeriodContribution periodContribution;
        if (TestUtil.findAll(em, PeriodContribution.class).isEmpty()) {
            periodContribution = PeriodContributionResourceIT.createUpdatedEntity(em);
            em.persist(periodContribution);
            em.flush();
        } else {
            periodContribution = TestUtil.findAll(em, PeriodContribution.class).get(0);
        }
        periodContributionItem.setPeriodContribution(periodContribution);
        return periodContributionItem;
    }

    @BeforeEach
    public void initTest() {
        periodContributionItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeriodContributionItem() throws Exception {
        int databaseSizeBeforeCreate = periodContributionItemRepository.findAll().size();

        // Create the PeriodContributionItem
        restPeriodContributionItemMockMvc.perform(post("/api/period-contribution-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContributionItem)))
            .andExpect(status().isCreated());

        // Validate the PeriodContributionItem in the database
        List<PeriodContributionItem> periodContributionItemList = periodContributionItemRepository.findAll();
        assertThat(periodContributionItemList).hasSize(databaseSizeBeforeCreate + 1);
        PeriodContributionItem testPeriodContributionItem = periodContributionItemList.get(periodContributionItemList.size() - 1);
        assertThat(testPeriodContributionItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPeriodContributionItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPeriodContributionItem.getDateReceived()).isEqualTo(DEFAULT_DATE_RECEIVED);
        assertThat(testPeriodContributionItem.getReceivedBy()).isEqualTo(DEFAULT_RECEIVED_BY);
    }

    @Test
    @Transactional
    public void createPeriodContributionItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = periodContributionItemRepository.findAll().size();

        // Create the PeriodContributionItem with an existing ID
        periodContributionItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodContributionItemMockMvc.perform(post("/api/period-contribution-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContributionItem)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodContributionItem in the database
        List<PeriodContributionItem> periodContributionItemList = periodContributionItemRepository.findAll();
        assertThat(periodContributionItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodContributionItemRepository.findAll().size();
        // set the field null
        periodContributionItem.setAmount(null);

        // Create the PeriodContributionItem, which fails.

        restPeriodContributionItemMockMvc.perform(post("/api/period-contribution-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContributionItem)))
            .andExpect(status().isBadRequest());

        List<PeriodContributionItem> periodContributionItemList = periodContributionItemRepository.findAll();
        assertThat(periodContributionItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodContributionItemRepository.findAll().size();
        // set the field null
        periodContributionItem.setDateReceived(null);

        // Create the PeriodContributionItem, which fails.

        restPeriodContributionItemMockMvc.perform(post("/api/period-contribution-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContributionItem)))
            .andExpect(status().isBadRequest());

        List<PeriodContributionItem> periodContributionItemList = periodContributionItemRepository.findAll();
        assertThat(periodContributionItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceivedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = periodContributionItemRepository.findAll().size();
        // set the field null
        periodContributionItem.setReceivedBy(null);

        // Create the PeriodContributionItem, which fails.

        restPeriodContributionItemMockMvc.perform(post("/api/period-contribution-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContributionItem)))
            .andExpect(status().isBadRequest());

        List<PeriodContributionItem> periodContributionItemList = periodContributionItemRepository.findAll();
        assertThat(periodContributionItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItems() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList
        restPeriodContributionItemMockMvc.perform(get("/api/period-contribution-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodContributionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateReceived").value(hasItem(DEFAULT_DATE_RECEIVED.toString())))
            .andExpect(jsonPath("$.[*].receivedBy").value(hasItem(DEFAULT_RECEIVED_BY)));
    }
    
    @Test
    @Transactional
    public void getPeriodContributionItem() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get the periodContributionItem
        restPeriodContributionItemMockMvc.perform(get("/api/period-contribution-items/{id}", periodContributionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(periodContributionItem.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateReceived").value(DEFAULT_DATE_RECEIVED.toString()))
            .andExpect(jsonPath("$.receivedBy").value(DEFAULT_RECEIVED_BY));
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where amount equals to DEFAULT_AMOUNT
        defaultPeriodContributionItemShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the periodContributionItemList where amount equals to UPDATED_AMOUNT
        defaultPeriodContributionItemShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where amount not equals to DEFAULT_AMOUNT
        defaultPeriodContributionItemShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the periodContributionItemList where amount not equals to UPDATED_AMOUNT
        defaultPeriodContributionItemShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPeriodContributionItemShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the periodContributionItemList where amount equals to UPDATED_AMOUNT
        defaultPeriodContributionItemShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where amount is not null
        defaultPeriodContributionItemShouldBeFound("amount.specified=true");

        // Get all the periodContributionItemList where amount is null
        defaultPeriodContributionItemShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultPeriodContributionItemShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the periodContributionItemList where amount is greater than or equal to UPDATED_AMOUNT
        defaultPeriodContributionItemShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where amount is less than or equal to DEFAULT_AMOUNT
        defaultPeriodContributionItemShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the periodContributionItemList where amount is less than or equal to SMALLER_AMOUNT
        defaultPeriodContributionItemShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where amount is less than DEFAULT_AMOUNT
        defaultPeriodContributionItemShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the periodContributionItemList where amount is less than UPDATED_AMOUNT
        defaultPeriodContributionItemShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where amount is greater than DEFAULT_AMOUNT
        defaultPeriodContributionItemShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the periodContributionItemList where amount is greater than SMALLER_AMOUNT
        defaultPeriodContributionItemShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where description equals to DEFAULT_DESCRIPTION
        defaultPeriodContributionItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the periodContributionItemList where description equals to UPDATED_DESCRIPTION
        defaultPeriodContributionItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where description not equals to DEFAULT_DESCRIPTION
        defaultPeriodContributionItemShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the periodContributionItemList where description not equals to UPDATED_DESCRIPTION
        defaultPeriodContributionItemShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPeriodContributionItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the periodContributionItemList where description equals to UPDATED_DESCRIPTION
        defaultPeriodContributionItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where description is not null
        defaultPeriodContributionItemShouldBeFound("description.specified=true");

        // Get all the periodContributionItemList where description is null
        defaultPeriodContributionItemShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeriodContributionItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where description contains DEFAULT_DESCRIPTION
        defaultPeriodContributionItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the periodContributionItemList where description contains UPDATED_DESCRIPTION
        defaultPeriodContributionItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where description does not contain DEFAULT_DESCRIPTION
        defaultPeriodContributionItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the periodContributionItemList where description does not contain UPDATED_DESCRIPTION
        defaultPeriodContributionItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDateReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where dateReceived equals to DEFAULT_DATE_RECEIVED
        defaultPeriodContributionItemShouldBeFound("dateReceived.equals=" + DEFAULT_DATE_RECEIVED);

        // Get all the periodContributionItemList where dateReceived equals to UPDATED_DATE_RECEIVED
        defaultPeriodContributionItemShouldNotBeFound("dateReceived.equals=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDateReceivedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where dateReceived not equals to DEFAULT_DATE_RECEIVED
        defaultPeriodContributionItemShouldNotBeFound("dateReceived.notEquals=" + DEFAULT_DATE_RECEIVED);

        // Get all the periodContributionItemList where dateReceived not equals to UPDATED_DATE_RECEIVED
        defaultPeriodContributionItemShouldBeFound("dateReceived.notEquals=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDateReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where dateReceived in DEFAULT_DATE_RECEIVED or UPDATED_DATE_RECEIVED
        defaultPeriodContributionItemShouldBeFound("dateReceived.in=" + DEFAULT_DATE_RECEIVED + "," + UPDATED_DATE_RECEIVED);

        // Get all the periodContributionItemList where dateReceived equals to UPDATED_DATE_RECEIVED
        defaultPeriodContributionItemShouldNotBeFound("dateReceived.in=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDateReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where dateReceived is not null
        defaultPeriodContributionItemShouldBeFound("dateReceived.specified=true");

        // Get all the periodContributionItemList where dateReceived is null
        defaultPeriodContributionItemShouldNotBeFound("dateReceived.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDateReceivedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where dateReceived is greater than or equal to DEFAULT_DATE_RECEIVED
        defaultPeriodContributionItemShouldBeFound("dateReceived.greaterThanOrEqual=" + DEFAULT_DATE_RECEIVED);

        // Get all the periodContributionItemList where dateReceived is greater than or equal to UPDATED_DATE_RECEIVED
        defaultPeriodContributionItemShouldNotBeFound("dateReceived.greaterThanOrEqual=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDateReceivedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where dateReceived is less than or equal to DEFAULT_DATE_RECEIVED
        defaultPeriodContributionItemShouldBeFound("dateReceived.lessThanOrEqual=" + DEFAULT_DATE_RECEIVED);

        // Get all the periodContributionItemList where dateReceived is less than or equal to SMALLER_DATE_RECEIVED
        defaultPeriodContributionItemShouldNotBeFound("dateReceived.lessThanOrEqual=" + SMALLER_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDateReceivedIsLessThanSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where dateReceived is less than DEFAULT_DATE_RECEIVED
        defaultPeriodContributionItemShouldNotBeFound("dateReceived.lessThan=" + DEFAULT_DATE_RECEIVED);

        // Get all the periodContributionItemList where dateReceived is less than UPDATED_DATE_RECEIVED
        defaultPeriodContributionItemShouldBeFound("dateReceived.lessThan=" + UPDATED_DATE_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByDateReceivedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where dateReceived is greater than DEFAULT_DATE_RECEIVED
        defaultPeriodContributionItemShouldNotBeFound("dateReceived.greaterThan=" + DEFAULT_DATE_RECEIVED);

        // Get all the periodContributionItemList where dateReceived is greater than SMALLER_DATE_RECEIVED
        defaultPeriodContributionItemShouldBeFound("dateReceived.greaterThan=" + SMALLER_DATE_RECEIVED);
    }


    @Test
    @Transactional
    public void getAllPeriodContributionItemsByReceivedByIsEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where receivedBy equals to DEFAULT_RECEIVED_BY
        defaultPeriodContributionItemShouldBeFound("receivedBy.equals=" + DEFAULT_RECEIVED_BY);

        // Get all the periodContributionItemList where receivedBy equals to UPDATED_RECEIVED_BY
        defaultPeriodContributionItemShouldNotBeFound("receivedBy.equals=" + UPDATED_RECEIVED_BY);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByReceivedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where receivedBy not equals to DEFAULT_RECEIVED_BY
        defaultPeriodContributionItemShouldNotBeFound("receivedBy.notEquals=" + DEFAULT_RECEIVED_BY);

        // Get all the periodContributionItemList where receivedBy not equals to UPDATED_RECEIVED_BY
        defaultPeriodContributionItemShouldBeFound("receivedBy.notEquals=" + UPDATED_RECEIVED_BY);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByReceivedByIsInShouldWork() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where receivedBy in DEFAULT_RECEIVED_BY or UPDATED_RECEIVED_BY
        defaultPeriodContributionItemShouldBeFound("receivedBy.in=" + DEFAULT_RECEIVED_BY + "," + UPDATED_RECEIVED_BY);

        // Get all the periodContributionItemList where receivedBy equals to UPDATED_RECEIVED_BY
        defaultPeriodContributionItemShouldNotBeFound("receivedBy.in=" + UPDATED_RECEIVED_BY);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByReceivedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where receivedBy is not null
        defaultPeriodContributionItemShouldBeFound("receivedBy.specified=true");

        // Get all the periodContributionItemList where receivedBy is null
        defaultPeriodContributionItemShouldNotBeFound("receivedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeriodContributionItemsByReceivedByContainsSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where receivedBy contains DEFAULT_RECEIVED_BY
        defaultPeriodContributionItemShouldBeFound("receivedBy.contains=" + DEFAULT_RECEIVED_BY);

        // Get all the periodContributionItemList where receivedBy contains UPDATED_RECEIVED_BY
        defaultPeriodContributionItemShouldNotBeFound("receivedBy.contains=" + UPDATED_RECEIVED_BY);
    }

    @Test
    @Transactional
    public void getAllPeriodContributionItemsByReceivedByNotContainsSomething() throws Exception {
        // Initialize the database
        periodContributionItemRepository.saveAndFlush(periodContributionItem);

        // Get all the periodContributionItemList where receivedBy does not contain DEFAULT_RECEIVED_BY
        defaultPeriodContributionItemShouldNotBeFound("receivedBy.doesNotContain=" + DEFAULT_RECEIVED_BY);

        // Get all the periodContributionItemList where receivedBy does not contain UPDATED_RECEIVED_BY
        defaultPeriodContributionItemShouldBeFound("receivedBy.doesNotContain=" + UPDATED_RECEIVED_BY);
    }


    @Test
    @Transactional
    public void getAllPeriodContributionItemsByPeriodContributionIsEqualToSomething() throws Exception {
        // Get already existing entity
        PeriodContribution periodContribution = periodContributionItem.getPeriodContribution();
        periodContributionItemRepository.saveAndFlush(periodContributionItem);
        Long periodContributionId = periodContribution.getId();

        // Get all the periodContributionItemList where periodContribution equals to periodContributionId
        defaultPeriodContributionItemShouldBeFound("periodContributionId.equals=" + periodContributionId);

        // Get all the periodContributionItemList where periodContribution equals to periodContributionId + 1
        defaultPeriodContributionItemShouldNotBeFound("periodContributionId.equals=" + (periodContributionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPeriodContributionItemShouldBeFound(String filter) throws Exception {
        restPeriodContributionItemMockMvc.perform(get("/api/period-contribution-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodContributionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateReceived").value(hasItem(DEFAULT_DATE_RECEIVED.toString())))
            .andExpect(jsonPath("$.[*].receivedBy").value(hasItem(DEFAULT_RECEIVED_BY)));

        // Check, that the count call also returns 1
        restPeriodContributionItemMockMvc.perform(get("/api/period-contribution-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPeriodContributionItemShouldNotBeFound(String filter) throws Exception {
        restPeriodContributionItemMockMvc.perform(get("/api/period-contribution-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPeriodContributionItemMockMvc.perform(get("/api/period-contribution-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPeriodContributionItem() throws Exception {
        // Get the periodContributionItem
        restPeriodContributionItemMockMvc.perform(get("/api/period-contribution-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriodContributionItem() throws Exception {
        // Initialize the database
        periodContributionItemService.save(periodContributionItem);

        int databaseSizeBeforeUpdate = periodContributionItemRepository.findAll().size();

        // Update the periodContributionItem
        PeriodContributionItem updatedPeriodContributionItem = periodContributionItemRepository.findById(periodContributionItem.getId()).get();
        // Disconnect from session so that the updates on updatedPeriodContributionItem are not directly saved in db
        em.detach(updatedPeriodContributionItem);
        updatedPeriodContributionItem
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .dateReceived(UPDATED_DATE_RECEIVED)
            .receivedBy(UPDATED_RECEIVED_BY);

        restPeriodContributionItemMockMvc.perform(put("/api/period-contribution-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeriodContributionItem)))
            .andExpect(status().isOk());

        // Validate the PeriodContributionItem in the database
        List<PeriodContributionItem> periodContributionItemList = periodContributionItemRepository.findAll();
        assertThat(periodContributionItemList).hasSize(databaseSizeBeforeUpdate);
        PeriodContributionItem testPeriodContributionItem = periodContributionItemList.get(periodContributionItemList.size() - 1);
        assertThat(testPeriodContributionItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPeriodContributionItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPeriodContributionItem.getDateReceived()).isEqualTo(UPDATED_DATE_RECEIVED);
        assertThat(testPeriodContributionItem.getReceivedBy()).isEqualTo(UPDATED_RECEIVED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingPeriodContributionItem() throws Exception {
        int databaseSizeBeforeUpdate = periodContributionItemRepository.findAll().size();

        // Create the PeriodContributionItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodContributionItemMockMvc.perform(put("/api/period-contribution-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(periodContributionItem)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodContributionItem in the database
        List<PeriodContributionItem> periodContributionItemList = periodContributionItemRepository.findAll();
        assertThat(periodContributionItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePeriodContributionItem() throws Exception {
        // Initialize the database
        periodContributionItemService.save(periodContributionItem);

        int databaseSizeBeforeDelete = periodContributionItemRepository.findAll().size();

        // Delete the periodContributionItem
        restPeriodContributionItemMockMvc.perform(delete("/api/period-contribution-items/{id}", periodContributionItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PeriodContributionItem> periodContributionItemList = periodContributionItemRepository.findAll();
        assertThat(periodContributionItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodContributionItem.class);
        PeriodContributionItem periodContributionItem1 = new PeriodContributionItem();
        periodContributionItem1.setId(1L);
        PeriodContributionItem periodContributionItem2 = new PeriodContributionItem();
        periodContributionItem2.setId(periodContributionItem1.getId());
        assertThat(periodContributionItem1).isEqualTo(periodContributionItem2);
        periodContributionItem2.setId(2L);
        assertThat(periodContributionItem1).isNotEqualTo(periodContributionItem2);
        periodContributionItem1.setId(null);
        assertThat(periodContributionItem1).isNotEqualTo(periodContributionItem2);
    }
}
