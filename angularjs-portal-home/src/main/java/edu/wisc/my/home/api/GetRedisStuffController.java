package edu.wisc.my.home.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import edu.wisc.my.home.dao.ISidebarRepository;
import edu.wisc.my.home.model.Sidebar;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@RestController
public class GetRedisStuffController {
    
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ISidebarRepository sidebarRepo;
    
    @RequestMapping("/marketplace.json")
    public @ResponseBody Object getMarketplace(HttpServletRequest request, 
                                               HttpServletResponse response) {
        
        log.trace("Got to marketplace.json");
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
        Map<String, String> marketplace = null;
        try (Jedis jedis = pool.getResource()) {
            jedis.hset("levett", "marketplace", "fname");
            marketplace = jedis.hgetAll("levett");
          }
          pool.close();
        return marketplace;
    }
    
    @RequestMapping("/sidebar.json")
    public @ResponseBody Object getSidebar(HttpServletRequest request, 
                                               HttpServletResponse response) {
        log.trace("Got to sidebar.json");
        
        return sidebarRepo.getAll();
        
    }
    
    @RequestMapping("/resetSidebar")
    public void setSidebar(HttpServletRequest request, 
                                               HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
        String sidebarList = "[{\"title\": \"Home\", \"hyperlink\" : \"/web/#/\", \"target\" : \"\", \"faIcon\" : \"fa-home\", \"displayFlag\" : \"\"},{\"title\": \"Apps\", \"hyperlink\" : \"/web/#/apps\", \"target\" : \"\", \"faIcon\" : \"fa-star\", \"displayFlag\" : \"\"},{\"title\": \"My Profile\", \"hyperlink\" : \"/profile\", \"target\" : \"\", \"faIcon\" : \"fa-user\", \"displayFlag\" : \"myProfileOption\"},{\"title\": \"Beta Settings\", \"hyperlink\" : \"#/settings\", \"target\" : \"\", \"faIcon\" : \"fa-cog\", \"displayFlag\" : \"sidebarShowSettings\"},{\"title\": \"Help\", \"hyperlink\" : \"/web/#/static/myuw-help\", \"target\" : \"\", \"faIcon\" : \"fa-question\", \"displayFlag\" : \"\"},{\"title\": \"Log out\", \"hyperlink\" : \"/portal/Logout\", \"target\" : \"\", \"faIcon\" : \"fa-sign-out\", \"displayFlag\" : \"\"}]";
        
        ObjectMapper om = new ObjectMapper();
        List<Sidebar> ls = om.readValue(sidebarList, 
                        TypeFactory.defaultInstance().constructParametricType(List.class, Sidebar.class));
        sidebarRepo.clear();
        
        for(Sidebar sb : ls) {
            sidebarRepo.add(sb);
        }
    }
    
    @RequestMapping("/addSidebarItem")
    public void addSidebarItem(HttpServletRequest request, 
                               HttpServletResponse response,
                               Sidebar sidebar) {
        sidebarRepo.add(sidebar);
    }
    
    @RequestMapping("/removeSidebarItem/{index}")
    public Long rmSidebarItem(HttpServletRequest request, 
                               HttpServletResponse response,
                               @PathVariable Integer index) {
        return sidebarRepo.rm(index);
    }

}
