package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.MemberPromise;
import org.church.domain.Member;
import org.church.domain.ChurchActivity;
import org.church.repository.MemberPromiseRepository;
import org.church.service.MemberPromiseService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.MemberPromiseCriteria;
import org.church.service.MemberPromiseQueryService;

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
 * Integration tests for the {@link MemberPromiseResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class MemberPromiseResourceIT {

    private static final LocalDate DEFAULT_PROMISE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROMISE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PROMISE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_OTHER_PROMISE = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_PROMISE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FULFILLMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FULFILLMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FULFILLMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_FULFILLED = false;
    private static final Boolean UPDATED_IS_FULFILLED = true;

    @Autowired
    private MemberPromiseRepository memberPromiseRepository;

    @Autowired
    private MemberPromiseService memberPromiseService;

    @Autowired
    private MemberPromiseQueryService memberPromiseQueryService;

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

    private MockMvc restMemberPromiseMockMvc;

    private MemberPromise memberPromise;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MemberPromiseResource memberPromiseResource = new MemberPromiseResource(memberPromiseService, memberPromiseQueryService);
        this.restMemberPromiseMockMvc = MockMvcBuilders.standaloneSetup(memberPromiseResource)
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
    public static MemberPromise createEntity(EntityManager em) {
        MemberPromise memberPromise = new MemberPromise()
            .promiseDate(DEFAULT_PROMISE_DATE)
            .amount(DEFAULT_AMOUNT)
            .otherPromise(DEFAULT_OTHER_PROMISE)
            .fulfillmentDate(DEFAULT_FULFILLMENT_DATE)
            .isFulfilled(DEFAULT_IS_FULFILLED);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceIT.createEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        memberPromise.setMember(member);
        // Add required entity
        ChurchActivity churchActivity;
        if (TestUtil.findAll(em, ChurchActivity.class).isEmpty()) {
            churchActivity = ChurchActivityResourceIT.createEntity(em);
            em.persist(churchActivity);
            em.flush();
        } else {
            churchActivity = TestUtil.findAll(em, ChurchActivity.class).get(0);
        }
        memberPromise.setChurchActivity(churchActivity);
        return memberPromise;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberPromise createUpdatedEntity(EntityManager em) {
        MemberPromise memberPromise = new MemberPromise()
            .promiseDate(UPDATED_PROMISE_DATE)
            .amount(UPDATED_AMOUNT)
            .otherPromise(UPDATED_OTHER_PROMISE)
            .fulfillmentDate(UPDATED_FULFILLMENT_DATE)
            .isFulfilled(UPDATED_IS_FULFILLED);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceIT.createUpdatedEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        memberPromise.setMember(member);
        // Add required entity
        ChurchActivity churchActivity;
        if (TestUtil.findAll(em, ChurchActivity.class).isEmpty()) {
            churchActivity = ChurchActivityResourceIT.createUpdatedEntity(em);
            em.persist(churchActivity);
            em.flush();
        } else {
            churchActivity = TestUtil.findAll(em, ChurchActivity.class).get(0);
        }
        memberPromise.setChurchActivity(churchActivity);
        return memberPromise;
    }

    @BeforeEach
    public void initTest() {
        memberPromise = createEntity(em);
    }

    @Test
    @Transactional
    public void createMemberPromise() throws Exception {
        int databaseSizeBeforeCreate = memberPromiseRepository.findAll().size();

        // Create the MemberPromise
        restMemberPromiseMockMvc.perform(post("/api/member-promises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberPromise)))
            .andExpect(status().isCreated());

        // Validate the MemberPromise in the database
        List<MemberPromise> memberPromiseList = memberPromiseRepository.findAll();
        assertThat(memberPromiseList).hasSize(databaseSizeBeforeCreate + 1);
        MemberPromise testMemberPromise = memberPromiseList.get(memberPromiseList.size() - 1);
        assertThat(testMemberPromise.getPromiseDate()).isEqualTo(DEFAULT_PROMISE_DATE);
        assertThat(testMemberPromise.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testMemberPromise.getOtherPromise()).isEqualTo(DEFAULT_OTHER_PROMISE);
        assertThat(testMemberPromise.getFulfillmentDate()).isEqualTo(DEFAULT_FULFILLMENT_DATE);
        assertThat(testMemberPromise.isIsFulfilled()).isEqualTo(DEFAULT_IS_FULFILLED);
    }

    @Test
    @Transactional
    public void createMemberPromiseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberPromiseRepository.findAll().size();

        // Create the MemberPromise with an existing ID
        memberPromise.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberPromiseMockMvc.perform(post("/api/member-promises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberPromise)))
            .andExpect(status().isBadRequest());

        // Validate the MemberPromise in the database
        List<MemberPromise> memberPromiseList = memberPromiseRepository.findAll();
        assertThat(memberPromiseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPromiseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberPromiseRepository.findAll().size();
        // set the field null
        memberPromise.setPromiseDate(null);

        // Create the MemberPromise, which fails.

        restMemberPromiseMockMvc.perform(post("/api/member-promises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberPromise)))
            .andExpect(status().isBadRequest());

        List<MemberPromise> memberPromiseList = memberPromiseRepository.findAll();
        assertThat(memberPromiseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberPromises() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList
        restMemberPromiseMockMvc.perform(get("/api/member-promises?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberPromise.getId().intValue())))
            .andExpect(jsonPath("$.[*].promiseDate").value(hasItem(DEFAULT_PROMISE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].otherPromise").value(hasItem(DEFAULT_OTHER_PROMISE)))
            .andExpect(jsonPath("$.[*].fulfillmentDate").value(hasItem(DEFAULT_FULFILLMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isFulfilled").value(hasItem(DEFAULT_IS_FULFILLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMemberPromise() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get the memberPromise
        restMemberPromiseMockMvc.perform(get("/api/member-promises/{id}", memberPromise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(memberPromise.getId().intValue()))
            .andExpect(jsonPath("$.promiseDate").value(DEFAULT_PROMISE_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.otherPromise").value(DEFAULT_OTHER_PROMISE))
            .andExpect(jsonPath("$.fulfillmentDate").value(DEFAULT_FULFILLMENT_DATE.toString()))
            .andExpect(jsonPath("$.isFulfilled").value(DEFAULT_IS_FULFILLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByPromiseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where promiseDate equals to DEFAULT_PROMISE_DATE
        defaultMemberPromiseShouldBeFound("promiseDate.equals=" + DEFAULT_PROMISE_DATE);

        // Get all the memberPromiseList where promiseDate equals to UPDATED_PROMISE_DATE
        defaultMemberPromiseShouldNotBeFound("promiseDate.equals=" + UPDATED_PROMISE_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByPromiseDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where promiseDate not equals to DEFAULT_PROMISE_DATE
        defaultMemberPromiseShouldNotBeFound("promiseDate.notEquals=" + DEFAULT_PROMISE_DATE);

        // Get all the memberPromiseList where promiseDate not equals to UPDATED_PROMISE_DATE
        defaultMemberPromiseShouldBeFound("promiseDate.notEquals=" + UPDATED_PROMISE_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByPromiseDateIsInShouldWork() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where promiseDate in DEFAULT_PROMISE_DATE or UPDATED_PROMISE_DATE
        defaultMemberPromiseShouldBeFound("promiseDate.in=" + DEFAULT_PROMISE_DATE + "," + UPDATED_PROMISE_DATE);

        // Get all the memberPromiseList where promiseDate equals to UPDATED_PROMISE_DATE
        defaultMemberPromiseShouldNotBeFound("promiseDate.in=" + UPDATED_PROMISE_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByPromiseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where promiseDate is not null
        defaultMemberPromiseShouldBeFound("promiseDate.specified=true");

        // Get all the memberPromiseList where promiseDate is null
        defaultMemberPromiseShouldNotBeFound("promiseDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByPromiseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where promiseDate is greater than or equal to DEFAULT_PROMISE_DATE
        defaultMemberPromiseShouldBeFound("promiseDate.greaterThanOrEqual=" + DEFAULT_PROMISE_DATE);

        // Get all the memberPromiseList where promiseDate is greater than or equal to UPDATED_PROMISE_DATE
        defaultMemberPromiseShouldNotBeFound("promiseDate.greaterThanOrEqual=" + UPDATED_PROMISE_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByPromiseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where promiseDate is less than or equal to DEFAULT_PROMISE_DATE
        defaultMemberPromiseShouldBeFound("promiseDate.lessThanOrEqual=" + DEFAULT_PROMISE_DATE);

        // Get all the memberPromiseList where promiseDate is less than or equal to SMALLER_PROMISE_DATE
        defaultMemberPromiseShouldNotBeFound("promiseDate.lessThanOrEqual=" + SMALLER_PROMISE_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByPromiseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where promiseDate is less than DEFAULT_PROMISE_DATE
        defaultMemberPromiseShouldNotBeFound("promiseDate.lessThan=" + DEFAULT_PROMISE_DATE);

        // Get all the memberPromiseList where promiseDate is less than UPDATED_PROMISE_DATE
        defaultMemberPromiseShouldBeFound("promiseDate.lessThan=" + UPDATED_PROMISE_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByPromiseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where promiseDate is greater than DEFAULT_PROMISE_DATE
        defaultMemberPromiseShouldNotBeFound("promiseDate.greaterThan=" + DEFAULT_PROMISE_DATE);

        // Get all the memberPromiseList where promiseDate is greater than SMALLER_PROMISE_DATE
        defaultMemberPromiseShouldBeFound("promiseDate.greaterThan=" + SMALLER_PROMISE_DATE);
    }


    @Test
    @Transactional
    public void getAllMemberPromisesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where amount equals to DEFAULT_AMOUNT
        defaultMemberPromiseShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the memberPromiseList where amount equals to UPDATED_AMOUNT
        defaultMemberPromiseShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where amount not equals to DEFAULT_AMOUNT
        defaultMemberPromiseShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the memberPromiseList where amount not equals to UPDATED_AMOUNT
        defaultMemberPromiseShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultMemberPromiseShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the memberPromiseList where amount equals to UPDATED_AMOUNT
        defaultMemberPromiseShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where amount is not null
        defaultMemberPromiseShouldBeFound("amount.specified=true");

        // Get all the memberPromiseList where amount is null
        defaultMemberPromiseShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultMemberPromiseShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the memberPromiseList where amount is greater than or equal to UPDATED_AMOUNT
        defaultMemberPromiseShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where amount is less than or equal to DEFAULT_AMOUNT
        defaultMemberPromiseShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the memberPromiseList where amount is less than or equal to SMALLER_AMOUNT
        defaultMemberPromiseShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where amount is less than DEFAULT_AMOUNT
        defaultMemberPromiseShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the memberPromiseList where amount is less than UPDATED_AMOUNT
        defaultMemberPromiseShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where amount is greater than DEFAULT_AMOUNT
        defaultMemberPromiseShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the memberPromiseList where amount is greater than SMALLER_AMOUNT
        defaultMemberPromiseShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllMemberPromisesByOtherPromiseIsEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where otherPromise equals to DEFAULT_OTHER_PROMISE
        defaultMemberPromiseShouldBeFound("otherPromise.equals=" + DEFAULT_OTHER_PROMISE);

        // Get all the memberPromiseList where otherPromise equals to UPDATED_OTHER_PROMISE
        defaultMemberPromiseShouldNotBeFound("otherPromise.equals=" + UPDATED_OTHER_PROMISE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByOtherPromiseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where otherPromise not equals to DEFAULT_OTHER_PROMISE
        defaultMemberPromiseShouldNotBeFound("otherPromise.notEquals=" + DEFAULT_OTHER_PROMISE);

        // Get all the memberPromiseList where otherPromise not equals to UPDATED_OTHER_PROMISE
        defaultMemberPromiseShouldBeFound("otherPromise.notEquals=" + UPDATED_OTHER_PROMISE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByOtherPromiseIsInShouldWork() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where otherPromise in DEFAULT_OTHER_PROMISE or UPDATED_OTHER_PROMISE
        defaultMemberPromiseShouldBeFound("otherPromise.in=" + DEFAULT_OTHER_PROMISE + "," + UPDATED_OTHER_PROMISE);

        // Get all the memberPromiseList where otherPromise equals to UPDATED_OTHER_PROMISE
        defaultMemberPromiseShouldNotBeFound("otherPromise.in=" + UPDATED_OTHER_PROMISE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByOtherPromiseIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where otherPromise is not null
        defaultMemberPromiseShouldBeFound("otherPromise.specified=true");

        // Get all the memberPromiseList where otherPromise is null
        defaultMemberPromiseShouldNotBeFound("otherPromise.specified=false");
    }
                @Test
    @Transactional
    public void getAllMemberPromisesByOtherPromiseContainsSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where otherPromise contains DEFAULT_OTHER_PROMISE
        defaultMemberPromiseShouldBeFound("otherPromise.contains=" + DEFAULT_OTHER_PROMISE);

        // Get all the memberPromiseList where otherPromise contains UPDATED_OTHER_PROMISE
        defaultMemberPromiseShouldNotBeFound("otherPromise.contains=" + UPDATED_OTHER_PROMISE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByOtherPromiseNotContainsSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where otherPromise does not contain DEFAULT_OTHER_PROMISE
        defaultMemberPromiseShouldNotBeFound("otherPromise.doesNotContain=" + DEFAULT_OTHER_PROMISE);

        // Get all the memberPromiseList where otherPromise does not contain UPDATED_OTHER_PROMISE
        defaultMemberPromiseShouldBeFound("otherPromise.doesNotContain=" + UPDATED_OTHER_PROMISE);
    }


    @Test
    @Transactional
    public void getAllMemberPromisesByFulfillmentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where fulfillmentDate equals to DEFAULT_FULFILLMENT_DATE
        defaultMemberPromiseShouldBeFound("fulfillmentDate.equals=" + DEFAULT_FULFILLMENT_DATE);

        // Get all the memberPromiseList where fulfillmentDate equals to UPDATED_FULFILLMENT_DATE
        defaultMemberPromiseShouldNotBeFound("fulfillmentDate.equals=" + UPDATED_FULFILLMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByFulfillmentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where fulfillmentDate not equals to DEFAULT_FULFILLMENT_DATE
        defaultMemberPromiseShouldNotBeFound("fulfillmentDate.notEquals=" + DEFAULT_FULFILLMENT_DATE);

        // Get all the memberPromiseList where fulfillmentDate not equals to UPDATED_FULFILLMENT_DATE
        defaultMemberPromiseShouldBeFound("fulfillmentDate.notEquals=" + UPDATED_FULFILLMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByFulfillmentDateIsInShouldWork() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where fulfillmentDate in DEFAULT_FULFILLMENT_DATE or UPDATED_FULFILLMENT_DATE
        defaultMemberPromiseShouldBeFound("fulfillmentDate.in=" + DEFAULT_FULFILLMENT_DATE + "," + UPDATED_FULFILLMENT_DATE);

        // Get all the memberPromiseList where fulfillmentDate equals to UPDATED_FULFILLMENT_DATE
        defaultMemberPromiseShouldNotBeFound("fulfillmentDate.in=" + UPDATED_FULFILLMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByFulfillmentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where fulfillmentDate is not null
        defaultMemberPromiseShouldBeFound("fulfillmentDate.specified=true");

        // Get all the memberPromiseList where fulfillmentDate is null
        defaultMemberPromiseShouldNotBeFound("fulfillmentDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByFulfillmentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where fulfillmentDate is greater than or equal to DEFAULT_FULFILLMENT_DATE
        defaultMemberPromiseShouldBeFound("fulfillmentDate.greaterThanOrEqual=" + DEFAULT_FULFILLMENT_DATE);

        // Get all the memberPromiseList where fulfillmentDate is greater than or equal to UPDATED_FULFILLMENT_DATE
        defaultMemberPromiseShouldNotBeFound("fulfillmentDate.greaterThanOrEqual=" + UPDATED_FULFILLMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByFulfillmentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where fulfillmentDate is less than or equal to DEFAULT_FULFILLMENT_DATE
        defaultMemberPromiseShouldBeFound("fulfillmentDate.lessThanOrEqual=" + DEFAULT_FULFILLMENT_DATE);

        // Get all the memberPromiseList where fulfillmentDate is less than or equal to SMALLER_FULFILLMENT_DATE
        defaultMemberPromiseShouldNotBeFound("fulfillmentDate.lessThanOrEqual=" + SMALLER_FULFILLMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByFulfillmentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where fulfillmentDate is less than DEFAULT_FULFILLMENT_DATE
        defaultMemberPromiseShouldNotBeFound("fulfillmentDate.lessThan=" + DEFAULT_FULFILLMENT_DATE);

        // Get all the memberPromiseList where fulfillmentDate is less than UPDATED_FULFILLMENT_DATE
        defaultMemberPromiseShouldBeFound("fulfillmentDate.lessThan=" + UPDATED_FULFILLMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByFulfillmentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where fulfillmentDate is greater than DEFAULT_FULFILLMENT_DATE
        defaultMemberPromiseShouldNotBeFound("fulfillmentDate.greaterThan=" + DEFAULT_FULFILLMENT_DATE);

        // Get all the memberPromiseList where fulfillmentDate is greater than SMALLER_FULFILLMENT_DATE
        defaultMemberPromiseShouldBeFound("fulfillmentDate.greaterThan=" + SMALLER_FULFILLMENT_DATE);
    }


    @Test
    @Transactional
    public void getAllMemberPromisesByIsFulfilledIsEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where isFulfilled equals to DEFAULT_IS_FULFILLED
        defaultMemberPromiseShouldBeFound("isFulfilled.equals=" + DEFAULT_IS_FULFILLED);

        // Get all the memberPromiseList where isFulfilled equals to UPDATED_IS_FULFILLED
        defaultMemberPromiseShouldNotBeFound("isFulfilled.equals=" + UPDATED_IS_FULFILLED);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByIsFulfilledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where isFulfilled not equals to DEFAULT_IS_FULFILLED
        defaultMemberPromiseShouldNotBeFound("isFulfilled.notEquals=" + DEFAULT_IS_FULFILLED);

        // Get all the memberPromiseList where isFulfilled not equals to UPDATED_IS_FULFILLED
        defaultMemberPromiseShouldBeFound("isFulfilled.notEquals=" + UPDATED_IS_FULFILLED);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByIsFulfilledIsInShouldWork() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where isFulfilled in DEFAULT_IS_FULFILLED or UPDATED_IS_FULFILLED
        defaultMemberPromiseShouldBeFound("isFulfilled.in=" + DEFAULT_IS_FULFILLED + "," + UPDATED_IS_FULFILLED);

        // Get all the memberPromiseList where isFulfilled equals to UPDATED_IS_FULFILLED
        defaultMemberPromiseShouldNotBeFound("isFulfilled.in=" + UPDATED_IS_FULFILLED);
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByIsFulfilledIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberPromiseRepository.saveAndFlush(memberPromise);

        // Get all the memberPromiseList where isFulfilled is not null
        defaultMemberPromiseShouldBeFound("isFulfilled.specified=true");

        // Get all the memberPromiseList where isFulfilled is null
        defaultMemberPromiseShouldNotBeFound("isFulfilled.specified=false");
    }

    @Test
    @Transactional
    public void getAllMemberPromisesByMemberIsEqualToSomething() throws Exception {
        // Get already existing entity
        Member member = memberPromise.getMember();
        memberPromiseRepository.saveAndFlush(memberPromise);
        Long memberId = member.getId();

        // Get all the memberPromiseList where member equals to memberId
        defaultMemberPromiseShouldBeFound("memberId.equals=" + memberId);

        // Get all the memberPromiseList where member equals to memberId + 1
        defaultMemberPromiseShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }


    @Test
    @Transactional
    public void getAllMemberPromisesByChurchActivityIsEqualToSomething() throws Exception {
        // Get already existing entity
        ChurchActivity churchActivity = memberPromise.getChurchActivity();
        memberPromiseRepository.saveAndFlush(memberPromise);
        Long churchActivityId = churchActivity.getId();

        // Get all the memberPromiseList where churchActivity equals to churchActivityId
        defaultMemberPromiseShouldBeFound("churchActivityId.equals=" + churchActivityId);

        // Get all the memberPromiseList where churchActivity equals to churchActivityId + 1
        defaultMemberPromiseShouldNotBeFound("churchActivityId.equals=" + (churchActivityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemberPromiseShouldBeFound(String filter) throws Exception {
        restMemberPromiseMockMvc.perform(get("/api/member-promises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberPromise.getId().intValue())))
            .andExpect(jsonPath("$.[*].promiseDate").value(hasItem(DEFAULT_PROMISE_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].otherPromise").value(hasItem(DEFAULT_OTHER_PROMISE)))
            .andExpect(jsonPath("$.[*].fulfillmentDate").value(hasItem(DEFAULT_FULFILLMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isFulfilled").value(hasItem(DEFAULT_IS_FULFILLED.booleanValue())));

        // Check, that the count call also returns 1
        restMemberPromiseMockMvc.perform(get("/api/member-promises/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemberPromiseShouldNotBeFound(String filter) throws Exception {
        restMemberPromiseMockMvc.perform(get("/api/member-promises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemberPromiseMockMvc.perform(get("/api/member-promises/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMemberPromise() throws Exception {
        // Get the memberPromise
        restMemberPromiseMockMvc.perform(get("/api/member-promises/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberPromise() throws Exception {
        // Initialize the database
        memberPromiseService.save(memberPromise);

        int databaseSizeBeforeUpdate = memberPromiseRepository.findAll().size();

        // Update the memberPromise
        MemberPromise updatedMemberPromise = memberPromiseRepository.findById(memberPromise.getId()).get();
        // Disconnect from session so that the updates on updatedMemberPromise are not directly saved in db
        em.detach(updatedMemberPromise);
        updatedMemberPromise
            .promiseDate(UPDATED_PROMISE_DATE)
            .amount(UPDATED_AMOUNT)
            .otherPromise(UPDATED_OTHER_PROMISE)
            .fulfillmentDate(UPDATED_FULFILLMENT_DATE)
            .isFulfilled(UPDATED_IS_FULFILLED);

        restMemberPromiseMockMvc.perform(put("/api/member-promises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMemberPromise)))
            .andExpect(status().isOk());

        // Validate the MemberPromise in the database
        List<MemberPromise> memberPromiseList = memberPromiseRepository.findAll();
        assertThat(memberPromiseList).hasSize(databaseSizeBeforeUpdate);
        MemberPromise testMemberPromise = memberPromiseList.get(memberPromiseList.size() - 1);
        assertThat(testMemberPromise.getPromiseDate()).isEqualTo(UPDATED_PROMISE_DATE);
        assertThat(testMemberPromise.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMemberPromise.getOtherPromise()).isEqualTo(UPDATED_OTHER_PROMISE);
        assertThat(testMemberPromise.getFulfillmentDate()).isEqualTo(UPDATED_FULFILLMENT_DATE);
        assertThat(testMemberPromise.isIsFulfilled()).isEqualTo(UPDATED_IS_FULFILLED);
    }

    @Test
    @Transactional
    public void updateNonExistingMemberPromise() throws Exception {
        int databaseSizeBeforeUpdate = memberPromiseRepository.findAll().size();

        // Create the MemberPromise

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberPromiseMockMvc.perform(put("/api/member-promises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberPromise)))
            .andExpect(status().isBadRequest());

        // Validate the MemberPromise in the database
        List<MemberPromise> memberPromiseList = memberPromiseRepository.findAll();
        assertThat(memberPromiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMemberPromise() throws Exception {
        // Initialize the database
        memberPromiseService.save(memberPromise);

        int databaseSizeBeforeDelete = memberPromiseRepository.findAll().size();

        // Delete the memberPromise
        restMemberPromiseMockMvc.perform(delete("/api/member-promises/{id}", memberPromise.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MemberPromise> memberPromiseList = memberPromiseRepository.findAll();
        assertThat(memberPromiseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberPromise.class);
        MemberPromise memberPromise1 = new MemberPromise();
        memberPromise1.setId(1L);
        MemberPromise memberPromise2 = new MemberPromise();
        memberPromise2.setId(memberPromise1.getId());
        assertThat(memberPromise1).isEqualTo(memberPromise2);
        memberPromise2.setId(2L);
        assertThat(memberPromise1).isNotEqualTo(memberPromise2);
        memberPromise1.setId(null);
        assertThat(memberPromise1).isNotEqualTo(memberPromise2);
    }
}
