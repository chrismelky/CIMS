package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.MemberContribution;
import org.church.domain.Member;
import org.church.domain.Church;
import org.church.domain.PaymentMethod;
import org.church.domain.MemberPromise;
import org.church.domain.ContributionType;
import org.church.repository.MemberContributionRepository;
import org.church.service.MemberContributionService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.MemberContributionCriteria;
import org.church.service.MemberContributionQueryService;

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
 * Integration tests for the {@link MemberContributionResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class MemberContributionResourceIT {

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAYMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    @Autowired
    private MemberContributionRepository memberContributionRepository;

    @Autowired
    private MemberContributionService memberContributionService;

    @Autowired
    private MemberContributionQueryService memberContributionQueryService;

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

    private MockMvc restMemberContributionMockMvc;

    private MemberContribution memberContribution;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MemberContributionResource memberContributionResource = new MemberContributionResource(memberContributionService, memberContributionQueryService);
        this.restMemberContributionMockMvc = MockMvcBuilders.standaloneSetup(memberContributionResource)
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
    public static MemberContribution createEntity(EntityManager em) {
        MemberContribution memberContribution = new MemberContribution()
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .amount(DEFAULT_AMOUNT);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceIT.createEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        memberContribution.setMember(member);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        memberContribution.setChurch(church);
        // Add required entity
        PaymentMethod paymentMethod;
        if (TestUtil.findAll(em, PaymentMethod.class).isEmpty()) {
            paymentMethod = PaymentMethodResourceIT.createEntity(em);
            em.persist(paymentMethod);
            em.flush();
        } else {
            paymentMethod = TestUtil.findAll(em, PaymentMethod.class).get(0);
        }
        memberContribution.setPaymentMethod(paymentMethod);
        return memberContribution;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberContribution createUpdatedEntity(EntityManager em) {
        MemberContribution memberContribution = new MemberContribution()
            .paymentDate(UPDATED_PAYMENT_DATE)
            .amount(UPDATED_AMOUNT);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceIT.createUpdatedEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        memberContribution.setMember(member);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createUpdatedEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        memberContribution.setChurch(church);
        // Add required entity
        PaymentMethod paymentMethod;
        if (TestUtil.findAll(em, PaymentMethod.class).isEmpty()) {
            paymentMethod = PaymentMethodResourceIT.createUpdatedEntity(em);
            em.persist(paymentMethod);
            em.flush();
        } else {
            paymentMethod = TestUtil.findAll(em, PaymentMethod.class).get(0);
        }
        memberContribution.setPaymentMethod(paymentMethod);
        return memberContribution;
    }

    @BeforeEach
    public void initTest() {
        memberContribution = createEntity(em);
    }

    @Test
    @Transactional
    public void createMemberContribution() throws Exception {
        int databaseSizeBeforeCreate = memberContributionRepository.findAll().size();

        // Create the MemberContribution
        restMemberContributionMockMvc.perform(post("/api/member-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberContribution)))
            .andExpect(status().isCreated());

        // Validate the MemberContribution in the database
        List<MemberContribution> memberContributionList = memberContributionRepository.findAll();
        assertThat(memberContributionList).hasSize(databaseSizeBeforeCreate + 1);
        MemberContribution testMemberContribution = memberContributionList.get(memberContributionList.size() - 1);
        assertThat(testMemberContribution.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testMemberContribution.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createMemberContributionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberContributionRepository.findAll().size();

        // Create the MemberContribution with an existing ID
        memberContribution.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberContributionMockMvc.perform(post("/api/member-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberContribution)))
            .andExpect(status().isBadRequest());

        // Validate the MemberContribution in the database
        List<MemberContribution> memberContributionList = memberContributionRepository.findAll();
        assertThat(memberContributionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberContributionRepository.findAll().size();
        // set the field null
        memberContribution.setAmount(null);

        // Create the MemberContribution, which fails.

        restMemberContributionMockMvc.perform(post("/api/member-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberContribution)))
            .andExpect(status().isBadRequest());

        List<MemberContribution> memberContributionList = memberContributionRepository.findAll();
        assertThat(memberContributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberContributions() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList
        restMemberContributionMockMvc.perform(get("/api/member-contributions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberContribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getMemberContribution() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get the memberContribution
        restMemberContributionMockMvc.perform(get("/api/member-contributions/{id}", memberContribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(memberContribution.getId().intValue()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultMemberContributionShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the memberContributionList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultMemberContributionShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByPaymentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where paymentDate not equals to DEFAULT_PAYMENT_DATE
        defaultMemberContributionShouldNotBeFound("paymentDate.notEquals=" + DEFAULT_PAYMENT_DATE);

        // Get all the memberContributionList where paymentDate not equals to UPDATED_PAYMENT_DATE
        defaultMemberContributionShouldBeFound("paymentDate.notEquals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultMemberContributionShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the memberContributionList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultMemberContributionShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where paymentDate is not null
        defaultMemberContributionShouldBeFound("paymentDate.specified=true");

        // Get all the memberContributionList where paymentDate is null
        defaultMemberContributionShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where paymentDate is greater than or equal to DEFAULT_PAYMENT_DATE
        defaultMemberContributionShouldBeFound("paymentDate.greaterThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the memberContributionList where paymentDate is greater than or equal to UPDATED_PAYMENT_DATE
        defaultMemberContributionShouldNotBeFound("paymentDate.greaterThanOrEqual=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where paymentDate is less than or equal to DEFAULT_PAYMENT_DATE
        defaultMemberContributionShouldBeFound("paymentDate.lessThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the memberContributionList where paymentDate is less than or equal to SMALLER_PAYMENT_DATE
        defaultMemberContributionShouldNotBeFound("paymentDate.lessThanOrEqual=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where paymentDate is less than DEFAULT_PAYMENT_DATE
        defaultMemberContributionShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the memberContributionList where paymentDate is less than UPDATED_PAYMENT_DATE
        defaultMemberContributionShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where paymentDate is greater than DEFAULT_PAYMENT_DATE
        defaultMemberContributionShouldNotBeFound("paymentDate.greaterThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the memberContributionList where paymentDate is greater than SMALLER_PAYMENT_DATE
        defaultMemberContributionShouldBeFound("paymentDate.greaterThan=" + SMALLER_PAYMENT_DATE);
    }


    @Test
    @Transactional
    public void getAllMemberContributionsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where amount equals to DEFAULT_AMOUNT
        defaultMemberContributionShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the memberContributionList where amount equals to UPDATED_AMOUNT
        defaultMemberContributionShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where amount not equals to DEFAULT_AMOUNT
        defaultMemberContributionShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the memberContributionList where amount not equals to UPDATED_AMOUNT
        defaultMemberContributionShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultMemberContributionShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the memberContributionList where amount equals to UPDATED_AMOUNT
        defaultMemberContributionShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where amount is not null
        defaultMemberContributionShouldBeFound("amount.specified=true");

        // Get all the memberContributionList where amount is null
        defaultMemberContributionShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultMemberContributionShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the memberContributionList where amount is greater than or equal to UPDATED_AMOUNT
        defaultMemberContributionShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where amount is less than or equal to DEFAULT_AMOUNT
        defaultMemberContributionShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the memberContributionList where amount is less than or equal to SMALLER_AMOUNT
        defaultMemberContributionShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where amount is less than DEFAULT_AMOUNT
        defaultMemberContributionShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the memberContributionList where amount is less than UPDATED_AMOUNT
        defaultMemberContributionShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMemberContributionsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);

        // Get all the memberContributionList where amount is greater than DEFAULT_AMOUNT
        defaultMemberContributionShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the memberContributionList where amount is greater than SMALLER_AMOUNT
        defaultMemberContributionShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllMemberContributionsByMemberIsEqualToSomething() throws Exception {
        // Get already existing entity
        Member member = memberContribution.getMember();
        memberContributionRepository.saveAndFlush(memberContribution);
        Long memberId = member.getId();

        // Get all the memberContributionList where member equals to memberId
        defaultMemberContributionShouldBeFound("memberId.equals=" + memberId);

        // Get all the memberContributionList where member equals to memberId + 1
        defaultMemberContributionShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }


    @Test
    @Transactional
    public void getAllMemberContributionsByChurchIsEqualToSomething() throws Exception {
        // Get already existing entity
        Church church = memberContribution.getChurch();
        memberContributionRepository.saveAndFlush(memberContribution);
        Long churchId = church.getId();

        // Get all the memberContributionList where church equals to churchId
        defaultMemberContributionShouldBeFound("churchId.equals=" + churchId);

        // Get all the memberContributionList where church equals to churchId + 1
        defaultMemberContributionShouldNotBeFound("churchId.equals=" + (churchId + 1));
    }


    @Test
    @Transactional
    public void getAllMemberContributionsByPaymentMethodIsEqualToSomething() throws Exception {
        // Get already existing entity
        PaymentMethod paymentMethod = memberContribution.getPaymentMethod();
        memberContributionRepository.saveAndFlush(memberContribution);
        Long paymentMethodId = paymentMethod.getId();

        // Get all the memberContributionList where paymentMethod equals to paymentMethodId
        defaultMemberContributionShouldBeFound("paymentMethodId.equals=" + paymentMethodId);

        // Get all the memberContributionList where paymentMethod equals to paymentMethodId + 1
        defaultMemberContributionShouldNotBeFound("paymentMethodId.equals=" + (paymentMethodId + 1));
    }


    @Test
    @Transactional
    public void getAllMemberContributionsByPromiseIsEqualToSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);
        MemberPromise promise = MemberPromiseResourceIT.createEntity(em);
        em.persist(promise);
        em.flush();
        memberContribution.setPromise(promise);
        memberContributionRepository.saveAndFlush(memberContribution);
        Long promiseId = promise.getId();

        // Get all the memberContributionList where promise equals to promiseId
        defaultMemberContributionShouldBeFound("promiseId.equals=" + promiseId);

        // Get all the memberContributionList where promise equals to promiseId + 1
        defaultMemberContributionShouldNotBeFound("promiseId.equals=" + (promiseId + 1));
    }


    @Test
    @Transactional
    public void getAllMemberContributionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        memberContributionRepository.saveAndFlush(memberContribution);
        ContributionType type = ContributionTypeResourceIT.createEntity(em);
        em.persist(type);
        em.flush();
        memberContribution.setType(type);
        memberContributionRepository.saveAndFlush(memberContribution);
        Long typeId = type.getId();

        // Get all the memberContributionList where type equals to typeId
        defaultMemberContributionShouldBeFound("typeId.equals=" + typeId);

        // Get all the memberContributionList where type equals to typeId + 1
        defaultMemberContributionShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemberContributionShouldBeFound(String filter) throws Exception {
        restMemberContributionMockMvc.perform(get("/api/member-contributions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberContribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restMemberContributionMockMvc.perform(get("/api/member-contributions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemberContributionShouldNotBeFound(String filter) throws Exception {
        restMemberContributionMockMvc.perform(get("/api/member-contributions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemberContributionMockMvc.perform(get("/api/member-contributions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMemberContribution() throws Exception {
        // Get the memberContribution
        restMemberContributionMockMvc.perform(get("/api/member-contributions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberContribution() throws Exception {
        // Initialize the database
        memberContributionService.save(memberContribution);

        int databaseSizeBeforeUpdate = memberContributionRepository.findAll().size();

        // Update the memberContribution
        MemberContribution updatedMemberContribution = memberContributionRepository.findById(memberContribution.getId()).get();
        // Disconnect from session so that the updates on updatedMemberContribution are not directly saved in db
        em.detach(updatedMemberContribution);
        updatedMemberContribution
            .paymentDate(UPDATED_PAYMENT_DATE)
            .amount(UPDATED_AMOUNT);

        restMemberContributionMockMvc.perform(put("/api/member-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMemberContribution)))
            .andExpect(status().isOk());

        // Validate the MemberContribution in the database
        List<MemberContribution> memberContributionList = memberContributionRepository.findAll();
        assertThat(memberContributionList).hasSize(databaseSizeBeforeUpdate);
        MemberContribution testMemberContribution = memberContributionList.get(memberContributionList.size() - 1);
        assertThat(testMemberContribution.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testMemberContribution.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingMemberContribution() throws Exception {
        int databaseSizeBeforeUpdate = memberContributionRepository.findAll().size();

        // Create the MemberContribution

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberContributionMockMvc.perform(put("/api/member-contributions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberContribution)))
            .andExpect(status().isBadRequest());

        // Validate the MemberContribution in the database
        List<MemberContribution> memberContributionList = memberContributionRepository.findAll();
        assertThat(memberContributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMemberContribution() throws Exception {
        // Initialize the database
        memberContributionService.save(memberContribution);

        int databaseSizeBeforeDelete = memberContributionRepository.findAll().size();

        // Delete the memberContribution
        restMemberContributionMockMvc.perform(delete("/api/member-contributions/{id}", memberContribution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MemberContribution> memberContributionList = memberContributionRepository.findAll();
        assertThat(memberContributionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberContribution.class);
        MemberContribution memberContribution1 = new MemberContribution();
        memberContribution1.setId(1L);
        MemberContribution memberContribution2 = new MemberContribution();
        memberContribution2.setId(memberContribution1.getId());
        assertThat(memberContribution1).isEqualTo(memberContribution2);
        memberContribution2.setId(2L);
        assertThat(memberContribution1).isNotEqualTo(memberContribution2);
        memberContribution1.setId(null);
        assertThat(memberContribution1).isNotEqualTo(memberContribution2);
    }
}
