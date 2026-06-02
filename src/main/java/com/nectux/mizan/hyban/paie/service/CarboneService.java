package com.nectux.mizan.hyban.paie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nectux.mizan.hyban.paie.dto.BulletinPaieDTO;
import com.nectux.mizan.hyban.paie.dto.RapportBullDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

//@Service
//@Profile("!local") // Actif seulement si le profil n'est PAS "local"
@Service
public class CarboneService {

    @Value("${carbone.url}")
    private String carboneUrl;

    private final ObjectMapper objectMapper;

    public CarboneService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] generateBulletinPdf(
            RapportBullDTO dto) throws Exception {

        RestTemplate restTemplate =
                new RestTemplate();

        String baseUrl = carboneUrl.endsWith("/render")
                ? carboneUrl.substring(0, carboneUrl.length() - "/render".length())
                : carboneUrl;

        MultiValueMap<String, Object> templateBody =
                new LinkedMultiValueMap<>();

        templateBody.add(
                "template",
                new ClassPathResource(
                        "templates/Bulletin_carbonev3.xlsx")
        );

        HttpHeaders templateHeaders = new HttpHeaders();
        templateHeaders.setContentType(
                MediaType.MULTIPART_FORM_DATA
        );

        HttpEntity<MultiValueMap<String, Object>> templateRequest =
                new HttpEntity<>(templateBody, templateHeaders);

        ResponseEntity<Map> templateResponse =
                restTemplate.postForEntity(
                        baseUrl + "/template",
                        templateRequest,
                        Map.class
                );

        Map templateResponseBody = templateResponse.getBody();
        Map templateData = templateResponseBody == null ? null : (Map) templateResponseBody.get("data");
        String templateId = valueAsString(
                templateData == null ? null : templateData.get("templateId")
        );
        if (templateId == null) {
            templateId = valueAsString(
                    templateData == null ? null : templateData.get("id")
            );
        }
        if (templateId == null) {
            templateId = valueAsString(
                    templateResponseBody == null ? null : templateResponseBody.get("templateId")
            );
        }
        if (templateId == null) {
            throw new IllegalStateException("Carbone templateId introuvable dans la réponse: " + templateResponseBody);
        }

        Map<String, Object> carboneData =
                objectMapper.convertValue(dto.getBulletinPaie(), Map.class);
        Map<String, Object> bulletinPaieData = new HashMap<>(carboneData);
        Object contratPersonnel = bulletinPaieData.get("contratPersonnel");
        if (contratPersonnel instanceof Map) {
            bulletinPaieData.put("contraPersonnel", contratPersonnel);
            Object personnel = ((Map) contratPersonnel).get("personnel");
            if (personnel instanceof Map) {
                bulletinPaieData.put("nom", ((Map) personnel).get("nom"));
                bulletinPaieData.put("prenom", ((Map) personnel).get("prenom"));
                bulletinPaieData.put("matricule", ((Map) personnel).get("matricule"));
                bulletinPaieData.put("numeroCnps", ((Map) personnel).get("numeroCnps"));
            }
        }

        Object imprimBulletinPaies =
                objectMapper.convertValue(dto.getImprimBulletinPaies(), Object.class);
        if (imprimBulletinPaies instanceof Iterable) {
            for (Object item : (Iterable) imprimBulletinPaies) {
                if (item instanceof Map && ((Map) item).containsKey("tauxPatron")) {
                    ((Map) item).put("tauxPatronal", ((Map) item).get("tauxPatron"));
                }
            }
        }

        carboneData.put("bulletinPaie", bulletinPaieData);
        carboneData.put("imprimBulletinPaies", imprimBulletinPaies);
        carboneData.put("listImprimBulletinPaie", imprimBulletinPaies);

        Map<String, Object> renderBody = new HashMap<>();
        renderBody.put("data", carboneData);
        renderBody.put("convertTo", "pdf");

        HttpHeaders renderHeaders = new HttpHeaders();
        renderHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> renderRequest =
                new HttpEntity<>(renderBody, renderHeaders);

        ResponseEntity<Map> renderResponse =
                restTemplate.postForEntity(
                        baseUrl + "/render/" + templateId,
                        renderRequest,
                        Map.class
                );

        Map renderResponseBody = renderResponse.getBody();
        Map renderData = renderResponseBody == null ? null : (Map) renderResponseBody.get("data");
        String renderId = valueAsString(
                renderData == null ? null : renderData.get("renderId")
        );
        if (renderId == null) {
            renderId = valueAsString(
                    renderData == null ? null : renderData.get("id")
            );
        }
        if (renderId == null) {
            renderId = valueAsString(
                    renderResponseBody == null ? null : renderResponseBody.get("renderId")
            );
        }
        if (renderId == null) {
            throw new IllegalStateException("Carbone renderId introuvable dans la réponse: " + renderResponseBody);
        }

        ResponseEntity<byte[]> reportResponse =
                restTemplate.getForEntity(
                        baseUrl + "/render/" + renderId,
                        byte[].class
                );

        return reportResponse.getBody();
    }

    private String valueAsString(Object value) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value);
        return text.isBlank() || "null".equalsIgnoreCase(text) ? null : text;
    }
}

//    private final RestTemplate restTemplate;
//
//    @Value("${carbone.url}") // Exemple : http://localhost:4000
//    private String carboneUrl;
//
//    public CarboneService(RestTemplateBuilder builder) {
//        this.restTemplate = builder.build();
//    }
//
//    public byte[] generateBulletin(Map<String, Object> data, String format) throws Exception {
//
//        // Charger le template depuis resources
//        InputStream templateStream = getClass()
//                .getResourceAsStream("/templates/Bulletin_de_paie_Type10_optionA.docx");
//
//        if (templateStream == null) {
//            throw new FileNotFoundException("Template non trouvé dans /templates !");
//        }
//
//        byte[] templateBytes = templateStream.readAllBytes();
//        String templateBase64 = Base64.getEncoder().encodeToString(templateBytes);
//
//        // Préparer le payload pour Carbone
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("template", "base64:" + templateBase64);// template en base64
//        payload.put("data", data);              // les données du bulletin
//        payload.put("convertTo", format);       // "pdf" ou "docx"
//
//        // Appel REST POST
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
//
//        ResponseEntity<Map> response = restTemplate.postForEntity(
//                carboneUrl + "/render", request, Map.class);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            // Carbone renvoie "content" encodé en base64
//            String base64Content = (String) response.getBody().get("content");
//            return Base64.getDecoder().decode(base64Content);
//        } else {
//            throw new RuntimeException("Erreur Carbone : " + response.getStatusCode());
//        }
//    }
//}

