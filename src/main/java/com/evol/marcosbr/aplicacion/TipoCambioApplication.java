package com.evol.marcosbr.aplicacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.evol.marcosbr.seguridad.JWTAuthorizationFilter;
import com.evol.marcosbr.util.TablasH2;

/**
*
* @author Marcos Bayona Rijalba
*/
@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan(basePackages = {"com.evol.marcosbr"})
public class TipoCambioApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate template;

    public static void main(String[] args) {
        SpringApplication.run(TipoCambioApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TipoCambioApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {

        // Para 'moneda'
        TablasH2.crearTablaMoneda(template);
        TablasH2.insertarRegistrosTablaMoneda(template);
        // Para 'tipocambio'
        TablasH2.crearTablaTipoCambio(template);
        TablasH2.insertarRegistrosTablaTipoCambio(template);
        // Para 'usuario'
        TablasH2.crearTablaUsuario(template);
        TablasH2.insertarRegistrosTablaUsuario(template);
    }

	@EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/usuario/logeo").permitAll()
                    .anyRequest().authenticated();
//            http
//                    .csrf()
//                    .disable()
//                    .authorizeRequests()
//                    .antMatchers("/**").permitAll()
//                    .anyRequest().authenticated();
        }
    }

}
