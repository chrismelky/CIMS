package org.church.web.rest;

import org.church.ChurchApp;
import org.church.domain.Member;
import org.church.domain.MemberRelative;
import org.church.domain.Church;
import org.church.domain.ChurchCommunity;
import org.church.domain.MemberRite;
import org.church.repository.MemberRepository;
import org.church.service.MemberService;
import org.church.web.rest.errors.ExceptionTranslator;
import org.church.service.MemberQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
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
import java.util.ArrayList;
import java.util.List;

import static org.church.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.church.domain.enumeration.Gender;
import org.church.domain.enumeration.MaritalStatus;
/**
 * Integration tests for the {@link MemberResource} REST controller.
 */
@SpringBootTest(classes = ChurchApp.class)
public class MemberResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.Male;
    private static final Gender UPDATED_GENDER = Gender.Female;

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BITH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BITH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BITH = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PLACE_OF_BITH = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_OF_BITH = "BBBBBBBBBB";

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.Single;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.Married;

    private static final String DEFAULT_WORK = "AAAAAAAAAA";
    private static final String UPDATED_WORK = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE_OF_WORK = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_OF_WORK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_DECEASED = false;
    private static final Boolean UPDATED_IS_DECEASED = true;

    private static final LocalDate DEFAULT_DECEASED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DECEASED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DECEASED_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private MemberRepository memberRepository;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private MemberService memberServiceMock;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberQueryService memberQueryService;

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

    private MockMvc restMemberMockMvc;

    private Member member;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MemberResource memberResource = new MemberResource(memberService, memberQueryService);
        this.restMemberMockMvc = MockMvcBuilders.standaloneSetup(memberResource)
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
    public static Member createEntity(EntityManager em) {
        Member member = new Member()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .gender(DEFAULT_GENDER)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .dateOfBith(DEFAULT_DATE_OF_BITH)
            .placeOfBith(DEFAULT_PLACE_OF_BITH)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .work(DEFAULT_WORK)
            .placeOfWork(DEFAULT_PLACE_OF_WORK)
            .isActive(DEFAULT_IS_ACTIVE)
            .isDeceased(DEFAULT_IS_DECEASED)
            .deceasedDate(DEFAULT_DECEASED_DATE);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        member.setChurch(church);
        return member;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createUpdatedEntity(EntityManager em) {
        Member member = new Member()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .gender(UPDATED_GENDER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .dateOfBith(UPDATED_DATE_OF_BITH)
            .placeOfBith(UPDATED_PLACE_OF_BITH)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .work(UPDATED_WORK)
            .placeOfWork(UPDATED_PLACE_OF_WORK)
            .isActive(UPDATED_IS_ACTIVE)
            .isDeceased(UPDATED_IS_DECEASED)
            .deceasedDate(UPDATED_DECEASED_DATE);
        // Add required entity
        Church church;
        if (TestUtil.findAll(em, Church.class).isEmpty()) {
            church = ChurchResourceIT.createUpdatedEntity(em);
            em.persist(church);
            em.flush();
        } else {
            church = TestUtil.findAll(em, Church.class).get(0);
        }
        member.setChurch(church);
        return member;
    }

    @BeforeEach
    public void initTest() {
        member = createEntity(em);
    }

    @Test
    @Transactional
    public void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member
        restMemberMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(member)))
            .andExpect(status().isCreated());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMember.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMember.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testMember.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testMember.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testMember.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMember.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BITH);
        assertThat(testMember.getPlaceOfBirth()).isEqualTo(DEFAULT_PLACE_OF_BITH);
        assertThat(testMember.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testMember.getWork()).isEqualTo(DEFAULT_WORK);
        assertThat(testMember.getPlaceOfWork()).isEqualTo(DEFAULT_PLACE_OF_WORK);
        assertThat(testMember.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testMember.isIsDeceased()).isEqualTo(DEFAULT_IS_DECEASED);
        assertThat(testMember.getDeceasedDate()).isEqualTo(DEFAULT_DECEASED_DATE);
    }

    @Test
    @Transactional
    public void createMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member with an existing ID
        member.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(member)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setFirstName(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(member)))
            .andExpect(status().isBadRequest());

        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setLastName(null);

        // Create the Member, which fails.

        restMemberMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(member)))
            .andExpect(status().isBadRequest());

        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMembers() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList
        restMemberMockMvc.perform(get("/api/members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateOfBith").value(hasItem(DEFAULT_DATE_OF_BITH.toString())))
            .andExpect(jsonPath("$.[*].placeOfBith").value(hasItem(DEFAULT_PLACE_OF_BITH)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].work").value(hasItem(DEFAULT_WORK)))
            .andExpect(jsonPath("$.[*].placeOfWork").value(hasItem(DEFAULT_PLACE_OF_WORK)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeceased").value(hasItem(DEFAULT_IS_DECEASED.booleanValue())))
            .andExpect(jsonPath("$.[*].deceasedDate").value(hasItem(DEFAULT_DECEASED_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllMembersWithEagerRelationshipsIsEnabled() throws Exception {
        MemberResource memberResource = new MemberResource(memberServiceMock, memberQueryService);
        when(memberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restMemberMockMvc = MockMvcBuilders.standaloneSetup(memberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMemberMockMvc.perform(get("/api/members?eagerload=true"))
        .andExpect(status().isOk());

        verify(memberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        MemberResource memberResource = new MemberResource(memberServiceMock, memberQueryService);
            when(memberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restMemberMockMvc = MockMvcBuilders.standaloneSetup(memberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMemberMockMvc.perform(get("/api/members?eagerload=true"))
        .andExpect(status().isOk());

            verify(memberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dateOfBith").value(DEFAULT_DATE_OF_BITH.toString()))
            .andExpect(jsonPath("$.placeOfBith").value(DEFAULT_PLACE_OF_BITH))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.work").value(DEFAULT_WORK))
            .andExpect(jsonPath("$.placeOfWork").value(DEFAULT_PLACE_OF_WORK))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isDeceased").value(DEFAULT_IS_DECEASED.booleanValue()))
            .andExpect(jsonPath("$.deceasedDate").value(DEFAULT_DECEASED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllMembersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where firstName equals to DEFAULT_FIRST_NAME
        defaultMemberShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the memberList where firstName equals to UPDATED_FIRST_NAME
        defaultMemberShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where firstName not equals to DEFAULT_FIRST_NAME
        defaultMemberShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the memberList where firstName not equals to UPDATED_FIRST_NAME
        defaultMemberShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultMemberShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the memberList where firstName equals to UPDATED_FIRST_NAME
        defaultMemberShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where firstName is not null
        defaultMemberShouldBeFound("firstName.specified=true");

        // Get all the memberList where firstName is null
        defaultMemberShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where firstName contains DEFAULT_FIRST_NAME
        defaultMemberShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the memberList where firstName contains UPDATED_FIRST_NAME
        defaultMemberShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where firstName does not contain DEFAULT_FIRST_NAME
        defaultMemberShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the memberList where firstName does not contain UPDATED_FIRST_NAME
        defaultMemberShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllMembersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where lastName equals to DEFAULT_LAST_NAME
        defaultMemberShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the memberList where lastName equals to UPDATED_LAST_NAME
        defaultMemberShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where lastName not equals to DEFAULT_LAST_NAME
        defaultMemberShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the memberList where lastName not equals to UPDATED_LAST_NAME
        defaultMemberShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultMemberShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the memberList where lastName equals to UPDATED_LAST_NAME
        defaultMemberShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where lastName is not null
        defaultMemberShouldBeFound("lastName.specified=true");

        // Get all the memberList where lastName is null
        defaultMemberShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where lastName contains DEFAULT_LAST_NAME
        defaultMemberShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the memberList where lastName contains UPDATED_LAST_NAME
        defaultMemberShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where lastName does not contain DEFAULT_LAST_NAME
        defaultMemberShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the memberList where lastName does not contain UPDATED_LAST_NAME
        defaultMemberShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllMembersByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where middleName equals to DEFAULT_MIDDLE_NAME
        defaultMemberShouldBeFound("middleName.equals=" + DEFAULT_MIDDLE_NAME);

        // Get all the memberList where middleName equals to UPDATED_MIDDLE_NAME
        defaultMemberShouldNotBeFound("middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByMiddleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where middleName not equals to DEFAULT_MIDDLE_NAME
        defaultMemberShouldNotBeFound("middleName.notEquals=" + DEFAULT_MIDDLE_NAME);

        // Get all the memberList where middleName not equals to UPDATED_MIDDLE_NAME
        defaultMemberShouldBeFound("middleName.notEquals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where middleName in DEFAULT_MIDDLE_NAME or UPDATED_MIDDLE_NAME
        defaultMemberShouldBeFound("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME);

        // Get all the memberList where middleName equals to UPDATED_MIDDLE_NAME
        defaultMemberShouldNotBeFound("middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where middleName is not null
        defaultMemberShouldBeFound("middleName.specified=true");

        // Get all the memberList where middleName is null
        defaultMemberShouldNotBeFound("middleName.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where middleName contains DEFAULT_MIDDLE_NAME
        defaultMemberShouldBeFound("middleName.contains=" + DEFAULT_MIDDLE_NAME);

        // Get all the memberList where middleName contains UPDATED_MIDDLE_NAME
        defaultMemberShouldNotBeFound("middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllMembersByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where middleName does not contain DEFAULT_MIDDLE_NAME
        defaultMemberShouldNotBeFound("middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);

        // Get all the memberList where middleName does not contain UPDATED_MIDDLE_NAME
        defaultMemberShouldBeFound("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME);
    }


    @Test
    @Transactional
    public void getAllMembersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where gender equals to DEFAULT_GENDER
        defaultMemberShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the memberList where gender equals to UPDATED_GENDER
        defaultMemberShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllMembersByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where gender not equals to DEFAULT_GENDER
        defaultMemberShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the memberList where gender not equals to UPDATED_GENDER
        defaultMemberShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllMembersByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultMemberShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the memberList where gender equals to UPDATED_GENDER
        defaultMemberShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllMembersByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where gender is not null
        defaultMemberShouldBeFound("gender.specified=true");

        // Get all the memberList where gender is null
        defaultMemberShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllMembersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultMemberShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the memberList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultMemberShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMembersByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultMemberShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the memberList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultMemberShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMembersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultMemberShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the memberList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultMemberShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMembersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phoneNumber is not null
        defaultMemberShouldBeFound("phoneNumber.specified=true");

        // Get all the memberList where phoneNumber is null
        defaultMemberShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultMemberShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the memberList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultMemberShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllMembersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultMemberShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the memberList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultMemberShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllMembersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email equals to DEFAULT_EMAIL
        defaultMemberShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the memberList where email equals to UPDATED_EMAIL
        defaultMemberShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllMembersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email not equals to DEFAULT_EMAIL
        defaultMemberShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the memberList where email not equals to UPDATED_EMAIL
        defaultMemberShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllMembersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultMemberShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the memberList where email equals to UPDATED_EMAIL
        defaultMemberShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllMembersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email is not null
        defaultMemberShouldBeFound("email.specified=true");

        // Get all the memberList where email is null
        defaultMemberShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByEmailContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email contains DEFAULT_EMAIL
        defaultMemberShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the memberList where email contains UPDATED_EMAIL
        defaultMemberShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllMembersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where email does not contain DEFAULT_EMAIL
        defaultMemberShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the memberList where email does not contain UPDATED_EMAIL
        defaultMemberShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllMembersByDateOfBithIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where dateOfBith equals to DEFAULT_DATE_OF_BITH
        defaultMemberShouldBeFound("dateOfBith.equals=" + DEFAULT_DATE_OF_BITH);

        // Get all the memberList where dateOfBith equals to UPDATED_DATE_OF_BITH
        defaultMemberShouldNotBeFound("dateOfBith.equals=" + UPDATED_DATE_OF_BITH);
    }

    @Test
    @Transactional
    public void getAllMembersByDateOfBithIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where dateOfBith not equals to DEFAULT_DATE_OF_BITH
        defaultMemberShouldNotBeFound("dateOfBith.notEquals=" + DEFAULT_DATE_OF_BITH);

        // Get all the memberList where dateOfBith not equals to UPDATED_DATE_OF_BITH
        defaultMemberShouldBeFound("dateOfBith.notEquals=" + UPDATED_DATE_OF_BITH);
    }

    @Test
    @Transactional
    public void getAllMembersByDateOfBithIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where dateOfBith in DEFAULT_DATE_OF_BITH or UPDATED_DATE_OF_BITH
        defaultMemberShouldBeFound("dateOfBith.in=" + DEFAULT_DATE_OF_BITH + "," + UPDATED_DATE_OF_BITH);

        // Get all the memberList where dateOfBith equals to UPDATED_DATE_OF_BITH
        defaultMemberShouldNotBeFound("dateOfBith.in=" + UPDATED_DATE_OF_BITH);
    }

    @Test
    @Transactional
    public void getAllMembersByDateOfBithIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where dateOfBith is not null
        defaultMemberShouldBeFound("dateOfBith.specified=true");

        // Get all the memberList where dateOfBith is null
        defaultMemberShouldNotBeFound("dateOfBith.specified=false");
    }

    @Test
    @Transactional
    public void getAllMembersByDateOfBithIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where dateOfBith is greater than or equal to DEFAULT_DATE_OF_BITH
        defaultMemberShouldBeFound("dateOfBith.greaterThanOrEqual=" + DEFAULT_DATE_OF_BITH);

        // Get all the memberList where dateOfBith is greater than or equal to UPDATED_DATE_OF_BITH
        defaultMemberShouldNotBeFound("dateOfBith.greaterThanOrEqual=" + UPDATED_DATE_OF_BITH);
    }

    @Test
    @Transactional
    public void getAllMembersByDateOfBithIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where dateOfBith is less than or equal to DEFAULT_DATE_OF_BITH
        defaultMemberShouldBeFound("dateOfBith.lessThanOrEqual=" + DEFAULT_DATE_OF_BITH);

        // Get all the memberList where dateOfBith is less than or equal to SMALLER_DATE_OF_BITH
        defaultMemberShouldNotBeFound("dateOfBith.lessThanOrEqual=" + SMALLER_DATE_OF_BITH);
    }

    @Test
    @Transactional
    public void getAllMembersByDateOfBithIsLessThanSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where dateOfBith is less than DEFAULT_DATE_OF_BITH
        defaultMemberShouldNotBeFound("dateOfBith.lessThan=" + DEFAULT_DATE_OF_BITH);

        // Get all the memberList where dateOfBith is less than UPDATED_DATE_OF_BITH
        defaultMemberShouldBeFound("dateOfBith.lessThan=" + UPDATED_DATE_OF_BITH);
    }

    @Test
    @Transactional
    public void getAllMembersByDateOfBithIsGreaterThanSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where dateOfBith is greater than DEFAULT_DATE_OF_BITH
        defaultMemberShouldNotBeFound("dateOfBith.greaterThan=" + DEFAULT_DATE_OF_BITH);

        // Get all the memberList where dateOfBith is greater than SMALLER_DATE_OF_BITH
        defaultMemberShouldBeFound("dateOfBith.greaterThan=" + SMALLER_DATE_OF_BITH);
    }


    @Test
    @Transactional
    public void getAllMembersByPlaceOfBithIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfBith equals to DEFAULT_PLACE_OF_BITH
        defaultMemberShouldBeFound("placeOfBith.equals=" + DEFAULT_PLACE_OF_BITH);

        // Get all the memberList where placeOfBith equals to UPDATED_PLACE_OF_BITH
        defaultMemberShouldNotBeFound("placeOfBith.equals=" + UPDATED_PLACE_OF_BITH);
    }

    @Test
    @Transactional
    public void getAllMembersByPlaceOfBithIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfBith not equals to DEFAULT_PLACE_OF_BITH
        defaultMemberShouldNotBeFound("placeOfBith.notEquals=" + DEFAULT_PLACE_OF_BITH);

        // Get all the memberList where placeOfBith not equals to UPDATED_PLACE_OF_BITH
        defaultMemberShouldBeFound("placeOfBith.notEquals=" + UPDATED_PLACE_OF_BITH);
    }

    @Test
    @Transactional
    public void getAllMembersByPlaceOfBithIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfBith in DEFAULT_PLACE_OF_BITH or UPDATED_PLACE_OF_BITH
        defaultMemberShouldBeFound("placeOfBith.in=" + DEFAULT_PLACE_OF_BITH + "," + UPDATED_PLACE_OF_BITH);

        // Get all the memberList where placeOfBith equals to UPDATED_PLACE_OF_BITH
        defaultMemberShouldNotBeFound("placeOfBith.in=" + UPDATED_PLACE_OF_BITH);
    }

    @Test
    @Transactional
    public void getAllMembersByPlaceOfBithIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfBith is not null
        defaultMemberShouldBeFound("placeOfBith.specified=true");

        // Get all the memberList where placeOfBith is null
        defaultMemberShouldNotBeFound("placeOfBith.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByPlaceOfBithContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfBith contains DEFAULT_PLACE_OF_BITH
        defaultMemberShouldBeFound("placeOfBith.contains=" + DEFAULT_PLACE_OF_BITH);

        // Get all the memberList where placeOfBith contains UPDATED_PLACE_OF_BITH
        defaultMemberShouldNotBeFound("placeOfBith.contains=" + UPDATED_PLACE_OF_BITH);
    }

    @Test
    @Transactional
    public void getAllMembersByPlaceOfBithNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfBith does not contain DEFAULT_PLACE_OF_BITH
        defaultMemberShouldNotBeFound("placeOfBith.doesNotContain=" + DEFAULT_PLACE_OF_BITH);

        // Get all the memberList where placeOfBith does not contain UPDATED_PLACE_OF_BITH
        defaultMemberShouldBeFound("placeOfBith.doesNotContain=" + UPDATED_PLACE_OF_BITH);
    }


    @Test
    @Transactional
    public void getAllMembersByMaritalStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where maritalStatus equals to DEFAULT_MARITAL_STATUS
        defaultMemberShouldBeFound("maritalStatus.equals=" + DEFAULT_MARITAL_STATUS);

        // Get all the memberList where maritalStatus equals to UPDATED_MARITAL_STATUS
        defaultMemberShouldNotBeFound("maritalStatus.equals=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    public void getAllMembersByMaritalStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where maritalStatus not equals to DEFAULT_MARITAL_STATUS
        defaultMemberShouldNotBeFound("maritalStatus.notEquals=" + DEFAULT_MARITAL_STATUS);

        // Get all the memberList where maritalStatus not equals to UPDATED_MARITAL_STATUS
        defaultMemberShouldBeFound("maritalStatus.notEquals=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    public void getAllMembersByMaritalStatusIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where maritalStatus in DEFAULT_MARITAL_STATUS or UPDATED_MARITAL_STATUS
        defaultMemberShouldBeFound("maritalStatus.in=" + DEFAULT_MARITAL_STATUS + "," + UPDATED_MARITAL_STATUS);

        // Get all the memberList where maritalStatus equals to UPDATED_MARITAL_STATUS
        defaultMemberShouldNotBeFound("maritalStatus.in=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    public void getAllMembersByMaritalStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where maritalStatus is not null
        defaultMemberShouldBeFound("maritalStatus.specified=true");

        // Get all the memberList where maritalStatus is null
        defaultMemberShouldNotBeFound("maritalStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllMembersByWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where work equals to DEFAULT_WORK
        defaultMemberShouldBeFound("work.equals=" + DEFAULT_WORK);

        // Get all the memberList where work equals to UPDATED_WORK
        defaultMemberShouldNotBeFound("work.equals=" + UPDATED_WORK);
    }

    @Test
    @Transactional
    public void getAllMembersByWorkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where work not equals to DEFAULT_WORK
        defaultMemberShouldNotBeFound("work.notEquals=" + DEFAULT_WORK);

        // Get all the memberList where work not equals to UPDATED_WORK
        defaultMemberShouldBeFound("work.notEquals=" + UPDATED_WORK);
    }

    @Test
    @Transactional
    public void getAllMembersByWorkIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where work in DEFAULT_WORK or UPDATED_WORK
        defaultMemberShouldBeFound("work.in=" + DEFAULT_WORK + "," + UPDATED_WORK);

        // Get all the memberList where work equals to UPDATED_WORK
        defaultMemberShouldNotBeFound("work.in=" + UPDATED_WORK);
    }

    @Test
    @Transactional
    public void getAllMembersByWorkIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where work is not null
        defaultMemberShouldBeFound("work.specified=true");

        // Get all the memberList where work is null
        defaultMemberShouldNotBeFound("work.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByWorkContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where work contains DEFAULT_WORK
        defaultMemberShouldBeFound("work.contains=" + DEFAULT_WORK);

        // Get all the memberList where work contains UPDATED_WORK
        defaultMemberShouldNotBeFound("work.contains=" + UPDATED_WORK);
    }

    @Test
    @Transactional
    public void getAllMembersByWorkNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where work does not contain DEFAULT_WORK
        defaultMemberShouldNotBeFound("work.doesNotContain=" + DEFAULT_WORK);

        // Get all the memberList where work does not contain UPDATED_WORK
        defaultMemberShouldBeFound("work.doesNotContain=" + UPDATED_WORK);
    }


    @Test
    @Transactional
    public void getAllMembersByPlaceOfWorkIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfWork equals to DEFAULT_PLACE_OF_WORK
        defaultMemberShouldBeFound("placeOfWork.equals=" + DEFAULT_PLACE_OF_WORK);

        // Get all the memberList where placeOfWork equals to UPDATED_PLACE_OF_WORK
        defaultMemberShouldNotBeFound("placeOfWork.equals=" + UPDATED_PLACE_OF_WORK);
    }

    @Test
    @Transactional
    public void getAllMembersByPlaceOfWorkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfWork not equals to DEFAULT_PLACE_OF_WORK
        defaultMemberShouldNotBeFound("placeOfWork.notEquals=" + DEFAULT_PLACE_OF_WORK);

        // Get all the memberList where placeOfWork not equals to UPDATED_PLACE_OF_WORK
        defaultMemberShouldBeFound("placeOfWork.notEquals=" + UPDATED_PLACE_OF_WORK);
    }

    @Test
    @Transactional
    public void getAllMembersByPlaceOfWorkIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfWork in DEFAULT_PLACE_OF_WORK or UPDATED_PLACE_OF_WORK
        defaultMemberShouldBeFound("placeOfWork.in=" + DEFAULT_PLACE_OF_WORK + "," + UPDATED_PLACE_OF_WORK);

        // Get all the memberList where placeOfWork equals to UPDATED_PLACE_OF_WORK
        defaultMemberShouldNotBeFound("placeOfWork.in=" + UPDATED_PLACE_OF_WORK);
    }

    @Test
    @Transactional
    public void getAllMembersByPlaceOfWorkIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfWork is not null
        defaultMemberShouldBeFound("placeOfWork.specified=true");

        // Get all the memberList where placeOfWork is null
        defaultMemberShouldNotBeFound("placeOfWork.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByPlaceOfWorkContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfWork contains DEFAULT_PLACE_OF_WORK
        defaultMemberShouldBeFound("placeOfWork.contains=" + DEFAULT_PLACE_OF_WORK);

        // Get all the memberList where placeOfWork contains UPDATED_PLACE_OF_WORK
        defaultMemberShouldNotBeFound("placeOfWork.contains=" + UPDATED_PLACE_OF_WORK);
    }

    @Test
    @Transactional
    public void getAllMembersByPlaceOfWorkNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where placeOfWork does not contain DEFAULT_PLACE_OF_WORK
        defaultMemberShouldNotBeFound("placeOfWork.doesNotContain=" + DEFAULT_PLACE_OF_WORK);

        // Get all the memberList where placeOfWork does not contain UPDATED_PLACE_OF_WORK
        defaultMemberShouldBeFound("placeOfWork.doesNotContain=" + UPDATED_PLACE_OF_WORK);
    }


    @Test
    @Transactional
    public void getAllMembersByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where isActive equals to DEFAULT_IS_ACTIVE
        defaultMemberShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the memberList where isActive equals to UPDATED_IS_ACTIVE
        defaultMemberShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMembersByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultMemberShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the memberList where isActive not equals to UPDATED_IS_ACTIVE
        defaultMemberShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMembersByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultMemberShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the memberList where isActive equals to UPDATED_IS_ACTIVE
        defaultMemberShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllMembersByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where isActive is not null
        defaultMemberShouldBeFound("isActive.specified=true");

        // Get all the memberList where isActive is null
        defaultMemberShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    public void getAllMembersByIsDeceasedIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where isDeceased equals to DEFAULT_IS_DECEASED
        defaultMemberShouldBeFound("isDeceased.equals=" + DEFAULT_IS_DECEASED);

        // Get all the memberList where isDeceased equals to UPDATED_IS_DECEASED
        defaultMemberShouldNotBeFound("isDeceased.equals=" + UPDATED_IS_DECEASED);
    }

    @Test
    @Transactional
    public void getAllMembersByIsDeceasedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where isDeceased not equals to DEFAULT_IS_DECEASED
        defaultMemberShouldNotBeFound("isDeceased.notEquals=" + DEFAULT_IS_DECEASED);

        // Get all the memberList where isDeceased not equals to UPDATED_IS_DECEASED
        defaultMemberShouldBeFound("isDeceased.notEquals=" + UPDATED_IS_DECEASED);
    }

    @Test
    @Transactional
    public void getAllMembersByIsDeceasedIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where isDeceased in DEFAULT_IS_DECEASED or UPDATED_IS_DECEASED
        defaultMemberShouldBeFound("isDeceased.in=" + DEFAULT_IS_DECEASED + "," + UPDATED_IS_DECEASED);

        // Get all the memberList where isDeceased equals to UPDATED_IS_DECEASED
        defaultMemberShouldNotBeFound("isDeceased.in=" + UPDATED_IS_DECEASED);
    }

    @Test
    @Transactional
    public void getAllMembersByIsDeceasedIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where isDeceased is not null
        defaultMemberShouldBeFound("isDeceased.specified=true");

        // Get all the memberList where isDeceased is null
        defaultMemberShouldNotBeFound("isDeceased.specified=false");
    }

    @Test
    @Transactional
    public void getAllMembersByDeceasedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where deceasedDate equals to DEFAULT_DECEASED_DATE
        defaultMemberShouldBeFound("deceasedDate.equals=" + DEFAULT_DECEASED_DATE);

        // Get all the memberList where deceasedDate equals to UPDATED_DECEASED_DATE
        defaultMemberShouldNotBeFound("deceasedDate.equals=" + UPDATED_DECEASED_DATE);
    }

    @Test
    @Transactional
    public void getAllMembersByDeceasedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where deceasedDate not equals to DEFAULT_DECEASED_DATE
        defaultMemberShouldNotBeFound("deceasedDate.notEquals=" + DEFAULT_DECEASED_DATE);

        // Get all the memberList where deceasedDate not equals to UPDATED_DECEASED_DATE
        defaultMemberShouldBeFound("deceasedDate.notEquals=" + UPDATED_DECEASED_DATE);
    }

    @Test
    @Transactional
    public void getAllMembersByDeceasedDateIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where deceasedDate in DEFAULT_DECEASED_DATE or UPDATED_DECEASED_DATE
        defaultMemberShouldBeFound("deceasedDate.in=" + DEFAULT_DECEASED_DATE + "," + UPDATED_DECEASED_DATE);

        // Get all the memberList where deceasedDate equals to UPDATED_DECEASED_DATE
        defaultMemberShouldNotBeFound("deceasedDate.in=" + UPDATED_DECEASED_DATE);
    }

    @Test
    @Transactional
    public void getAllMembersByDeceasedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where deceasedDate is not null
        defaultMemberShouldBeFound("deceasedDate.specified=true");

        // Get all the memberList where deceasedDate is null
        defaultMemberShouldNotBeFound("deceasedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllMembersByDeceasedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where deceasedDate is greater than or equal to DEFAULT_DECEASED_DATE
        defaultMemberShouldBeFound("deceasedDate.greaterThanOrEqual=" + DEFAULT_DECEASED_DATE);

        // Get all the memberList where deceasedDate is greater than or equal to UPDATED_DECEASED_DATE
        defaultMemberShouldNotBeFound("deceasedDate.greaterThanOrEqual=" + UPDATED_DECEASED_DATE);
    }

    @Test
    @Transactional
    public void getAllMembersByDeceasedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where deceasedDate is less than or equal to DEFAULT_DECEASED_DATE
        defaultMemberShouldBeFound("deceasedDate.lessThanOrEqual=" + DEFAULT_DECEASED_DATE);

        // Get all the memberList where deceasedDate is less than or equal to SMALLER_DECEASED_DATE
        defaultMemberShouldNotBeFound("deceasedDate.lessThanOrEqual=" + SMALLER_DECEASED_DATE);
    }

    @Test
    @Transactional
    public void getAllMembersByDeceasedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where deceasedDate is less than DEFAULT_DECEASED_DATE
        defaultMemberShouldNotBeFound("deceasedDate.lessThan=" + DEFAULT_DECEASED_DATE);

        // Get all the memberList where deceasedDate is less than UPDATED_DECEASED_DATE
        defaultMemberShouldBeFound("deceasedDate.lessThan=" + UPDATED_DECEASED_DATE);
    }

    @Test
    @Transactional
    public void getAllMembersByDeceasedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where deceasedDate is greater than DEFAULT_DECEASED_DATE
        defaultMemberShouldNotBeFound("deceasedDate.greaterThan=" + DEFAULT_DECEASED_DATE);

        // Get all the memberList where deceasedDate is greater than SMALLER_DECEASED_DATE
        defaultMemberShouldBeFound("deceasedDate.greaterThan=" + SMALLER_DECEASED_DATE);
    }


    @Test
    @Transactional
    public void getAllMembersByRelativesIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);
        MemberRelative relatives = MemberRelativeResourceIT.createEntity(em);
        em.persist(relatives);
        em.flush();
        member.addRelatives(relatives);
        memberRepository.saveAndFlush(member);
        Long relativesId = relatives.getId();

        // Get all the memberList where relatives equals to relativesId
        defaultMemberShouldBeFound("relativesId.equals=" + relativesId);

        // Get all the memberList where relatives equals to relativesId + 1
        defaultMemberShouldNotBeFound("relativesId.equals=" + (relativesId + 1));
    }


    @Test
    @Transactional
    public void getAllMembersByChurchIsEqualToSomething() throws Exception {
        // Get already existing entity
        Church church = member.getChurch();
        memberRepository.saveAndFlush(member);
        Long churchId = church.getId();

        // Get all the memberList where church equals to churchId
        defaultMemberShouldBeFound("churchId.equals=" + churchId);

        // Get all the memberList where church equals to churchId + 1
        defaultMemberShouldNotBeFound("churchId.equals=" + (churchId + 1));
    }


    @Test
    @Transactional
    public void getAllMembersByChurchCommunitiesIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);
        ChurchCommunity churchCommunities = ChurchCommunityResourceIT.createEntity(em);
        em.persist(churchCommunities);
        em.flush();
        member.addChurchCommunities(churchCommunities);
        memberRepository.saveAndFlush(member);
        Long churchCommunitiesId = churchCommunities.getId();

        // Get all the memberList where churchCommunities equals to churchCommunitiesId
        defaultMemberShouldBeFound("churchCommunitiesId.equals=" + churchCommunitiesId);

        // Get all the memberList where churchCommunities equals to churchCommunitiesId + 1
        defaultMemberShouldNotBeFound("churchCommunitiesId.equals=" + (churchCommunitiesId + 1));
    }


    @Test
    @Transactional
    public void getAllMembersByMemberRitesIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);
        MemberRite memberRites = MemberRiteResourceIT.createEntity(em);
        em.persist(memberRites);
        em.flush();
        member.addMemberRites(memberRites);
        memberRepository.saveAndFlush(member);
        Long memberRitesId = memberRites.getId();

        // Get all the memberList where memberRites equals to memberRitesId
        defaultMemberShouldBeFound("memberRitesId.equals=" + memberRitesId);

        // Get all the memberList where memberRites equals to memberRitesId + 1
        defaultMemberShouldNotBeFound("memberRitesId.equals=" + (memberRitesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemberShouldBeFound(String filter) throws Exception {
        restMemberMockMvc.perform(get("/api/members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateOfBith").value(hasItem(DEFAULT_DATE_OF_BITH.toString())))
            .andExpect(jsonPath("$.[*].placeOfBith").value(hasItem(DEFAULT_PLACE_OF_BITH)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].work").value(hasItem(DEFAULT_WORK)))
            .andExpect(jsonPath("$.[*].placeOfWork").value(hasItem(DEFAULT_PLACE_OF_WORK)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeceased").value(hasItem(DEFAULT_IS_DECEASED.booleanValue())))
            .andExpect(jsonPath("$.[*].deceasedDate").value(hasItem(DEFAULT_DECEASED_DATE.toString())));

        // Check, that the count call also returns 1
        restMemberMockMvc.perform(get("/api/members/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemberShouldNotBeFound(String filter) throws Exception {
        restMemberMockMvc.perform(get("/api/members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemberMockMvc.perform(get("/api/members/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMember() throws Exception {
        // Initialize the database
        memberService.save(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).get();
        // Disconnect from session so that the updates on updatedMember are not directly saved in db
        em.detach(updatedMember);
        updatedMember
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .gender(UPDATED_GENDER)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .dateOfBith(UPDATED_DATE_OF_BITH)
            .placeOfBith(UPDATED_PLACE_OF_BITH)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .work(UPDATED_WORK)
            .placeOfWork(UPDATED_PLACE_OF_WORK)
            .isActive(UPDATED_IS_ACTIVE)
            .isDeceased(UPDATED_IS_DECEASED)
            .deceasedDate(UPDATED_DECEASED_DATE);

        restMemberMockMvc.perform(put("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMember)))
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMember.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMember.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testMember.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testMember.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testMember.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMember.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BITH);
        assertThat(testMember.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BITH);
        assertThat(testMember.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testMember.getWork()).isEqualTo(UPDATED_WORK);
        assertThat(testMember.getPlaceOfWork()).isEqualTo(UPDATED_PLACE_OF_WORK);
        assertThat(testMember.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testMember.isIsDeceased()).isEqualTo(UPDATED_IS_DECEASED);
        assertThat(testMember.getDeceasedDate()).isEqualTo(UPDATED_DECEASED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Create the Member

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc.perform(put("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(member)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMember() throws Exception {
        // Initialize the database
        memberService.save(member);

        int databaseSizeBeforeDelete = memberRepository.findAll().size();

        // Delete the member
        restMemberMockMvc.perform(delete("/api/members/{id}", member.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Member.class);
        Member member1 = new Member();
        member1.setId(1L);
        Member member2 = new Member();
        member2.setId(member1.getId());
        assertThat(member1).isEqualTo(member2);
        member2.setId(2L);
        assertThat(member1).isNotEqualTo(member2);
        member1.setId(null);
        assertThat(member1).isNotEqualTo(member2);
    }
}
