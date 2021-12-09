package com.mechanitis.stockui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.mechanitis.stockui.ChartApplication.*;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {
    @Value("classpath:/chart.fxml")
    private Resource chartResource;

    private final String applicationTitle;
    private final ApplicationContext applicationContext;

    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
    }


    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(chartResource.getURL());
            loader.setControllerFactory(applicationContext::getBean);
            Parent parent = loader.load();

            Stage stage = event.getStage();
            stage.setTitle(applicationTitle);

            stage.setScene(new Scene(parent, 800, 600));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }
}
