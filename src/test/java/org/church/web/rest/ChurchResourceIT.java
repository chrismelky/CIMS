package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.Church;
import org.church.domain.Church;
import org.church.domain.ChurchType;
import org.church.repository.ChurchRepository;
import org.church.service.ChurchService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.dto.ChurchCriteria;
import org.church.service.ChurchQueryService;

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
import java.math.BigDecimal;
import java.util.List;

import static org.church.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChurchResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class ChurchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_FAX = "AAAAAAAAAA";
    private static final String UPDATED_FAX = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LONGITUDE = new BigDecimal(1 - 1);

    private static final String DEFAULT_OTHER_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_DETAILS = "BBBBBBBBBB";

    @Autowired
    private ChurchRepository churchRepository;

    @Autowired
    private ChurchService churchService;

    @Autowired
    private ChurchQueryService churchQueryService;

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

    private MockMvc restChurchMockMvc;

    private Church church;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChurchResource churchResource = new ChurchResource(churchService, churchQueryService);
        this.restChurchMockMvc = MockMvcBuilders.standaloneSetup(churchResource)
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
    public static Church createEntity(EntityManager em) {
        Church church = new Church()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .fax(DEFAULT_FAX)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .otherDetails(DEFAULT_OTHER_DETAILS);
        // Add required entity
        ChurchType churchType;
        if (TestUtil.findAll(em, ChurchType.class).isEmpty()) {
            churchType = ChurchTypeResourceIT.createEntity(em);
            em.persist(churchType);
            em.flush();
        } else {
            churchType = TestUtil.findAll(em, ChurchType.class).get(0);
        }
        church.setType(churchType);
        return church;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Church createUpdatedEntity(EntityManager em) {
        Church church = new Church()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .fax(UPDATED_FAX)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .otherDetails(UPDATED_OTHER_DETAILS);
        // Add required entity
        ChurchType churchType;
        if (TestUtil.findAll(em, ChurchType.class).isEmpty()) {
            churchType = ChurchTypeResourceIT.createUpdatedEntity(em);
            em.persist(churchType);
            em.flush();
        } else {
            churchType = TestUtil.findAll(em, ChurchType.class).get(0);
        }
        church.setType(churchType);
        return church;
    }

    @BeforeEach
    public void initTest() {
        church = createEntity(em);
    }

    @Test
    @Transactional
    public void createChurch() throws Exception {
        int databaseSizeBeforeCreate = churchRepository.findAll().size();

        // Create the Church
        restChurchMockMvc.perform(post("/api/churches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(church)))
            .andExpect(status().isCreated());

        // Validate the Church in the database
        List<Church> churchList = churchRepository.findAll();
        assertThat(churchList).hasSize(databaseSizeBeforeCreate + 1);
        Church testChurch = churchList.get(churchList.size() - 1);
        assertThat(testChurch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChurch.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testChurch.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testChurch.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testChurch.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testChurch.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testChurch.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testChurch.getOtherDetails()).isEqualTo(DEFAULT_OTHER_DETAILS);
    }

    @Test
    @Transactional
    public void createChurchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = churchRepository.findAll().size();

        // Create the Church with an existing ID
        church.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChurchMockMvc.perform(post("/api/churches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(church)))
            .andExpect(status().isBadRequest());

        // Validate the Church in the database
        List<Church> churchList = churchRepository.findAll();
        assertThat(churchList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = churchRepository.findAll().size();
        // set the field null
        church.setName(null);

        // Create the Church, which fails.

        restChurchMockMvc.perform(post("/api/churches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(church)))
            .andExpect(status().isBadRequest());

        List<Church> churchList = churchRepository.findAll();
        assertThat(churchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChurches() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList
        restChurchMockMvc.perform(get("/api/churches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(church.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].otherDetails").value(hasItem(DEFAULT_OTHER_DETAILS.toString())));
    }
    
    @Test
    @Transactional
    public void getChurch() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get the church
        restChurchMockMvc.perform(get("/api/churches/{id}", church.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(church.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.otherDetails").value(DEFAULT_OTHER_DETAILS.toString()));
    }

    @Test
    @Transactional
    public void getAllChurchesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where name equals to DEFAULT_NAME
        defaultChurchShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the churchList where name equals to UPDATED_NAME
        defaultChurchShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where name not equals to DEFAULT_NAME
        defaultChurchShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the churchList where name not equals to UPDATED_NAME
        defaultChurchShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where name in DEFAULT_NAME or UPDATED_NAME
        defaultChurchShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the churchList where name equals to UPDATED_NAME
        defaultChurchShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where name is not null
        defaultChurchShouldBeFound("name.specified=true");

        // Get all the churchList where name is null
        defaultChurchShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllChurchesByNameContainsSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where name contains DEFAULT_NAME
        defaultChurchShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the churchList where name contains UPDATED_NAME
        defaultChurchShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllChurchesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where name does not contain DEFAULT_NAME
        defaultChurchShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the churchList where name does not contain UPDATED_NAME
        defaultChurchShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllChurchesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where email equals to DEFAULT_EMAIL
        defaultChurchShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the churchList where email equals to UPDATED_EMAIL
        defaultChurchShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllChurchesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where email not equals to DEFAULT_EMAIL
        defaultChurchShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the churchList where email not equals to UPDATED_EMAIL
        defaultChurchShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllChurchesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultChurchShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the churchList where email equals to UPDATED_EMAIL
        defaultChurchShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllChurchesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where email is not null
        defaultChurchShouldBeFound("email.specified=true");

        // Get all the churchList where email is null
        defaultChurchShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllChurchesByEmailContainsSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where email contains DEFAULT_EMAIL
        defaultChurchShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the churchList where email contains UPDATED_EMAIL
        defaultChurchShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllChurchesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where email does not contain DEFAULT_EMAIL
        defaultChurchShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the churchList where email does not contain UPDATED_EMAIL
        defaultChurchShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllChurchesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultChurchShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the churchList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultChurchShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllChurchesByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultChurchShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the churchList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultChurchShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllChurchesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultChurchShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the churchList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultChurchShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllChurchesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where phoneNumber is not null
        defaultChurchShouldBeFound("phoneNumber.specified=true");

        // Get all the churchList where phoneNumber is null
        defaultChurchShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllChurchesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultChurchShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the churchList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultChurchShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllChurchesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultChurchShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the churchList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultChurchShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllChurchesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where address equals to DEFAULT_ADDRESS
        defaultChurchShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the churchList where address equals to UPDATED_ADDRESS
        defaultChurchShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllChurchesByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where address not equals to DEFAULT_ADDRESS
        defaultChurchShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the churchList where address not equals to UPDATED_ADDRESS
        defaultChurchShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllChurchesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultChurchShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the churchList where address equals to UPDATED_ADDRESS
        defaultChurchShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllChurchesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where address is not null
        defaultChurchShouldBeFound("address.specified=true");

        // Get all the churchList where address is null
        defaultChurchShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllChurchesByAddressContainsSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where address contains DEFAULT_ADDRESS
        defaultChurchShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the churchList where address contains UPDATED_ADDRESS
        defaultChurchShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllChurchesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where address does not contain DEFAULT_ADDRESS
        defaultChurchShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the churchList where address does not contain UPDATED_ADDRESS
        defaultChurchShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllChurchesByFaxIsEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where fax equals to DEFAULT_FAX
        defaultChurchShouldBeFound("fax.equals=" + DEFAULT_FAX);

        // Get all the churchList where fax equals to UPDATED_FAX
        defaultChurchShouldNotBeFound("fax.equals=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    public void getAllChurchesByFaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where fax not equals to DEFAULT_FAX
        defaultChurchShouldNotBeFound("fax.notEquals=" + DEFAULT_FAX);

        // Get all the churchList where fax not equals to UPDATED_FAX
        defaultChurchShouldBeFound("fax.notEquals=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    public void getAllChurchesByFaxIsInShouldWork() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where fax in DEFAULT_FAX or UPDATED_FAX
        defaultChurchShouldBeFound("fax.in=" + DEFAULT_FAX + "," + UPDATED_FAX);

        // Get all the churchList where fax equals to UPDATED_FAX
        defaultChurchShouldNotBeFound("fax.in=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    public void getAllChurchesByFaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where fax is not null
        defaultChurchShouldBeFound("fax.specified=true");

        // Get all the churchList where fax is null
        defaultChurchShouldNotBeFound("fax.specified=false");
    }
                @Test
    @Transactional
    public void getAllChurchesByFaxContainsSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where fax contains DEFAULT_FAX
        defaultChurchShouldBeFound("fax.contains=" + DEFAULT_FAX);

        // Get all the churchList where fax contains UPDATED_FAX
        defaultChurchShouldNotBeFound("fax.contains=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    public void getAllChurchesByFaxNotContainsSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where fax does not contain DEFAULT_FAX
        defaultChurchShouldNotBeFound("fax.doesNotContain=" + DEFAULT_FAX);

        // Get all the churchList where fax does not contain UPDATED_FAX
        defaultChurchShouldBeFound("fax.doesNotContain=" + UPDATED_FAX);
    }


    @Test
    @Transactional
    public void getAllChurchesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where latitude equals to DEFAULT_LATITUDE
        defaultChurchShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the churchList where latitude equals to UPDATED_LATITUDE
        defaultChurchShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where latitude not equals to DEFAULT_LATITUDE
        defaultChurchShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the churchList where latitude not equals to UPDATED_LATITUDE
        defaultChurchShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultChurchShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the churchList where latitude equals to UPDATED_LATITUDE
        defaultChurchShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where latitude is not null
        defaultChurchShouldBeFound("latitude.specified=true");

        // Get all the churchList where latitude is null
        defaultChurchShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllChurchesByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultChurchShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the churchList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultChurchShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultChurchShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the churchList where latitude is less than or equal to SMALLER_LATITUDE
        defaultChurchShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where latitude is less than DEFAULT_LATITUDE
        defaultChurchShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the churchList where latitude is less than UPDATED_LATITUDE
        defaultChurchShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where latitude is greater than DEFAULT_LATITUDE
        defaultChurchShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the churchList where latitude is greater than SMALLER_LATITUDE
        defaultChurchShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }


    @Test
    @Transactional
    public void getAllChurchesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where longitude equals to DEFAULT_LONGITUDE
        defaultChurchShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the churchList where longitude equals to UPDATED_LONGITUDE
        defaultChurchShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where longitude not equals to DEFAULT_LONGITUDE
        defaultChurchShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the churchList where longitude not equals to UPDATED_LONGITUDE
        defaultChurchShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultChurchShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the churchList where longitude equals to UPDATED_LONGITUDE
        defaultChurchShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where longitude is not null
        defaultChurchShouldBeFound("longitude.specified=true");

        // Get all the churchList where longitude is null
        defaultChurchShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllChurchesByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultChurchShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the churchList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultChurchShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultChurchShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the churchList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultChurchShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where longitude is less than DEFAULT_LONGITUDE
        defaultChurchShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the churchList where longitude is less than UPDATED_LONGITUDE
        defaultChurchShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllChurchesByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);

        // Get all the churchList where longitude is greater than DEFAULT_LONGITUDE
        defaultChurchShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the churchList where longitude is greater than SMALLER_LONGITUDE
        defaultChurchShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }


    @Test
    @Transactional
    public void getAllChurchesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        churchRepository.saveAndFlush(church);
        Church parent = ChurchResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        church.setParent(parent);
        churchRepository.saveAndFlush(church);
        Long parentId = parent.getId();

        // Get all the churchList where parent equals to parentId
        defaultChurchShouldBeFound("parentId.equals=" + parentId);

        // Get all the churchList where parent equals to parentId + 1
        defaultChurchShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }


    @Test
    @Transactional
    public void getAllChurchesByTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        ChurchType type = church.getType();
        churchRepository.saveAndFlush(church);
        Long typeId = type.getId();

        // Get all the churchList where type equals to typeId
        defaultChurchShouldBeFound("typeId.equals=" + typeId);

        // Get all the churchList where type equals to typeId + 1
        defaultChurchShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChurchShouldBeFound(String filter) throws Exception {
        restChurchMockMvc.perform(get("/api/churches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(church.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].otherDetails").value(hasItem(DEFAULT_OTHER_DETAILS.toString())));

        // Check, that the count call also returns 1
        restChurchMockMvc.perform(get("/api/churches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChurchShouldNotBeFound(String filter) throws Exception {
        restChurchMockMvc.perform(get("/api/churches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChurchMockMvc.perform(get("/api/churches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingChurch() throws Exception {
        // Get the church
        restChurchMockMvc.perform(get("/api/churches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChurch() throws Exception {
        // Initialize the database
        churchService.save(church);

        int databaseSizeBeforeUpdate = churchRepository.findAll().size();

        // Update the church
        Church updatedChurch = churchRepository.findById(church.getId()).get();
        // Disconnect from session so that the updates on updatedChurch are not directly saved in db
        em.detach(updatedChurch);
        updatedChurch
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .fax(UPDATED_FAX)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .otherDetails(UPDATED_OTHER_DETAILS);

        restChurchMockMvc.perform(put("/api/churches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChurch)))
            .andExpect(status().isOk());

        // Validate the Church in the database
        List<Church> churchList = churchRepository.findAll();
        assertThat(churchList).hasSize(databaseSizeBeforeUpdate);
        Church testChurch = churchList.get(churchList.size() - 1);
        assertThat(testChurch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChurch.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testChurch.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testChurch.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testChurch.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testChurch.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testChurch.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testChurch.getOtherDetails()).isEqualTo(UPDATED_OTHER_DETAILS);
    }

    @Test
    @Transactional
    public void updateNonExistingChurch() throws Exception {
        int databaseSizeBeforeUpdate = churchRepository.findAll().size();

        // Create the Church

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChurchMockMvc.perform(put("/api/churches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(church)))
            .andExpect(status().isBadRequest());

        // Validate the Church in the database
        List<Church> churchList = churchRepository.findAll();
        assertThat(churchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChurch() throws Exception {
        // Initialize the database
        churchService.save(church);

        int databaseSizeBeforeDelete = churchRepository.findAll().size();

        // Delete the church
        restChurchMockMvc.perform(delete("/api/churches/{id}", church.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Church> churchList = churchRepository.findAll();
        assertThat(churchList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Church.class);
        Church church1 = new Church();
        church1.setId(1L);
        Church church2 = new Church();
        church2.setId(church1.getId());
        assertThat(church1).isEqualTo(church2);
        church2.setId(2L);
        assertThat(church1).isNotEqualTo(church2);
        church1.setId(null);
        assertThat(church1).isNotEqualTo(church2);
    }
}
