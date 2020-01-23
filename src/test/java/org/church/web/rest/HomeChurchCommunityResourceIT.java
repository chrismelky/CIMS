package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.HomeChurchCommunity;
import org.church.domain.Church;
import org.church.domain.Member;
import org.church.repository.HomeChurchCommunityRepository;
import org.church.service.HomeChurchCommunityService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.HomeChurchCommunityQueryService;

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
 * Integration tests for the {@link HomeChurchCommunityResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class HomeChurchCommunityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_HOUSE_HOLD = 1;
    private static final Integer UPDATED_NUMBER_OF_HOUSE_HOLD = 2;
    private static final Integer SMALLER_NUMBER_OF_HOUSE_HOLD = 1 - 1;

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private HomeChurchCommunityRepository homeChurchCommunityRepository;

    @Autowired
    private HomeChurchCommunityService homeChurchCommunityService;

    @Autowired
    private HomeChurchCommunityQueryService homeChurchCommunityQueryService;

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

    private MockMvc restHomeChurchCommunityMockMvc;

    private HomeChurchCommunity homeChurchCommunity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HomeChurchCommunityResource homeChurchCommunityResource = new HomeChurchCommunityResource(homeChurchCommunityService, homeChurchCommunityQueryService);
        this.restHomeChurchCommunityMockMvc = MockMvcBuilders.standaloneSetup(homeChurchCommunityResource)
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
    public static HomeChurchCommunity createEntity(EntityManager em) {
        HomeChurchCommunity homeChurchCommunity = new HomeChurchCommunity()
            .name(DEFAULT_NAME)
            .numberOfHouseHold(DEFAULT_NUMBER_OF_HOUSE_HOLD)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        homeChurchCommunity.setChurch(church);
        return homeChurchCommunity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HomeChurchCommunity createUpdatedEntity(EntityManager em) {
        HomeChurchCommunity homeChurchCommunity = new HomeChurchCommunity()
            .name(UPDATED_NAME)
            .numberOfHouseHold(UPDATED_NUMBER_OF_HOUSE_HOLD)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createUpdatedEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        homeChurchCommunity.setChurch(church);
        return homeChurchCommunity;
    }

    @BeforeEach
    public void initTest() {
        homeChurchCommunity = createEntity(em);
    }

    @Test
    @Transactional
    public void createHomeChurchCommunity() throws Exception {
        int databaseSizeBeforeCreate = homeChurchCommunityRepository.findAll().size();

        // Create the HomeChurchCommunity
        restHomeChurchCommunityMockMvc.perform(post("/api/home-church-communities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homeChurchCommunity)))
            .andExpect(status().isCreated());

        // Validate the HomeChurchCommunity in the database
        List<HomeChurchCommunity> homeChurchCommunityList = homeChurchCommunityRepository.findAll();
        assertThat(homeChurchCommunityList).hasSize(databaseSizeBeforeCreate + 1);
        HomeChurchCommunity testHomeChurchCommunity = homeChurchCommunityList.get(homeChurchCommunityList.size() - 1);
        assertThat(testHomeChurchCommunity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHomeChurchCommunity.getNumberOfHouseHold()).isEqualTo(DEFAULT_NUMBER_OF_HOUSE_HOLD);
        assertThat(testHomeChurchCommunity.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testHomeChurchCommunity.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createHomeChurchCommunityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = homeChurchCommunityRepository.findAll().size();

        // Create the HomeChurchCommunity with an existing ID
        homeChurchCommunity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHomeChurchCommunityMockMvc.perform(post("/api/home-church-communities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homeChurchCommunity)))
            .andExpect(status().isBadRequest());

        // Validate the HomeChurchCommunity in the database
        List<HomeChurchCommunity> homeChurchCommunityList = homeChurchCommunityRepository.findAll();
        assertThat(homeChurchCommunityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = homeChurchCommunityRepository.findAll().size();
        // set the field null
        homeChurchCommunity.setName(null);

        // Create the HomeChurchCommunity, which fails.

        restHomeChurchCommunityMockMvc.perform(post("/api/home-church-communities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homeChurchCommunity)))
            .andExpect(status().isBadRequest());

        List<HomeChurchCommunity> homeChurchCommunityList = homeChurchCommunityRepository.findAll();
        assertThat(homeChurchCommunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunities() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList
        restHomeChurchCommunityMockMvc.perform(get("/api/home-church-communities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homeChurchCommunity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].numberOfHouseHold").value(hasItem(DEFAULT_NUMBER_OF_HOUSE_HOLD)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }
    
    @Test
    @Transactional
    public void getHomeChurchCommunity() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get the homeChurchCommunity
        restHomeChurchCommunityMockMvc.perform(get("/api/home-church-communities/{id}", homeChurchCommunity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(homeChurchCommunity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.numberOfHouseHold").value(DEFAULT_NUMBER_OF_HOUSE_HOLD))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where name equals to DEFAULT_NAME
        defaultHomeChurchCommunityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the homeChurchCommunityList where name equals to UPDATED_NAME
        defaultHomeChurchCommunityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where name not equals to DEFAULT_NAME
        defaultHomeChurchCommunityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the homeChurchCommunityList where name not equals to UPDATED_NAME
        defaultHomeChurchCommunityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHomeChurchCommunityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the homeChurchCommunityList where name equals to UPDATED_NAME
        defaultHomeChurchCommunityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where name is not null
        defaultHomeChurchCommunityShouldBeFound("name.specified=true");

        // Get all the homeChurchCommunityList where name is null
        defaultHomeChurchCommunityShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where name contains DEFAULT_NAME
        defaultHomeChurchCommunityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the homeChurchCommunityList where name contains UPDATED_NAME
        defaultHomeChurchCommunityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where name does not contain DEFAULT_NAME
        defaultHomeChurchCommunityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the homeChurchCommunityList where name does not contain UPDATED_NAME
        defaultHomeChurchCommunityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNumberOfHouseHoldIsEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where numberOfHouseHold equals to DEFAULT_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldBeFound("numberOfHouseHold.equals=" + DEFAULT_NUMBER_OF_HOUSE_HOLD);

        // Get all the homeChurchCommunityList where numberOfHouseHold equals to UPDATED_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldNotBeFound("numberOfHouseHold.equals=" + UPDATED_NUMBER_OF_HOUSE_HOLD);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNumberOfHouseHoldIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where numberOfHouseHold not equals to DEFAULT_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldNotBeFound("numberOfHouseHold.notEquals=" + DEFAULT_NUMBER_OF_HOUSE_HOLD);

        // Get all the homeChurchCommunityList where numberOfHouseHold not equals to UPDATED_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldBeFound("numberOfHouseHold.notEquals=" + UPDATED_NUMBER_OF_HOUSE_HOLD);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNumberOfHouseHoldIsInShouldWork() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where numberOfHouseHold in DEFAULT_NUMBER_OF_HOUSE_HOLD or UPDATED_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldBeFound("numberOfHouseHold.in=" + DEFAULT_NUMBER_OF_HOUSE_HOLD + "," + UPDATED_NUMBER_OF_HOUSE_HOLD);

        // Get all the homeChurchCommunityList where numberOfHouseHold equals to UPDATED_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldNotBeFound("numberOfHouseHold.in=" + UPDATED_NUMBER_OF_HOUSE_HOLD);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNumberOfHouseHoldIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where numberOfHouseHold is not null
        defaultHomeChurchCommunityShouldBeFound("numberOfHouseHold.specified=true");

        // Get all the homeChurchCommunityList where numberOfHouseHold is null
        defaultHomeChurchCommunityShouldNotBeFound("numberOfHouseHold.specified=false");
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNumberOfHouseHoldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where numberOfHouseHold is greater than or equal to DEFAULT_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldBeFound("numberOfHouseHold.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_HOUSE_HOLD);

        // Get all the homeChurchCommunityList where numberOfHouseHold is greater than or equal to UPDATED_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldNotBeFound("numberOfHouseHold.greaterThanOrEqual=" + UPDATED_NUMBER_OF_HOUSE_HOLD);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNumberOfHouseHoldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where numberOfHouseHold is less than or equal to DEFAULT_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldBeFound("numberOfHouseHold.lessThanOrEqual=" + DEFAULT_NUMBER_OF_HOUSE_HOLD);

        // Get all the homeChurchCommunityList where numberOfHouseHold is less than or equal to SMALLER_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldNotBeFound("numberOfHouseHold.lessThanOrEqual=" + SMALLER_NUMBER_OF_HOUSE_HOLD);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNumberOfHouseHoldIsLessThanSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where numberOfHouseHold is less than DEFAULT_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldNotBeFound("numberOfHouseHold.lessThan=" + DEFAULT_NUMBER_OF_HOUSE_HOLD);

        // Get all the homeChurchCommunityList where numberOfHouseHold is less than UPDATED_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldBeFound("numberOfHouseHold.lessThan=" + UPDATED_NUMBER_OF_HOUSE_HOLD);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByNumberOfHouseHoldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where numberOfHouseHold is greater than DEFAULT_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldNotBeFound("numberOfHouseHold.greaterThan=" + DEFAULT_NUMBER_OF_HOUSE_HOLD);

        // Get all the homeChurchCommunityList where numberOfHouseHold is greater than SMALLER_NUMBER_OF_HOUSE_HOLD
        defaultHomeChurchCommunityShouldBeFound("numberOfHouseHold.greaterThan=" + SMALLER_NUMBER_OF_HOUSE_HOLD);
    }


    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultHomeChurchCommunityShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the homeChurchCommunityList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultHomeChurchCommunityShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultHomeChurchCommunityShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the homeChurchCommunityList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultHomeChurchCommunityShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultHomeChurchCommunityShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the homeChurchCommunityList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultHomeChurchCommunityShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where phoneNumber is not null
        defaultHomeChurchCommunityShouldBeFound("phoneNumber.specified=true");

        // Get all the homeChurchCommunityList where phoneNumber is null
        defaultHomeChurchCommunityShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultHomeChurchCommunityShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the homeChurchCommunityList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultHomeChurchCommunityShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultHomeChurchCommunityShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the homeChurchCommunityList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultHomeChurchCommunityShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where address equals to DEFAULT_ADDRESS
        defaultHomeChurchCommunityShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the homeChurchCommunityList where address equals to UPDATED_ADDRESS
        defaultHomeChurchCommunityShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where address not equals to DEFAULT_ADDRESS
        defaultHomeChurchCommunityShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the homeChurchCommunityList where address not equals to UPDATED_ADDRESS
        defaultHomeChurchCommunityShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultHomeChurchCommunityShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the homeChurchCommunityList where address equals to UPDATED_ADDRESS
        defaultHomeChurchCommunityShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where address is not null
        defaultHomeChurchCommunityShouldBeFound("address.specified=true");

        // Get all the homeChurchCommunityList where address is null
        defaultHomeChurchCommunityShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByAddressContainsSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where address contains DEFAULT_ADDRESS
        defaultHomeChurchCommunityShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the homeChurchCommunityList where address contains UPDATED_ADDRESS
        defaultHomeChurchCommunityShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);

        // Get all the homeChurchCommunityList where address does not contain DEFAULT_ADDRESS
        defaultHomeChurchCommunityShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the homeChurchCommunityList where address does not contain UPDATED_ADDRESS
        defaultHomeChurchCommunityShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByChurchIsEqualToSomething() throws Exception {
        // Get already existing entity
        Church church = homeChurchCommunity.getChurch();
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);
        Long churchId = church.getId();

        // Get all the homeChurchCommunityList where church equals to churchId
        defaultHomeChurchCommunityShouldBeFound("churchId.equals=" + churchId);

        // Get all the homeChurchCommunityList where church equals to churchId + 1
        defaultHomeChurchCommunityShouldNotBeFound("churchId.equals=" + (churchId + 1));
    }


    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByChairManIsEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);
        Member chairMan = MemberResourceIT.createEntity(em);
        em.persist(chairMan);
        em.flush();
        homeChurchCommunity.setChairman(chairMan);
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);
        Long chairManId = chairMan.getId();

        // Get all the homeChurchCommunityList where chairMan equals to chairManId
        defaultHomeChurchCommunityShouldBeFound("chairManId.equals=" + chairManId);

        // Get all the homeChurchCommunityList where chairMan equals to chairManId + 1
        defaultHomeChurchCommunityShouldNotBeFound("chairManId.equals=" + (chairManId + 1));
    }


    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesBySecreatryIsEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);
        Member secreatry = MemberResourceIT.createEntity(em);
        em.persist(secreatry);
        em.flush();
        homeChurchCommunity.setSecretary(secreatry);
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);
        Long secreatryId = secreatry.getId();

        // Get all the homeChurchCommunityList where secreatry equals to secreatryId
        defaultHomeChurchCommunityShouldBeFound("secreatryId.equals=" + secreatryId);

        // Get all the homeChurchCommunityList where secreatry equals to secreatryId + 1
        defaultHomeChurchCommunityShouldNotBeFound("secreatryId.equals=" + (secreatryId + 1));
    }


    @Test
    @Transactional
    public void getAllHomeChurchCommunitiesByTreasurerIsEqualToSomething() throws Exception {
        // Initialize the database
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);
        Member treasurer = MemberResourceIT.createEntity(em);
        em.persist(treasurer);
        em.flush();
        homeChurchCommunity.setTreasurer(treasurer);
        homeChurchCommunityRepository.saveAndFlush(homeChurchCommunity);
        Long treasurerId = treasurer.getId();

        // Get all the homeChurchCommunityList where treasurer equals to treasurerId
        defaultHomeChurchCommunityShouldBeFound("treasurerId.equals=" + treasurerId);

        // Get all the homeChurchCommunityList where treasurer equals to treasurerId + 1
        defaultHomeChurchCommunityShouldNotBeFound("treasurerId.equals=" + (treasurerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHomeChurchCommunityShouldBeFound(String filter) throws Exception {
        restHomeChurchCommunityMockMvc.perform(get("/api/home-church-communities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homeChurchCommunity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].numberOfHouseHold").value(hasItem(DEFAULT_NUMBER_OF_HOUSE_HOLD)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restHomeChurchCommunityMockMvc.perform(get("/api/home-church-communities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHomeChurchCommunityShouldNotBeFound(String filter) throws Exception {
        restHomeChurchCommunityMockMvc.perform(get("/api/home-church-communities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHomeChurchCommunityMockMvc.perform(get("/api/home-church-communities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHomeChurchCommunity() throws Exception {
        // Get the homeChurchCommunity
        restHomeChurchCommunityMockMvc.perform(get("/api/home-church-communities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHomeChurchCommunity() throws Exception {
        // Initialize the database
        homeChurchCommunityService.save(homeChurchCommunity);

        int databaseSizeBeforeUpdate = homeChurchCommunityRepository.findAll().size();

        // Update the homeChurchCommunity
        HomeChurchCommunity updatedHomeChurchCommunity = homeChurchCommunityRepository.findById(homeChurchCommunity.getId()).get();
        // Disconnect from session so that the updates on updatedHomeChurchCommunity are not directly saved in db
        em.detach(updatedHomeChurchCommunity);
        updatedHomeChurchCommunity
            .name(UPDATED_NAME)
            .numberOfHouseHold(UPDATED_NUMBER_OF_HOUSE_HOLD)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS);

        restHomeChurchCommunityMockMvc.perform(put("/api/home-church-communities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHomeChurchCommunity)))
            .andExpect(status().isOk());

        // Validate the HomeChurchCommunity in the database
        List<HomeChurchCommunity> homeChurchCommunityList = homeChurchCommunityRepository.findAll();
        assertThat(homeChurchCommunityList).hasSize(databaseSizeBeforeUpdate);
        HomeChurchCommunity testHomeChurchCommunity = homeChurchCommunityList.get(homeChurchCommunityList.size() - 1);
        assertThat(testHomeChurchCommunity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHomeChurchCommunity.getNumberOfHouseHold()).isEqualTo(UPDATED_NUMBER_OF_HOUSE_HOLD);
        assertThat(testHomeChurchCommunity.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testHomeChurchCommunity.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingHomeChurchCommunity() throws Exception {
        int databaseSizeBeforeUpdate = homeChurchCommunityRepository.findAll().size();

        // Create the HomeChurchCommunity

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomeChurchCommunityMockMvc.perform(put("/api/home-church-communities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homeChurchCommunity)))
            .andExpect(status().isBadRequest());

        // Validate the HomeChurchCommunity in the database
        List<HomeChurchCommunity> homeChurchCommunityList = homeChurchCommunityRepository.findAll();
        assertThat(homeChurchCommunityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHomeChurchCommunity() throws Exception {
        // Initialize the database
        homeChurchCommunityService.save(homeChurchCommunity);

        int databaseSizeBeforeDelete = homeChurchCommunityRepository.findAll().size();

        // Delete the homeChurchCommunity
        restHomeChurchCommunityMockMvc.perform(delete("/api/home-church-communities/{id}", homeChurchCommunity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HomeChurchCommunity> homeChurchCommunityList = homeChurchCommunityRepository.findAll();
        assertThat(homeChurchCommunityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomeChurchCommunity.class);
        HomeChurchCommunity homeChurchCommunity1 = new HomeChurchCommunity();
        homeChurchCommunity1.setId(1L);
        HomeChurchCommunity homeChurchCommunity2 = new HomeChurchCommunity();
        homeChurchCommunity2.setId(homeChurchCommunity1.getId());
        assertThat(homeChurchCommunity1).isEqualTo(homeChurchCommunity2);
        homeChurchCommunity2.setId(2L);
        assertThat(homeChurchCommunity1).isNotEqualTo(homeChurchCommunity2);
        homeChurchCommunity1.setId(null);
        assertThat(homeChurchCommunity1).isNotEqualTo(homeChurchCommunity2);
    }
}
