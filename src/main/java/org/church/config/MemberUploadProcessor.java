package org.church.config;

import org.apache.commons.lang3.StringUtils;
import org.church.domain.*;
import org.church.domain.enumeration.*;
import org.church.dto.MemberUploadDto;
import org.church.repository.*;
import org.church.service.MemberService;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@StepScope
public class MemberUploadProcessor implements ItemProcessor<MemberUploadDto, Member> {

    @Value("#{jobParameters[churchId]}") private Long churchId;

    @Value("#{jobParameters[fyId]}") private Long fyId;

    private FinancialYear fy = null;

    private  Church church = null;

    @Autowired
    private HomeChurchCommunityRepository homeChurchCommunityRepository;

    @Autowired
    private ChurchRepository churchRepository;

    @Autowired
    private ChurchCommunityRepository communityRepository;

    @Autowired
    private PeriodContributionTypeRepository contributionTypeRepository;

    @Autowired
    private FinancialYearRepository financialYearRepository;

    @Autowired
    private MemberService memberService;

    @PostConstruct
    public void init() {
        if (fyId != null) {
            this.fy = financialYearRepository.getOne(fyId);
        }
        if (churchId != null) {
            this.church = churchRepository.getOne(churchId);
        }
    }

    @Override
    public Member process(MemberUploadDto dto) throws Exception {

        if (church != null && !dto.getJina().isEmpty()) {

                Member m = new Member();
                m.setChurch(this.church);
                m.setMemberRn(memberService.getMemberRn(church));

                // Calculate Names
                String[] nameStr = dto.getJina().split(" ");
                if (nameStr.length >= 3) {
                    m.setFirstName(nameStr[0]);
                    m.setMiddleName(nameStr[1]);
                    m.setLastName(nameStr[2]);
                } else if (nameStr.length == 2) {
                    m.setFirstName(nameStr[0]);
                    m.setLastName(nameStr[1]);
                } else {
                    m.setFirstName(nameStr[0]);
                }

                // Calculate Gender
                if (!dto.getJinsia().isEmpty()) {
                    if (dto.getJinsia().equals("KE")) {
                        m.setGender(Gender.Female);
                    } else {
                        m.setGender(Gender.Male);
                    }
                }
                m.setChurchRn(dto.getNamba_ya_ahadi());

                // Calculate Date of birth
                if (!dto.getTarehe_ya_kuzaliwa().isEmpty()) {
                    try {
                        List<String> dobStr = new ArrayList<>();
                        if (dto.getTarehe_ya_kuzaliwa().contains("/")) {
                            dobStr = Arrays.asList(dto.getTarehe_ya_kuzaliwa().split("/"));
                        } else if (dto.getTarehe_ya_kuzaliwa().contains(".")) {
                            dobStr = Arrays.asList(dto.getTarehe_ya_kuzaliwa().split("\\."));
                        } else if (dto.getTarehe_ya_kuzaliwa().length() == 4) {
                            dobStr.add(0, dto.getTarehe_ya_kuzaliwa());
                        }

                        if (dobStr.size() > 0) {
                            String dob = null;
                            if (dobStr.size() == 3 && dobStr.get(2).length() == 4) {
                                dob = dobStr.get(2).concat("-").concat(StringUtils.leftPad(dobStr.get(1), 2, "0")).concat("-").concat(StringUtils.leftPad(dobStr.get(0), 2, "0"));
                            } else if (dobStr.size() == 2 && dobStr.get(1).length() == 4) {
                                dob = dobStr.get(1).concat("-").concat(StringUtils.leftPad(dobStr.get(0), 2, "0")).concat("-01");
                            } else if (dobStr.size() == 1 && dobStr.get(0).length() == 4) {
                                dob = dobStr.get(0).concat("-01-01");
                            }
                            if (dob != null) {
                                m.setDateOfBirth(LocalDate.parse(dob));
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("DOB error*************************");
                    }
                }

                // Calculate Place of birth
                if (!dto.getAlipozaliwa().isEmpty()) {
                    m.setPlaceOfBirth(dto.getAlipozaliwa());
                }

                // Calculate Marital Status
                if (!dto.getHali_ya_ndoa().isEmpty()) {
                    if (dto.getHali_ya_ndoa().toLowerCase().contains("ameoa") || dto.getHali_ya_ndoa().toLowerCase().contains("ameolewa")) {
                        m.setMaritalStatus(MaritalStatus.Married);

                        MemberRite rt = new MemberRite();
                        rt.setMember(m);
                        rt.setRiteType(RiteType.NDOA);
                        m.getMemberRites().add(rt);
                    } else if (dto.getHali_ya_ndoa().toLowerCase().contains("hajaoa") || dto.getHali_ya_ndoa().toLowerCase().contains("hajaolewa")) {
                        m.setMaritalStatus(MaritalStatus.Single);
                    } else if (dto.getHali_ya_ndoa().toLowerCase().contains("tengana")) {
                        m.setMaritalStatus(MaritalStatus.Devoced);
                    } else if (dto.getHali_ya_ndoa().toLowerCase().contains("mjane")) {
                        m.setMaritalStatus(MaritalStatus.Widow);
                    }
                }

                // Calculate relative
                if (!dto.getMwenza().isEmpty()) {
                    MemberRelative rl = new MemberRelative();
                    rl.setMember(m);
                    rl.setRelativeFullName(dto.getMwenza());
                    rl.setRelativeType(RelativeType.SPOUSE);
                    m.getRelatives().add(rl);
                }
                if(!dto.getSimu().isEmpty()) {
                    m.setPhoneNumber(dto.getSimu());
                }
                if(!dto.getSimu2().isEmpty()) {
                    m.setPhoneNumber2(dto.getSimu2());
                }
                if(!dto.getMzee_wa_kanisa().isEmpty()) {
                    m.setChurchElder(dto.getMzee_wa_kanisa());
                }
                if(!dto.getNamba_ya_mzee().isEmpty()) {
                    m.setChurchElderPhone(dto.getNamba_ya_mzee());
                }
                if (!dto.getShughuli().isEmpty()) {
                    m.setWork(dto.getShughuli());
                }

                // Calculate Ubatizo
                if (!dto.getUbatizo().isEmpty()) {
                    MemberRite rt = new MemberRite();
                    rt.setMember(m);
                    rt.setRiteType(RiteType.UBATIZO);
                    try {
                        List<String> rtDStr = new ArrayList<>();
                        if (dto.getUbatizo().contains("/")) {
                            rtDStr = Arrays.asList(dto.getUbatizo().split("/"));
                        } else if (dto.getUbatizo().contains(".")) {
                            rtDStr = Arrays.asList(dto.getUbatizo().split("\\."));
                        } else if (dto.getUbatizo().length() == 4) {
                            rtDStr.add(0, dto.getKipaimara());
                        }

                        String rtD = null;
                        if (rtDStr.size() == 3) {
                            rtD = rtDStr.get(2).concat("-").concat(StringUtils.leftPad(rtDStr.get(1), 2, "0")).concat("-").concat(StringUtils.leftPad(rtDStr.get(0), 2, "0"));
                        } else if (rtDStr.size() == 2) {
                            rtD = rtDStr.get(1).concat("-").concat(StringUtils.leftPad(rtDStr.get(0), 2, "0")).concat("-01");
                        } else if (rtDStr.size() == 1) {
                            rtD = rtDStr.get(0).concat("-01-01");
                        }
                        if (rtD != null) {
                            rt.setDateReceived(LocalDate.parse(rtD));

                        }
                    } catch (Exception e){
                        System.out.println("Ubatizo error*************************");
                    }
                    m.getMemberRites().add(rt);
                }

                // Calculate Kipaimara
                if (!dto.getKipaimara().isEmpty()) {
                    MemberRite rt = new MemberRite();
                    rt.setMember(m);
                    rt.setRiteType(RiteType.KIPAIMARA);
                    try {
                        List<String> rtDStr = new ArrayList<>();
                        if (dto.getKipaimara().contains("/")) {
                            rtDStr = Arrays.asList(dto.getKipaimara().split("/"));
                        } else if (dto.getKipaimara().contains(".")) {
                            rtDStr = Arrays.asList(dto.getKipaimara().split("\\."));
                        } else if (dto.getKipaimara().length() == 4) {
                            rtDStr.add(0, dto.getKipaimara());
                        }
                        String rtD = null;
                        if (rtDStr.size() == 3) {
                            rtD = rtDStr.get(2).concat("-").concat(StringUtils.leftPad(rtDStr.get(1), 2, "0")).concat("-").concat(StringUtils.leftPad(rtDStr.get(0), 2, "0"));
                        } else if (rtDStr.size() == 2) {
                            rtD = rtDStr.get(1).concat("-").concat(StringUtils.leftPad(rtDStr.get(0), 2, "0")).concat("-01");
                        } else if (rtDStr.size() == 1 && rtDStr.get(0).length() == 4) {
                            rtD = rtDStr.get(0).concat("-01-01");
                        }
                        if (rtD != null) {
                            rt.setDateReceived(LocalDate.parse(rtD));

                        }
                    } catch (Exception e) {
                        System.out.println("Kipaimara error*************************");
                    }
                    m.getMemberRites().add(rt);
                }

                // Calculate Ndoa
                if (!dto.getNdoa().isEmpty()) {
                    if(dto.getNdoa().toLowerCase().contains("kikristo")) {
                        m.setMarriageType(MarriageType.Christian);
                    } else if (dto.getNdoa().toLowerCase().contains("hapana")) {
                        m.setMarriageType(MarriageType.NonChristian);
                    }
                }
                // Calculate Home Church Community
                if (!dto.getJumuiya().isEmpty()) {
                    HomeChurchCommunity homeChurchCommunity = homeChurchCommunityRepository.findFirstByNameIgnoreCaseAndChurch_Id(dto.getJumuiya(), church.getId());
                    if (homeChurchCommunity == null) {
                        homeChurchCommunity = homeChurchCommunityRepository.save(new HomeChurchCommunity(dto.getJumuiya(), church));
                    }
                    m.setHomeChurchCommunity(homeChurchCommunity);
                }
                // Calculate Church Communities
                if (!dto.getHuduma().isEmpty()) {
                    List<String> com = new ArrayList<>();
                    com = Arrays.asList(dto.getHuduma().split("/"));
                    com.forEach(c -> {
                        ChurchCommunity community = communityRepository.findFirstByNameIgnoreCaseAndChurch_Id(c, church.getId());
                        if (community == null) {
                            community = communityRepository.save(new ChurchCommunity(c, church));
                        }
                        m.getChurchCommunities().add(community);
                    });
                }
                // Calculate Ahadi
                if (fy != null) {
                    if (!dto.getAhadi().isEmpty()) {
                        PeriodContributionType type = contributionTypeRepository.findFirstByCodeIgnoreCase("ahadi");
                        // TODO if no type found through error
                        BigDecimal amount = null;
                        try {
                            amount = BigDecimal.valueOf(Long.parseLong(dto.getAhadi()));
                        } catch (Exception e) {
                        }
                        if (type != null && amount != null) {

                            MemberPromise promise = new MemberPromise(LocalDate.now(), type, church, m, amount, fy);
                            m.getMemberPromises().add(promise);
                        }
                    }

                    // Calculate Jengo
                    if (!dto.getJengo().isEmpty()) {
                        PeriodContributionType type = contributionTypeRepository.findFirstByCodeIgnoreCase("jengo");
                        // TODO if no type found through error
                        BigDecimal amount = null;
                        try {
                            amount = BigDecimal.valueOf(Long.parseLong(dto.getJengo()));
                        } catch (Exception e) {
                        }
                        if (type != null && amount != null) {

                            MemberPromise promise = new MemberPromise(LocalDate.now(), type, church, m, amount, fy);
                            m.getMemberPromises().add(promise);
                        }
                    }
                }
                System.out.println(m);
                return m;

        }
        return null;
    }

    public Long getChurchId() {
        return churchId;
    }

    public void setChurchId(Long churchId) {
        this.churchId = churchId;
    }

    public Long getFyId() {
        return fyId;
    }

    public void setFyId(Long fyId) {
        this.fyId = fyId;
    }
}
