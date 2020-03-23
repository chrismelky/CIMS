package org.church.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, org.church.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, org.church.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, org.church.domain.User.class.getName());
            createCache(cm, org.church.domain.Authority.class.getName());
            createCache(cm, org.church.domain.User.class.getName() + ".authorities");
            createCache(cm, org.church.domain.ChurchType.class.getName());
            createCache(cm, org.church.domain.ChurchCommunity.class.getName());
            createCache(cm, org.church.domain.ChurchCommunity.class.getName() + ".members");
            createCache(cm, org.church.domain.Church.class.getName());
            createCache(cm, org.church.domain.ChurchActivity.class.getName());
            createCache(cm, org.church.domain.ChuchService.class.getName());
            createCache(cm, org.church.domain.PaymentMethod.class.getName());
            createCache(cm, org.church.domain.ContributionType.class.getName());
            createCache(cm, org.church.domain.Member.class.getName());
            createCache(cm, org.church.domain.Member.class.getName() + ".relatives");
            createCache(cm, org.church.domain.Member.class.getName() + ".churchCommunities");
            createCache(cm, org.church.domain.Member.class.getName() + ".memberRites");
            createCache(cm, org.church.domain.Rite.class.getName());
            createCache(cm, org.church.domain.MemberRite.class.getName());
            createCache(cm, org.church.domain.MemberPromise.class.getName());
            createCache(cm, org.church.domain.MemberRelative.class.getName());
            createCache(cm, org.church.domain.MemberContribution.class.getName());
            createCache(cm, org.church.domain.HomeChurchCommunity.class.getName());
            createCache(cm, org.church.domain.PeriodType.class.getName());
            createCache(cm, org.church.domain.Period.class.getName());
            createCache(cm, org.church.domain.PeriodContributionType.class.getName());
            createCache(cm, org.church.domain.PeriodContribution.class.getName());
            createCache(cm, org.church.domain.PeriodContributionItem.class.getName());
            createCache(cm, org.church.domain.FinancialYear.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}
