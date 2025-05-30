package org.booklibrary.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class HomeController {

    private final OpenAPI openAPI;

    public HomeController(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    @GetMapping(value = "/openapi.yaml", produces = "application/vnd.oai.openapi")
    @ResponseBody
    public String openapiYaml() throws JsonProcessingException {
        return Yaml.mapper().writeValueAsString(openAPI);
    }

    @GetMapping(value = "/openapi.json", produces = "application/json")
    @ResponseBody
    public String openapiJson() throws JsonProcessingException {
        return Json.mapper().writeValueAsString(openAPI);
    }

    @RequestMapping("/oauth/authorize")
    public String authorize() {
        return "forward:/api/oauth/dialog";
    }

}
