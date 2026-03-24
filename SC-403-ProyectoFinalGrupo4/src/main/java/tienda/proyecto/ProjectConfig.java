package tienda.proyecto;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import tienda.proyecto.domain.Ruta;
import tienda.proyecto.service.RutaService;

@Configuration
public class ProjectConfig implements WebMvcConfigurer {

    /* Los siguiente métodos son para implementar el tema de seguridad dentro del proyecto */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registro/nuevo").setViewName("/registro/nuevo");
    }

    /* El siguiente método se utilizar para publicar en la nube, independientemente  */
    @Bean
    public SpringResourceTemplateResolver templateResolver_0() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setOrder(0);
        resolver.setCheckExistence(true);
        return resolver;
    }

    @Bean
    public LocaleResolver localeResolver() {
        var slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.getDefault());
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezome");
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(localeChangeInterceptor());
    }

    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Autowired
    private RutaService rutaService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            RoleHierarchy roleHierarchy) throws Exception {
        var rutas = rutaService.getRutas();

        //esta linea puede ser utilizada como debug de PERMIT ALL y saltarse role check
        //http.authorizeHttpRequests(req -> req.anyRequest().permitAll());
        http.authorizeHttpRequests(requests -> {
            //esto llama a 
//            requests.expressionHandler(handler);

            for (Ruta ruta : rutas) {
                if (ruta.isRequiereRol()) {
                    requests.requestMatchers(ruta.getRuta()).access(hasRoleWithHierarchy(ruta.getRol().getRol(), roleHierarchy));
                    //requests.requestMatchers(ruta.getRuta()).hasRole(ruta.getRol().getRol());
                } else {
                    requests.requestMatchers(ruta.getRuta()).permitAll();
                }
            }
            requests.anyRequest().authenticated();
            //requests.anyRequest().permitAll();
        });
        http.formLogin(form -> form //config de formulario de login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        ).logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).exceptionHandling(exceptions -> exceptions //manejo de excepciones
                .accessDeniedPage("/acceso_denegado")
        ).sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );
        return http.build();
    }

    //esto es para definir jerarquia de roles, que todo lo que user tenga access,
    //vendedor y admin tenga tambien access implicito por se superior
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        roleHierarchy.setHierarchy("""
        ROLE_ADMIN > ROLE_VENDEDOR
        ROLE_VENDEDOR > ROLE_USUARIO
    """);

        //roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }


    
    private AuthorityAuthorizationManager<RequestAuthorizationContext> hasRoleWithHierarchy(
        String role, RoleHierarchy roleHierarchy) {

    AuthorityAuthorizationManager<RequestAuthorizationContext> manager =
            AuthorityAuthorizationManager.hasRole(role);

    manager.setRoleHierarchy(roleHierarchy);
    return manager;
}

}
