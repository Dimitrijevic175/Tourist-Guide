package org.example.backend;

import org.example.backend.repositories.activity.ActivityRepository;
import org.example.backend.repositories.activity.MySqlActivityRepository;
import org.example.backend.repositories.article.ArticleRepository;
import org.example.backend.repositories.article.MySqlArticleRepository;
import org.example.backend.repositories.articleactivity.ArticleActivityRepository;
import org.example.backend.repositories.articleactivity.MySqlArticleActivityRepository;
import org.example.backend.repositories.comment.CommentRepository;
import org.example.backend.repositories.comment.MySqlCommentRepository;
import org.example.backend.repositories.destination.DestinationRepository;
import org.example.backend.repositories.destination.MySqlDestinationRepository;
import org.example.backend.repositories.user.MySqlUserRepository;
import org.example.backend.repositories.user.UserRepository;
import org.example.backend.services.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {

    public HelloApplication() {
        // Ukljucujemo validaciju
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        // Definisemo implementacije u dependency container-u
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                // USERI
                this.bind(MySqlUserRepository.class).to(UserRepository.class).in(Singleton.class);
                // DESTINACIJE
                this.bind(MySqlDestinationRepository.class).to(DestinationRepository.class).in(Singleton.class);
                // AKTIVITIJI
                this.bind(MySqlActivityRepository.class).to(ActivityRepository.class).in(Singleton.class);
                // ARTIKLI (CLANCI)
                this.bind(MySqlArticleRepository.class).to(ArticleRepository.class).in(Singleton.class);
                // KOMENTARI
                this.bind(MySqlCommentRepository.class).to(CommentRepository.class).in(Singleton.class);
                //
                this.bind(MySqlArticleActivityRepository.class).to(ArticleActivityRepository.class).in(Singleton.class);

                this.bindAsContract(UserService.class);
                this.bindAsContract(DestinationService.class);
                this.bindAsContract(ActivityService.class);
                this.bindAsContract(ArticleService.class);
                this.bindAsContract(CommentService.class);
                this.bindAsContract(ArticleActivityService.class);
            }
        };
        register(binder);

        // Ucitavamo resurse
        packages("org.example.backend");

    }

}