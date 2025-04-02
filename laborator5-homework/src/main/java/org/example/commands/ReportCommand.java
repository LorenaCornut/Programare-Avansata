package org.example.commands;

import freemarker.template.*;
import org.example.repository.Repository;
import org.example.exceptions.*;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class ReportCommand implements Command {
    private final Repository repository;
    private final String outputPath;

    public ReportCommand(Repository repository, String[] args) throws InvalidCommandException {
        if (args.length != 1) {
            throw new InvalidCommandException("Usage: report <output.html>");
        }
        this.repository = repository;
        this.outputPath = args[0];
    }

    @Override
    public void execute() throws ReportGenerationException {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31); ///creaza un obiect Configuration pentru FreeMarker cu versiunea 2.3.31
            cfg.setClassForTemplateLoading(getClass(), "/"); ///seteaza locatia sabloanelor in src/main/resources
            cfg.setDefaultEncoding("UTF-8"); ///evita problemele cu diacritice

            Template template = cfg.getTemplate("report_template.ftl"); ///cauta si incarca sablonul report_template.ftl -> Acest fișier trebuie să fie salvat în src/main/resources/report_template.ftl

            Map<String, Object> data = new HashMap<>(); ///Creează un dicționar (Map<String, Object>) cu datele pe care le va folosi șablonul .ftl
            data.put("images", repository.getImages()); /// lista de imagini.
            data.put("title", "Image Repository Report"); /// titlul paginii

            try (FileWriter out = new FileWriter(outputPath)) { /// Deschide un FileWriter pentru a scrie în fișierul HTML
                template.process(data, out); /// înlocuiește variabilele din șablon cu datele din data
            }

            System.out.println("Report generated: " + outputPath);
        } catch (Exception e) {
            throw new ReportGenerationException("Failed to generate report: " + e.getMessage());
        }
    }
}