package com.hzy.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.hzy.realm.MyRealm;
/**
 * 
 *shiro相关配置
 * 
 * @author  liuchangsong
 * @version  [版本号, 2018年10月18日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@SpringBootConfiguration
public class ShiroConfig {
	/**
	 * 根据外部xml文件创建cachemanagerfactorrybean
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Bean
	public EhCacheManagerFactoryBean ehCacheManager() {
		EhCacheManagerFactoryBean ehCache = new EhCacheManagerFactoryBean();
		ehCache.setConfigLocation(new ClassPathResource("shiro-ehcache.xml"));
		ehCache.setShared(true);
		return ehCache;
	}
	/**
	 * 通过EhCacheManagerFactoryBean创建缓存，用于存储shiro授权信息
	 * @param bean    缓存工厂
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Bean
	public EhCacheManager cacheManager(EhCacheManagerFactoryBean bean) {
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManager(bean.getObject());
		return cacheManager;
	}
	/**
	 * 信息散列加密的相关配置
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Bean
	public HashedCredentialsMatcher credentials() {
		HashedCredentialsMatcher credentials= new HashedCredentialsMatcher();
		credentials.setHashAlgorithmName("MD5");
		credentials.setHashIterations(3);
		return credentials;
	}
	/**
	 * 配置自定义的realm
	 * @param credentials   加密规则
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Bean
	public AuthorizingRealm myRealm(HashedCredentialsMatcher credentials) {
		MyRealm myRealm = new MyRealm();
		myRealm.setCredentialsMatcher(credentials);
		return myRealm;
	}
	/**
	 *创建安全管理器
	 * @param realm    自定义的realm
	 * @param cache    缓存
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Bean
	public DefaultWebSecurityManager securityManager(AuthorizingRealm realm,EhCacheManager cache) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(realm);
		securityManager.setCacheManager(cache);
		return securityManager;
	}
	/**
	 *配置shiro拦截器
	 * @param securityManager
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean filter = new ShiroFilterFactoryBean();
		
		Map<String,String> map=new LinkedHashMap<>();        //这里要使用linkedHashMap,要是顺序有序
		map.put("/logout", "logout");
		map.put("/**", "authc");
		
		filter.setLoginUrl("/login");                       //登陆的地址
		filter.setUnauthorizedUrl("/nopermission.jsp");     //未授权的响应地址
		filter.setSecurityManager(securityManager);         //设置安全管理器
		filter.setFilterChainDefinitionMap(map);            //设置拦截器链
		return filter;
	}
	/**
	 *开启aop，对类代理
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
	 @Bean
	 @DependsOn("lifecycleBeanPostProcessor")
	    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
	        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
	        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
	        // https://zhuanlan.zhihu.com/p/29161098
	        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
	        return defaultAdvisorAutoProxyCreator;
	    }
	 /**
	  * 开启shiro注解支持
	  * @param securityManager
	  * @return
	  * @see [类、类#方法、类#成员]
	  */
	 @Bean
	 public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
	        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
	        advisor.setSecurityManager(securityManager);
	        return advisor;
	    }
	 /**
	  *  定义需要特殊处理的异常，用类名或完全路径名作为key，异常页名作为值 
	  *  shiro权限异常处理
	  * @return
	  * @see [类、类#方法、类#成员]
	  */
	 @Bean
	 public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
		 SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
		 Properties prop = new Properties();
		 prop.setProperty("org.apache.shiro.authz.UnauthorizedException", "redirect:/nopermission.jsp");
		 simpleMappingExceptionResolver.setExceptionMappings(prop);
		 return simpleMappingExceptionResolver;
	 }
}
